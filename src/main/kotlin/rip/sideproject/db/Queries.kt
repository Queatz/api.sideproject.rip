package rip.sideproject.db

fun Db.countProjects() = query(Int::class, """
RETURN LENGTH(${Project::class.collection()})
""".trimIndent()).first()!!

fun Db.projects(offset: Int, limit: Int) = list(
    Project::class,
    """
        FOR project IN @@collection
            SORT project.created DESC
            LIMIT @offset, @l
            RETURN project
    """.trimIndent(),
    mapOf(
        "offset" to offset,
        "l" to limit,
    )
)

fun Db.projects(search: String?, offset: Int, limit: Int) = search?.let {
    list(
        Project::class,
        """
        FOR project IN @@collection
            FILTER REGEX_MATCHES(project.name, @search, true)
                OR REGEX_MATCHES(project.link, @search, true)
                OR REGEX_MATCHES(project.purpose, @search, true)
            SORT project.created DESC
            LIMIT @offset, @l
            RETURN project
    """.trimIndent(),
        mapOf(
            "search" to search,
            "offset" to offset,
            "l" to limit,
        )
    )
} ?: projects(offset, limit)

fun Db.projectComments(projectId: String) = list(
    Comment::class,
    """
        FOR comment IN @@collection
                FILTER comment.projectId == @projectId
                SORT comment.created DESC
            RETURN comment
        
    """.trimIndent(),
    mapOf(
        "projectId" to projectId.asId(Project::class),
    )
)
