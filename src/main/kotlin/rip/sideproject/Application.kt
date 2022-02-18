package rip.sideproject

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import rip.sideproject.plugins.*
import java.io.File
import java.security.KeyStore

val secrets = Secrets()

fun main() {
    val environment = applicationEngineEnvironment {
        connector {
            port = System.getenv("PORT")?.toInt() ?: 8080
        }

        secrets.config?.ssl?.let { ssl ->
            File(ssl.keyStorePath).takeIf {
                try { it.exists() } catch (e: Exception) { false }
            }?.let { file ->
                sslConnector(
                    keyStore = KeyStore.getInstance(file, ssl.keyStorePassword.toCharArray()),
                    keyAlias = ssl.keyAlias,
                    keyStorePassword = { ssl.keyStorePassword.toCharArray() },
                    privateKeyPassword = { ssl.privateKeyPassword.toCharArray() }
                ) {
                    port = 443
                    keyStorePath = file
                }
            }
        }

        module(Application::module)
    }

    embeddedServer(Netty, environment).start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureHTTP()
    configureSerialization()
}
