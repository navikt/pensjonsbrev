package no.nav.pensjon.brev.skribenten

import com.typesafe.config.Config
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.auth.JwtConfig
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brevbaker.api.model.RenderedJsonLetter
import java.util.*
import kotlin.collections.LinkedHashMap

class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata
data class RenderLetterRequest(val letterData: GenericBrevdata, val editedLetter: EditedJsonLetter?)

data class MottakerSearchRequest(
    val soeketekst: String,
    val recipientType: RecipientType?,
    val location: Place?,
    val kommunenummer: List<String>?,
    val land: String?,
) {
    enum class Place { INNLAND, UTLAND }
    enum class RecipientType { PERSON, SAMHANDLER }
}

data class EditedJsonLetter(val letter: RenderedJsonLetter, val deletedBlocks: Set<Int>)
data class OrderLetterRequest(
    val brevkode: String,
    val spraak: SpraakKode,
    val sakId: Long,
    val gjelderPid: String,
    val landkode: String? = null,
    val mottakerText: String? = null,
)

// TODO innfør X-Request-ID
fun Application.configureRouting(authConfig: JwtConfig, skribentenConfig: Config) {
    // TODO sett opp retry funksjonalitet for ulike tjenester.
    val authService = AzureADService(authConfig)
    val safService = SafService(skribentenConfig.getConfig("services.saf"), authService)
    val penService = PenService(skribentenConfig.getConfig("services.pen"), authService)
    val pensjonPersonDataService = PensjonPersonDataService(skribentenConfig.getConfig("services.pensjon_persondata"), authService)
    val kodeverkService = KodeverkService(skribentenConfig.getConfig("services.kodeverk"))
    val pdlService = PdlService(skribentenConfig.getConfig("services.pdl"), authService)
    val krrService = KrrService(skribentenConfig.getConfig("services.krr"), authService)
    val brevbakerService = BrevbakerService(skribentenConfig.getConfig("services.brevbaker"), authService)
    val brevmetadataService = BrevmetadataService(skribentenConfig.getConfig( "services.brevmetadata"))
    val databaseService = SkribentenFakeDatabaseService()
    routing {
        get("/isAlive") {
            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/isReady") {
            call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        authenticate(authConfig.name) {
            post("/test/pen") {
                respondWithResult(safService.getStatus(call, "453840176"))
            }

            post("/pen/extream") {
                TODO("bestill extream brev via tjenestebuss-integrasjon")
                //// TODO skal vi validere metadata?
                //val request = call.receive<OrderLetterRequest>()
                ////TODO try to get extra claims when authorizing user instead of using graph service.
                //val name = getClaim("name") ?: throw UnauthorizedException("Could not find name of user")

                //// TODO create respond on error or similar function to avoid boilerplate. RespondOnError?
                //val onPremisesSamAccountName: String =
                //    when (val response = microsoftGraphService.getOnPremisesSamAccountName(call)) {
                //        is ServiceResult.Ok -> response.result
                //        is ServiceResult.Error, is ServiceResult.AuthorizationError -> {
                //            respondWithResult(response)
                //            return@post
                //        }
                //    }

                //TODO better error handling.
                // TODO access controls for e-blanketter
                //penService.bestillExtreamBrev(call, request, name, onPremisesSamAccountName).map { journalpostId ->
                //    val error = safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)
                //    if (error != null) {
                //        if (error.type == SafService.JournalpostLoadingError.ErrorType.TIMEOUT) {
                //            call.respond(HttpStatusCode.RequestTimeout, error.error)
                //        } else {
                //            call.respondText(text = error.error, status = HttpStatusCode.InternalServerError)
                //        }
                //    } else {
                //        TODO("bestill extream brev via tjenestebuss-integrasjon")
                //        //respondWithResult(penService.redigerExtreamBrev(call, journalpostId))
                //    }
                //}
            }

            post("/pen/doksys") {
                TODO("rediger doksys brev via tjenestebuss-integrasjon")
                //val name = getClaim("name") ?: throw UnauthorizedException("Could not find name of user")
                //val request = call.receive<OrderLetterRequest>()
                //val onPremisesSamAccountName: String =
                //    when (val response = microsoftGraphService.getOnPremisesSamAccountName(call)) {
                //        is ServiceResult.Ok -> response.result
                //        is ServiceResult.Error, is ServiceResult.AuthorizationError -> {
                //            respondWithResult(response)
                //            return@post
                //        }
                //    }
                //respondWithResult(penService.bestillDoksysBrev(call, request, name, onPremisesSamAccountName))
                // TODO rediger doksys brev via tjenestebuss-integrasjon
                //when (val response = penService.bestillDoksysBrev(call, request, name, onPremisesSamAccountName)) {
                //    is ServiceResult.Ok -> {
                //        val journalpostId = response.result
                //        //respondWithResult(penService.redigerDoksysBrev(call, journalpostId))
                //    }
                //    is ServiceResult.Error, is ServiceResult.AuthorizationError -> {
                //        respondWithResult(response)
                //        return@post
                //    }
                //}
            }

            //TODO Check access using /tilganger(?). Is there an on behalf of endpoint which checks access?
            get("/pen/sak/{sakId}") {
                val sakId = call.parameters.getOrFail("sakId")
                respondWithResult(penService.hentSak(call, sakId))
            }

            get("/pdl/navn/{fnr}") {
                // TODO validate fnr
                val fnr = call.parameters.getOrFail("fnr")
                respondWithResult(pdlService.hentNavn(call, fnr))
            }

            post("/pdl/soekmottaker") {
                val request = call.receive<MottakerSearchRequest>()
                respondWithResult(pdlService.personSoek(call, request))
            }

            get("/adresse/{pid}") {
                val pid = call.parameters.getOrFail("pid")
                respondWithResult(pensjonPersonDataService.hentAdresse(call, pid))
            }

            get("/foretrukketSpraak/{pid}") {
                val pid = call.parameters.getOrFail("pid")
                respondWithResult(krrService.getPreferredLocale(call, pid))
            }

            route("/kodeverk") {
                install(CachingHeaders) {
                    options { _, _ -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 86400)) }
                }
                get("/kommune") {
                    call.respond(kodeverkService.getKommuner(call))
                }
                get("/avtaleland") {
                    respondWithResult(penService.hentAvtaleland(call))
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

            data class LetterTemplatesResponse(
                val kategorier: List<LetterCategory>,
                val eblanketter: List<LetterMetadata>
            )
            get("/lettertemplates/{sakType}") {
                val sakType = call.parameters.getOrFail("sakType")
                call.respond(
                    LetterTemplatesResponse(
                        brevmetadataService.getRedigerbareBrevKategorier(sakType),
                        //TODO figure out who has access to e-blanketter and filter them out. then only display eblanketter when you get the metadata back.
                        brevmetadataService.getEblanketter()
                    )
                )
            }

            post("/favourites") {
                getLoggedInUserId()?.let { call.respond(databaseService.addFavourite(it, call.receive<String>())) }
                    ?: call.respond(HttpStatusCode.Unauthorized)
            }

            delete("/favourites") {
                getLoggedInUserId()?.let { call.respond(databaseService.removeFavourite(it, call.receive<String>())) }
                    ?: call.respond(HttpStatusCode.Unauthorized)
            }

            get("/favourites") {
                getLoggedInUserId()?.let { call.respond(databaseService.getFavourites(it)) }
                    ?: call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

private fun PipelineContext<Unit, ApplicationCall>.getLoggedInUserId(): String? =
    call.authentication.principal<UserPrincipal>()?.getUserId()

private fun PipelineContext<Unit, ApplicationCall>.getClaim(claim: String): String? =
    call.authentication.principal<UserPrincipal>()?.jwtPayload?.getClaim(claim)?.asString()