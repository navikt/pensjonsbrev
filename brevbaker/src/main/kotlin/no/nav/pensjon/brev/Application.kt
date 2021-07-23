package no.nav.pensjon.brev

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.utils.io.charsets.Charsets
import no.nav.pensjon.brev.api.LetterRequest
import no.nav.pensjon.brev.api.LetterResource
import no.nav.pensjon.brev.api.TemplateResource

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }



    routing {
        get("/templates") {
            call.respond(TemplateResource.getTemplates())
        }

        get("/templates/{name}") {
            val template = TemplateResource.getTemplate(call.parameters["name"]!!)
            if (template == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(template)
            }
        }

        post("/letter") {
            val letter = call.receive<LetterRequest>()
            call.respondOutputStream(ContentType.Text.Plain.withCharset(Charsets.UTF_8)) {
                LetterResource.create(letter)
                    .let { it.template.render(it, this) }
            }
        }
    }
}

