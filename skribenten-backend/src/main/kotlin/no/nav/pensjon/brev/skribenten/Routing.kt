package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.services.*
import java.util.Base64

data class RenderLetterRequest(val letterData: Any, val editedLetter: EditedJsonLetter?)
data class EditedJsonLetter(val letter: RenderedJsonLetter, val deletedBlocks: Set<Int>)

fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureADService(authConfig)
    val penService = PenService(skribentenConfig.getConfig("services.pen"), authService)
    val brevbakerService = BrevbakerService(skribentenConfig.getConfig("services.brevbaker"), authService)

    routing {
        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        authenticate(authConfig.name) {
            post("/test/pen") {
                val sak = penService.hentSak(call, 22958874)
                respondWithResult(sak)
            }

            get("/test/brevbaker") {
                val brev = brevbakerService.genererBrev(call)
                respondWithResult(brev, onOk = { respondBytes(Base64.getDecoder().decode(it.base64pdf), ContentType.Application.Pdf) })
            }

            get("/template/{brevkode}") {
                val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")

                when (val template = brevbakerService.getTemplate(call, brevkode)) {
                    is ServiceResult.AuthorizationError -> TODO()
                    is ServiceResult.Error -> TODO()
                    is ServiceResult.Ok -> call.respondText(template.result, ContentType.Application.Json)
                }
            }
            post("/letter/{brevkode}") {
                val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")
                val request = call.receive<RenderLetterRequest>()

                when (val rendered = brevbakerService.renderLetter(call, brevkode, request.letterData)) {
                    is ServiceResult.AuthorizationError -> TODO()
                    is ServiceResult.Error -> TODO()
                    is ServiceResult.Ok -> call.respond(
                        request.editedLetter?.let { updatedEditedLetter(it, rendered.result) }
                            ?: rendered.result
                    )
                }
            }
        }
    }
}