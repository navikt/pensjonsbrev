package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.services.KrrService
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataService
import no.nav.pensjon.brev.skribenten.services.respondWithResult


fun Route.personRoute(pdlService: PdlService, pensjonPersonDataService: PensjonPersonDataService, krrService: KrrService) {

    route("/person") {
        post<PidRequest>("/navn") {
            respondWithResult(pdlService.hentNavn(call, it.pid))
        }

        post<PidRequest>("/adresse") {
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(call, it.pid))
        }

        post<PidRequest>("/foretrukketSpraak") {
            respondWithResult(krrService.getPreferredLocale(call, it.pid))
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