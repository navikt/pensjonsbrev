package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgangForBrev
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringFacade
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.DiffBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.FrigiReservasjonHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.ReserverBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.TilbakestillBrevHandler
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.PdlService

context(app: Application)
fun Route.brev() {
    val pdlService: PdlService by app.dependencies
    val fagsakService: FagsakService by app.dependencies
    val brevredigeringFacade: BrevredigeringFacade by app.dependencies
    val dto2ApiService: Dto2ApiService by app.dependencies

    route("/brev/{brevId}") {
        install(AuthorizeAnsattSakTilgangForBrev) {
            this.pdlService = pdlService
            this.fagsakService = fagsakService
            this.hentBrevService = brevredigeringFacade
        }

        get("/info") {
            val brevId = call.parameters.brevId()
            val brev = brevredigeringFacade.hentBrevInfo(brevId)?.let { dto2ApiService.toApi(it) }

            if (brev != null) {
                call.respond(HttpStatusCode.OK, brev)
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }
        }

        val oppdaterBrev: OppdaterBrevHandler by app.dependencies
        put<Edit.Letter>("/redigertBrev") { request ->
            val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()
            val resultat = oppdaterBrev(
                OppdaterBrevHandler.Request(
                    brevId = call.parameters.brevId(),
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = request,
                    frigiReservasjon = frigiReservasjon,
                )
            )
            apiRespond(dto2ApiService, resultat)
        }

        route("/reservasjon") {
            get {
                val brevId = call.parameters.brevId()
                val reservasjon = brevredigeringFacade.reserverBrev(ReserverBrevHandler.Request(brevId = brevId))
                apiRespond(dto2ApiService, reservasjon)
            }

            delete {
                val brevId = call.parameters.brevId()
                val result = brevredigeringFacade.frigiReservasjon(FrigiReservasjonHandler.Request(brevId = brevId))
                apiRespond(dto2ApiService, result)
            }
        }

        post("/tilbakestill") {
            val brevId = call.parameters.brevId()
            val resultat = brevredigeringFacade.tilbakestillBrev(TilbakestillBrevHandler.Request(brevId = brevId))
            apiRespond(dto2ApiService, resultat)
        }

        post<Edit.Letter>("/diff") { request ->
            val brevId = call.parameters.brevId()
            val split = call.request.queryParameters["split"]?.toBoolean() ?: false

            val result = brevredigeringFacade.diffBrev(DiffBrevHandler.Request(brevId, request, split))
            respondOutcome(dto2ApiService, result) {
                respond(HttpStatusCode.OK, it)
            }
        }
    }
}

fun Parameters.brevId() = BrevId(getOrFail<Long>("brevId"))

fun Parameters.vedleggId(): VedleggId = VedleggId(getOrFail("vedleggId"))