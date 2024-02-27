package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.services.*

fun Route.sakRoute(
    penService: PenService,
    navansattService: NavansattService,
    legacyBrevService: LegacyBrevService,
    pdlService: PdlService,
    pensjonPersonDataService: PensjonPersonDataService,
    krrService: KrrService,
) {
    route("/sak/{sakId}") {
        install(AuthorizeAnsattSakTilgang(navansattService, pdlService, penService))

        get {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            call.respond(sak)
        }
        post("/bestillbrev") {
            val request = call.receive<LegacyBrevService.OrderLetterRequest>()
            call.respond(legacyBrevService.bestillBrev(call, request))
        }
        get("/navn") {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            respondWithResult(pdlService.hentNavn(call, sak.foedselsnr))
        }

        get("/adresse") {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(call, sak.foedselsnr))
        }

        get("/foretrukketSpraak") {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            call.respond(krrService.getPreferredLocale(call, sak.foedselsnr))
        }
    }
}

private data class BestillBrevRequest(
    val brevkode: String,
    val spraak: SpraakKode,
    val landkode: String? = null,
    val mottakerText: String? = null,
    val isSensitive: Boolean?,
    val vedtaksId: Long? = null,
    val idTSSEkstern: String? = null,
)