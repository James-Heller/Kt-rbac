package space.jamestang.core.expand.ktorm

import org.ktorm.entity.*
import java.time.*

interface EnhanceEntity<T, E: Entity<E>>: Entity<E> {

    var id: T
    var deleted: Boolean
    var createTime: LocalDateTime
    var updateTime: LocalDateTime
    var deleteTime: LocalDateTime
}