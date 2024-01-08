package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillBrevRequestDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.SakskontekstDto
import no.nav.pensjon.brev.skribenten.services.BrevmetadataService
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.TjenestebussIntegrasjonService
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar


fun Route.bestillBrevRoute(penService: PenService, tjenestebussIntegrasjonService: TjenestebussIntegrasjonService, brevmetadataService: BrevmetadataService) {
    post("/bestillBrev/extream") {
        // TODO skal vi validere metadata?
        val request = call.receive<OrderLetterRequest>()

        brevmetadataService
        val navIdent = getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker")

        // TODO create respond on error or similar function to avoid boilerplate. RespondOnError?
        val metadata = brevmetadataService.getMal(request.brevkode)

        //TODO better error handling.
        // TODO access controls for e-blanketter
        tjenestebussIntegrasjonService.bestillBrev(
            call, BestillBrevRequestDto(
                brevKode = request.brevkode,
                brevGruppe = metadata.brevgruppe?: throw BadRequestException("Fant ikke brevgruppe gitt brev :${request.brevkode}"),
                isRedigerbart = metadata.redigerbart,
                sprakkode = request.spraak.toString(),
                sakskontekstDto = SakskontekstDto(
                    journalenhet = "0001",
                    gjelder = request.gjelderPid,
                    dokumenttype = metadata.dokType,
                    dokumentdato = getCurrentGregorianTime(),
                    fagsystem = "PEN",
                    fagomradekode = "PEN",
                    innhold = metadata.dekode,
                    kategori = metadata.dokumentkategori.toString(),
                    saksid = request.sakId.toString(),// sakid
                    saksbehandlernavn = "saksbehandler saksbehandlerson", // TODO hent navn fra nav ansatt service? eller claim kanskje?
                    saksbehandlerId = navIdent,
                    sensitivitet = "true" // TODO vi må spørre brukeren om brevet er sensitivt eller ikke i grensesnittet. evt gjør om til boolean parameter i tjenestebuss-integrasjon
                )
            )
        )
    }

    post("/bestillBrev/doksys") {

    }
}

fun getCurrentGregorianTime(): XMLGregorianCalendar {
    val cal = GregorianCalendar()
    cal.time = Date()
    return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
}