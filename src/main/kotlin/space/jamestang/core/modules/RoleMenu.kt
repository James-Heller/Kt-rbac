package space.jamestang.core.modules

import org.ktorm.entity.Entity
import org.ktorm.jackson.json
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface RoleMenu : Entity<RoleMenu> {

    companion object : Entity.Factory<RoleMenu>()

    var roleId: Int
    var menuId: List<Int>
}

object RoleMenus : Table<RoleMenu>("rbac_role_menus") {

    val roleId = int("role_id").bindTo { it.roleId }
    val menuId = json<List<Int>>("menus").bindTo { it.menuId }

}