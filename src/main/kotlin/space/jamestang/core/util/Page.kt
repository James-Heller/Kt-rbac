package space.jamestang.core.util

data class Page<E>(
    var currentPage: Int? = null,
    var pageSize: Int? = null,
    var total: Int? = null,
    var record: List<E> = emptyList()
)