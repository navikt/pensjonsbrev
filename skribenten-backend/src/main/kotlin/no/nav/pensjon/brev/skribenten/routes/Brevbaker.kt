package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Sakspart
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Signatur
import no.nav.pensjon.brevbaker.api.model.NAVEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import org.slf4j.LoggerFactory
import java.time.LocalDate


data class RenderLetterRequest(val letterData: GenericBrevdata, val editedLetter: Edit.Letter?)
data class RenderLetterResponse(val editedLetter: Edit.Letter, val title: String, val sakspart: Sakspart, val signatur: Signatur)
class GenericBrevdata : LinkedHashMap<String, Any>(), BrevbakerBrevdata

// TODO: Flytt til topp-rute /brevbaker
fun Route.brevbakerRoute(brevbakerService: BrevbakerService) {
    val logger = LoggerFactory.getLogger("brevbakerRoute")

    get("/template/{brevkode}") {
        val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")
        brevbakerService.getTemplate(call, brevkode)
            .onOk { call.respondText(it, ContentType.Application.Json) }
            .onError { message, status ->
                logger.error("Feil ved henting av brevkode: Status:$status Melding: $message ")
                call.respond(status, message)
            }
    }

    post<RenderLetterRequest>("/letter/{brevkode}") { request ->
        val brevkode = call.parameters.getOrFail<Brevkode.Redigerbar>("brevkode")

        brevbakerService.renderLetter(
            call, brevkode, GeneriskRedigerbarBrevdata(EmptyBrevdata, request.letterData), Felles(
                dokumentDato = LocalDate.now(),
                saksnummer = "1234",
                avsenderEnhet = NAVEnhet("nav.no", "NAV Familie- og pensjonsytelser Porsgrunn", Telefonnummer("22225555")),
                bruker = Bruker(Foedselsnummer("12345678910"), "Test", null, "Testeson"),
                vergeNavn = null,
                signerendeSaksbehandlere = SignerendeSaksbehandlere("Ole Saksbehandler")
            )
        )
            .onOk { rendered ->
                call.respond(
                    RenderLetterResponse(
                        request.editedLetter?.updateEditedLetter(rendered) ?: rendered.toEdit(),
                        rendered.title,
                        rendered.sakspart,
                        rendered.signatur,
                    )
                )
            }.onError { message, status ->
                logger.error("Feil ved rendring av brevbaker brev Brevkode: $brevkode Melding: $message Status: $status")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved rendring av brevbaker brev.")
            }
    }
}

