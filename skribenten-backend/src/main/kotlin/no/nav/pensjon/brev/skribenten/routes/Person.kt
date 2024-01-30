package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.services.*
import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("personRoute")
fun Route.personRoute(pdlService: PdlService, pensjonPersonDataService: PensjonPersonDataService, krrService: KrrService) {

    route("/person") {
        post<PidRequest>("/navn") {
            respondWithResult2(pdlService.hentNavn(call, it.pid))
        }

        post<PidRequest>("/adresse") {
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(call, it.pid))
        }

        post<PidRequest>("/foretrukketSpraak") {
            call.respond(krrService.getPreferredLocale(call, it.pid))
        }

        post<MottakerSearchRequest>("/soekmottaker") { request ->
            respondWithResult(pdlService.personSoek(call, request))
        }
    }

    post("/pdl/soekmottaker") {
        val request = call.receive<MottakerSearchRequest>()
        respondWithResult(pdlService.personSoek(call, request))
    }

}

data class PidRequest(val pid: String)

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