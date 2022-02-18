package rip.sideproject.plugins

import com.arangodb.velocypack.internal.util.DateUtil
import com.google.gson.*
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.lang.reflect.Type
import java.text.ParseException
import java.time.Instant
import java.util.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            registerTypeAdapter(Instant::class.java, InstantTypeConverter())
        }
    }

    routing {
        get("/hi") {
            call.respond(mapOf("hi" to true))
        }
    }
}

class InstantTypeConverter : JsonSerializer<Instant>, JsonDeserializer<Instant> {
    override fun serialize(
        src: Instant,
        srcType: Type,
        context: JsonSerializationContext
    ) = JsonPrimitive(DateUtil.format(Date.from(src)))

    override fun deserialize(
        json: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ) = try {
        DateUtil.parse(json.asString).toInstant()
    } catch (e: ParseException) {
        null
    }
}
