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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class RenderLetterRequest(val letterData: Any, val editedLetter: EditedJsonLetter?)
data class EditedJsonLetter(val letter: RenderedJsonLetter, val deletedBlocks: Set<Int>)
data class OrderLetterRequest(
    val brevkode: String,
    val spraakKode: SpraakKode,
)

fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    val authService = AzureADService(authConfig)
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
                val sak =
                    penService.bestillExtreamBrev(call, 22972355, spraak = SpraakKode.NB, brevkode = "PE_IY_05_300")
                respondWithResult(sak)
            }

            get("/test/pdl") {
                respondWithResult(pdlService.hentNavn(call, "09417320595"))
            }

            post("/pen/orderExtreamLetter") {
                val request = call.receive<OrderLetterRequest>()
                val journalpostId = penService.bestillExtreamBrev(
                    call,
                    22972355,
                    brevkode = request.brevkode,
                    spraak = request.spraakKode,
                )
                respondWithResult(journalpostId)
            }

            post("/pen/orderDoksysLetter") {
                val journalpostId =
                    penService.bestillExtreamBrev(call, 22972355, spraak = SpraakKode.NB, brevkode = "PE_IY_05_300")
                respondWithResult(journalpostId)
            }

            data class SakSelection(
                val sakId: Long,
                val foedselsnr: String,
                val foedselsdato: String,
                val sakType: String,
            )
            get("/pen/sak/{sakId}") {
                val sakId = call.parameters["sakId"]?.toLongOrNull()
                if (sakId != null) {
                    when (val sakInfo = penService.hentSak(call, sakId)) {
                        is ServiceResult.AuthorizationError -> TODO()
                        is ServiceResult.Error -> TODO()
                        is ServiceResult.Ok -> call.respond(
                            sakInfo.result.let {
                                SakSelection(
                                    sakId = it.sakId,
                                    foedselsnr = it.penPerson.fnr,
                                    foedselsdato = it.penPerson.fodselsdato.format(DateTimeFormatter.ISO_DATE.localizedBy(Locale.forLanguageTag("NB-no"))),
                                    sakType = it.sakType,
                                )
                            }
                        )
                    }
                }
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
            get("/lettertemplates") {
                //TODO supplement the data with extra data from brevbaker
                //TODO fetch templates from brevbaker
                val userId = getLoggedInUserId()
                call.respond(brevmetadataService.getRedigerbareBrevKategorier("UFOREP"))
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
