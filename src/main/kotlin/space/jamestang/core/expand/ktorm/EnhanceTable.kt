package space.jamestang.core.expand.ktorm

import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.schema.*
import space.jamestang.core.plugins.*
import space.jamestang.core.util.*
import java.time.*

abstract class EnhanceTable<E: EnhanceEntity<E>>(tableName: String, primaryKeyName: String="id"): Table<E>(tableName) {

    val id = int(primaryKeyName).primaryKey().bindTo { it.id }
    val deleted = boolean("deleted").bindTo { it.deleted }
    val createTime = datetime("create_time").bindTo { it.createTime }
    val updateTime = datetime("update_time").bindTo { it.updateTime }
    val deleteTime = datetime("delete_time").bindTo { it.deleteTime }

    abstract val sequence: EntitySequence<E, EnhanceTable<E>>



    @JvmName("get-sequence")
    private fun getSequence(): EntitySequence<E, EnhanceTable<E>> {
        return sequence

    }



    fun create(entity: E): Boolean{

        entity.createTime = LocalDateTime.now()
        return sequence.add(entity) == 1
    }

    @Suppress("UNCHECKED_CAST")
    fun batchCreate(entities: List<E>): IntArray {
        val now = LocalDateTime.now()

        val ids = DB.mysql.batchInsert(this){

            for (entity in entities){
                item {
                    for (column in this@EnhanceTable.columns){
                        val v = with(EntityExtensionsApi()){entity.getColumnValue(column.binding!!)}
                        if (column.name == "create_time") {
                            set(column as Column<Any>, now)
                        } else {
                            set(column as Column<Any>, v)
                        }
                    }
                }
            }

        }
        return ids
    }

    fun selectById(id: Int): E? = sequence.find { it.id eq id and (this.deleted eq false) }


    fun <T: Any> selectByColumn(column: Column<T>, value: T): List<E> {

        return sequence.filter { column eq value and (this.deleted eq false)}.toList()
    }

    fun <T: Any> selectUniqueByColumn(column: Column<T>, value: T): E? {
        return sequence.find{ column eq value and (this.deleted eq false) }
    }

    fun getAll(): List<E> = sequence.filter { this.deleted eq false }.toList()

    fun page(page: Int, pageSize: Int): Page<E> {

        val data = DB.mysql.from(this).select().where{ this.deleted eq false }.limit(pageSize).offset((page - 1) * pageSize)
        val total = data.totalRecordsInAllPages
        val pageData = data.map(this::createEntity)


        return Page(page, pageSize, total, pageData)
    }



    fun filterPage(page: Int, pageSize: Int, orderBy: Column<out Any>?=null, filter: (() -> ColumnDeclaring<Boolean>)): Page<E> {

        lateinit var data: Query
        try {
            data = DB.mysql.from(this).select().where{ this.deleted eq false }.where(filter).limit(pageSize).offset((page - 1) * pageSize)
            if (orderBy != null) {
                data = data.orderBy(orderBy.desc())
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        val total = data.totalRecordsInAllPages
        val pageData = data.map(this::createEntity)

        return Page(page, pageSize, total, pageData)
    }

    fun update(entity: E): Boolean = getSequence().update(entity) == 1

    fun <T: Any> update(column: Column<T>, value: T, locate: () -> ColumnDeclaring<Boolean>): Boolean {
        return DB.mysql.update(this){
            set(column, value)
            set(this@EnhanceTable.updateTime, LocalDateTime.now())
            where(locate)
        } == 1
    }

    fun logicDelete(id: Int): Boolean{

        return DB.mysql.update(this){
            set(this@EnhanceTable.deleteTime, LocalDateTime.now())
            set(this@EnhanceTable.deleted, true)
            where { id eq it.id }
        } == 1
    }

    fun delete(id: Int): Boolean = sequence.removeIf { it.id eq id } == 1


}