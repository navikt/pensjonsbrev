package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter
import java.util.*

data class RenderLetterRequest(val letterData: Any, val editedLetter: EditedJsonLetter?)
data class EditedJsonLetter(val letter: RenderedJsonLetter, val deletedBlocks: Set<Int>)
data class OrderLetterRequest(
    val brevkode: String,
    val spraak: SpraakKode,
    val sakId: Long,
    val gjelderPid: String,
)

// TODO innfør nav-call id på ulike kall for feilsøking
fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureADService(authConfig)
    val safService = SafService(skribentenConfig.getConfig("services.saf"), authService)
    val penService = PenService(skribentenConfig.getConfig("services.pen"), authService)
    val pdlService = PdlService(skribentenConfig.getConfig("services.pdl"), authService)
    val brevbakerService = BrevbakerService(skribentenConfig.getConfig("services.brevbaker"), authService)
    val brevmetadataService = BrevmetadataService(skribentenConfig.getConfig("services.brevmetadata"))
    val databaseService = SkribentenFakeDatabaseService(brevmetadataService)

    routing {
        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        authenticate(authConfig.name) {
            post("/test/pen") {
                //respondWithResult(penService.redigerExtreamBrev(call, "453840176"))
                respondWithResult(safService.getStatus(call, "453840176"))
            }

            post("/pen/extream") {
                val request = call.receive<OrderLetterRequest>()
                when (val response = penService.bestillExtreamBrev(call, request)) {
                    is ServiceResult.Ok -> {
                        val journalpostId = response.result
                        val error = safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)
                        if (error != null) {
                            if (error.type == SafService.JournalpostLoadingError.ErrorType.TIMEOUT) {
                                call.respond(HttpStatusCode.RequestTimeout, error.error)
                            } else {
                                call.respondText(text = error.error, status = HttpStatusCode.InternalServerError)
                            }
                        } else {
                            respondWithResult(penService.redigerExtreamBrev(call, journalpostId))
                        }
                    }

                    is ServiceResult.AuthorizationError -> {
                        call.respond(response.error)
                    }

                    is ServiceResult.Error -> {
                        call.respond(response.error)
                    }
                }
            }

            get("/pen/extream/rediger/{journalpostId}") {
                // TODO slå opp dokumentId i skribenten så vi kan sjekke at brukeren har tilgang til dokumentet først.
                val journalpostId = call.parameters["journalpostId"]
                if (journalpostId != null) {
                    respondWithResult(penService.redigerExtreamBrev(call, journalpostId))
                }
            }

            post("/pen/orderDoksysLetter") {
//                val journalpostId =
//                    penService.bestillDoksysBrev(call, 22972355, spraak = SpraakKode.NB, brevkode = "PE_IY_05_300")
//                respondWithResult(journalpostId.toServiceResult())
            }

            get("/pen/sak/{sakId}") {
                val sakId = call.parameters.getOrFail("sakId")
                respondWithResult(penService.hentSak(call, sakId))
            }

            get("/pdl/navn/{fnr}") {
                // TODO validate fnr
                val fnr = call.parameters.getOrFail("fnr")
                respondWithResult(pdlService.hentNavn(call, fnr))
            }

            get("/test/brevbaker") {
                val brev = brevbakerService.genererBrev(call)
                respondWithResult(
                    brev,
                    onOk = { respondBytes(Base64.getDecoder().decode(it.base64pdf), ContentType.Application.Pdf) })
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

            get("/lettertemplates/{sakType}") {
                val sakType = call.parameters.getOrFail("sakType")
                call.respond(brevmetadataService.getRedigerbareBrevKategorier(sakType))
            }

            post("/favourites") {
                call.respond(databaseService.addFavourite(getLoggedInUserId(), call.receive<String>()))
            }

            delete("/favourites") {
                call.respond(databaseService.removeFavourite(getLoggedInUserId(), call.receive<String>()))
            }

            get("/favourites") {
                call.respond(databaseService.getFavourites(getLoggedInUserId()))
            }
        }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.getLoggedInUserId(): String =
    (call.authentication.principal<UserPrincipal>()?.getUserId()
        ?: throw UnauthorizedException("Missing user principal"))
