package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.services.*
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

fun Route.bestillBrevRoute(
    penService: PenService,
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    brevmetadataService: BrevmetadataService,
) {
    post("/bestillbrev") {
        val request = call.receive<OrderLetterRequest>()
        val navIdent = getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker")
        // TODO create respond on error or similar function to avoid boilerplate. RespondOnError?
        val metadata: BrevdataDto = brevmetadataService.getMal(request.brevkode)
        tjenestebussIntegrasjonService.bestillExtreamBrev(call, request, navIdent, metadata)
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