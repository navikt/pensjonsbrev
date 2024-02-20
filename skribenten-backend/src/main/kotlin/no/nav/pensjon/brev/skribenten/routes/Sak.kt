package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.KrrService
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService
import no.nav.pensjon.brev.skribenten.services.NAVEnhet
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataService
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import no.nav.pensjon.brev.skribenten.services.respondWithResult
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(Route::class.java)

fun Route.sakRoute(
    penService: PenService,
    navansattService: NavansattService,
    legacyBrevService: LegacyBrevService,
    pdlService: PdlService,
    pensjonPersonDataService: PensjonPersonDataService,
    krrService: KrrService,
) {
    route("/sak/{sakId}") {
        intercept(ApplicationCallPipeline.Call) {
            sjekkTilganger(navansattService, pdlService, penService)
        }
        get("") {
            val sakId = call.parameters.getOrFail("sakId")
            respondWithResult(penService.hentSak(call, sakId))
        }
        post("/bestillbrev") {
            val request = call.receive<LegacyBrevService.OrderLetterRequest>()
            call.respond(legacyBrevService.bestillBrev(call, request))
        }
        post<PidRequest>("/navn") {
            respondWithResult(pdlService.hentNavn(call, it.pid))
        }

        post<PidRequest>("/adresse") {
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(call, it.pid))
        }

        post<PidRequest>("/foretrukketSpraak") {
            call.respond(krrService.getPreferredLocale(call, it.pid))
        }
    }
}

data class PidRequest(val pid: String)


private suspend fun PipelineContext<Unit, ApplicationCall>.sjekkGruppetilgang(
    navansattService: NavansattService,
    sakSelection: Deferred<ServiceResult<PenService.SakSelection>>,
    pdlService: PdlService
) {
    navansattService.hentGruppetilgang(call, fetchLoggedInNavIdent(call)).map { grupper ->
        when (val sak = sakSelection.await()) {
            is ServiceResult.Error -> call.respond(HttpStatusCode.InternalServerError, "En feil oppstod under henting av sak fra PEN")
            is ServiceResult.Ok -> {
                when (val adressebeskyttelse = pdlService.hentAdressebeskyttelse(call, sak.result.foedselsnr)) {
                    is ServiceResult.Error -> call.respond(HttpStatusCode.InternalServerError, "En feil oppstod under henting av adressebeskyttelse fra PDL")
                    is ServiceResult.Ok -> {
                        if (grupper.groups.none { gruppe -> gruppe == adressebeskyttelse.result.gruppetilgang }) {
                            call.respond(HttpStatusCode.NotFound) // Vi ønsker ikke å utlevere informasjon som kan utledes
                        }
                    }
                }
            }
        }
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.sjekkTilganger(
    navansattService: NavansattService,
    pdlService: PdlService,
    penService: PenService
) {
    val sakId = call.parameters["sakId"].toString()
    if (sakId.isEmpty()) {
        call.respond(HttpStatusCode.BadRequest, "SakId mangler i request")
        return
    }
    val loggedInNavIdent: String = fetchLoggedInNavIdent(call)
    val sakSelection = async { penService.hentSak(call, sakId) }
    val enheter = async { navansattService.hentNavAnsattEnhetListe(call, loggedInNavIdent) }

    sjekkEnhetstilgang(sakSelection, enheter)
    sjekkGruppetilgang(navansattService, sakSelection, pdlService)
}

private suspend fun PipelineContext<Unit, ApplicationCall>.sjekkEnhetstilgang(
    sakSelection: Deferred<ServiceResult<PenService.SakSelection>>,
    enheter: Deferred<ServiceResult<List<NAVEnhet>>>
) {
    sakSelection.await().map { sak ->
        enheter.await().map { result ->
            result.any { it.id == sak.enhetId }
            if (sak.enhetId.isNullOrEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "Sak har ikke enhetsId")
            } else if (result.none { it.id == sak.enhetId }) {
                val message = "Navansatt: ${fetchLoggedInNavIdent(call)} har ikke enhetstilgang til sak"
                logger.error(message)
                call.respond(HttpStatusCode.Forbidden, message)
            }
        }.catch { message, status ->
            logger.error("En feil oppstod under henting av enheter, status: $status , message: $message")
            call.respond(HttpStatusCode.InternalServerError, "En feil oppstod under henting av enheter")
        }
    }.catch { message, status ->
        logger.error("En feil oppstod under henting av sak, status: $status , message: $message")
        call.respond(HttpStatusCode.InternalServerError, "En feil oppstod under henting av sak")
    }
}

private fun fetchLoggedInNavIdent(call: ApplicationCall): String {
    return call.getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
}
