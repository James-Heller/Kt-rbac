package space.jamestang.core.modules

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int

interface RoleMenu : Entity<RoleMenu> {

    companion object : Entity.Factory<RoleMenu>()

    var roleId: Int
    var menuId: Int
}

object RoleMenus : Table<RoleMenu>("rbac_role_menu") {

    val roleId = int("role_id").bindTo { it.roleId }
    val menuId = int("menu_id").bindTo { it.menuId }

}