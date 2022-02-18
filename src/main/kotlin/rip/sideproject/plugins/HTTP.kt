package rip.sideproject.plugins

import io.ktor.server.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureHTTP() {
    install(Compression)
    install(CORS) {
        method(HttpMethod.Options)
        header(HttpHeaders.Authorization)
        host("localhost:4200")
        host("www.sideproject.rip", listOf("https"))
        host("sideproject.rip", listOf("https"))
        allowNonSimpleContentTypes = true
    }
    install(DefaultHeaders)
}
