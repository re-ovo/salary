package me.rerere.salary.model

data class User(
    val eid: Int,
    val name: String,
    val rank: String,
    val department: String,
    val role: Int
) {
    val roleName get() = when(role) {
        0 -> "员工"
        1 -> "管理员"
        else -> "未知"
    }
}