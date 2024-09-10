package space.jamestang.core.modules

import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import space.jamestang.core.expand.ktorm.EnhanceEntity
import space.jamestang.core.expand.ktorm.EnhanceTable
import space.jamestang.core.plugins.DB

interface Menu : EnhanceEntity<Menu> {

    companion object : Entity.Factory<Menu>()

    var name: String
    var nameEn: String
    var url: String
    var component: String
    var parentId: Int?
    var sort: Int
    var icon: String
    var permission: String
}


object Menus: EnhanceTable<Menu>("rbac_menu"){

    val name = varchar("name").bindTo { it.name }
    val nameEn = varchar("name_en").bindTo { it.nameEn }
    val url = varchar("url").bindTo { it.url }
    val component = varchar("component").bindTo { it.component }
    val parentId = int("parent_id").bindTo { it.parentId }
    val sort = int("sort").bindTo { it.sort }
    val icon = varchar("icon").bindTo { it.icon }
    val permission = varchar("permission").bindTo { it.permission }

    override val sequence: EntitySequence<Menu, EnhanceTable<Menu>> get() = DB.mysql.sequenceOf(this)
}