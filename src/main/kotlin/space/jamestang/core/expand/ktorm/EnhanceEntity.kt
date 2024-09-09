package space.jamestang.core.expand.ktorm

import org.ktorm.entity.*
import java.time.*

interface EnhanceEntity<E: Entity<E>>: Entity<E> {

    var id: Int?
    var deleted: Boolean
    var createTime: LocalDateTime
    var updateTime: LocalDateTime
    var deleteTime: LocalDateTime
}