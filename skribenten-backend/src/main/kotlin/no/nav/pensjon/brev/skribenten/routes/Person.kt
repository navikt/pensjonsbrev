package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.services.KrrService
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataService
import no.nav.pensjon.brev.skribenten.services.respondWithResult

fun Route.personRoute(pdlService: PdlService, pensjonPersonDataService: PensjonPersonDataService, krrService: KrrService) {

    route("/person") {
        get("/navn") {
            withHeaderPid { respondWithResult(pdlService.hentNavn(call, it)) }
        }

        get("/adresse") {
            withHeaderPid { respondWithResult(pensjonPersonDataService.hentAdresse(call, it)) }
        }

        get("/foretrukketSpraak") {
            withHeaderPid { respondWithResult(krrService.getPreferredLocale(call, it)) }
        }

        post<MottakerSearchRequest>("/soekmottaker") { request ->
            respondWithResult(pdlService.personSoek(call, request))
        }
    }

    // TODO: fjern disse når frontend bruker de nye
    get("/pdl/navn/{fnr}") {
        // TODO validate fnr
        val fnr = call.parameters.getOrFail("fnr")
        respondWithResult(pdlService.hentNavn(call, fnr))
    }

    get("/adresse/{pid}") {
        val pid = call.parameters.getOrFail("pid")
        respondWithResult(pensjonPersonDataService.hentAdresse(call, pid))
    }

    get("/foretrukketSpraak/{pid}") {
        val pid = call.parameters.getOrFail("pid")
        respondWithResult(krrService.getPreferredLocale(call, pid))
    }

    post("/pdl/soekmottaker") {
        val request = call.receive<MottakerSearchRequest>()
        respondWithResult(pdlService.personSoek(call, request))
    }

}

private suspend fun PipelineContext<Unit, ApplicationCall>.withHeaderPid(block: suspend PipelineContext<Unit, ApplicationCall>.(pid: String) -> Unit) {
    val pid = call.request.header("pid")
    if (pid?.isNotBlank() == true) {
        block(pid)
    } else {
        call.respond(HttpStatusCode.BadRequest, "Missing 'pid' header (for fnr/dnr)")
    }
}

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