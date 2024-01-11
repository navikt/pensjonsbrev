package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInName
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.*
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

fun Route.bestillBrevRoute(
    penService: PenService,
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    brevmetadataService: BrevmetadataService,
    safService: SafService,
) {
    post("/bestillbrev") {
        val request = call.receive<OrderLetterRequest>()
        val navIdent =
            getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
        val navn = getLoggedInName() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker i claim")

        val metadata: BrevdataDto = brevmetadataService.getMal(request.brevkode)
        val bestillingsResult =
            tjenestebussIntegrasjonService.bestillExtreamBrev(call, request, navIdent, metadata, navn)

        bestillingsResult.map { result ->
            val error = safService.waitForJournalpostStatusUnderArbeid(call, result.journalpostId)

            if (error != null) {
                call.respond(
                    message = when (error.type) {
                        SafService.JournalpostLoadingError.ErrorType.ERROR -> "Feil ved henting av status på journalpost"
                        SafService.JournalpostLoadingError.ErrorType.TIMEOUT -> "Journalposten brukte lang tid på å fullføre. (Timeout)"
                    },
                    status = HttpStatusCode.InternalServerError
                )
            } else {
                respondWithResult(tjenestebussIntegrasjonService.redigerExtreamBrev(call, result.journalpostId))
            }
        }
    }
}

fun getCurrentGregorianTime(): XMLGregorianCalendar {
    val cal = GregorianCalendar()
    cal.time = Date()
    return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
}


data class OrderLetterRequest(
    val brevkode: String,
    val spraak: SpraakKode,
    val sakId: Long,
    val gjelderPid: String,
    val landkode: String? = null,
    val mottakerText: String? = null,
)

data class OrderLetterResponse(
    val uri: String,
)