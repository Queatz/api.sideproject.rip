package rip.sideproject.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import rip.sideproject.db.*
import java.time.Instant

val db = Db()

fun Application.configureRouting() {

    routing {
        get("/projects") {
            call.respond(
               ProjectsResponse(
                   call.parameters["offset"]?.toInt() ?: 0,
                   db.countProjects(),
                   db.projects(
                       call.parameters["search"],
                       call.parameters["offset"]?.toInt()?.coerceAtLeast(0) ?: 0,
                       call.parameters["limit"]?.toInt()?.coerceAtLeast(0) ?: 0
                   )
               )
            )
        }

        post("/projects") {
            call.receive<Project>().let {
                it.name = it.name.trim()
                it.link = it.link.trim()
                it.purpose = it.purpose.trim()

                if (!listOf("http://", "https://").any { prefix -> it.link.startsWith(prefix, true) }) {
                    it.link = "https://${it.link}"
                }

                it.id = null
                it.created = Instant.now()
                call.respond(db.insert(it))
            }
        }

        get("/project/{id}/comments") {
            call.respond(db.projectComments(call.parameters["id"]!!))
        }

        post("/project/{id}/comments") {
            call.receive<Comment>().let {
                it.text = it.text.trim()
                it.id = null
                it.projectId = call.parameters["id"]!!.asId(Project::class)
                it.created = Instant.now()
                call.respond(db.insert(it))
            }
        }
    }
}

data class ProjectsResponse(
    val offset: Int,
    val total: Int,
    val data: List<Project>
)
