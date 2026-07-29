package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgangForBrev
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.DiffBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.FrigiReservasjonHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevInfoHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.ReserverBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.TilbakestillBrevHandler
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService

context(app: Application)
fun Route.brev() {
    val dto2ApiService: Dto2ApiService by app.dependencies

    route("/brev/{brevId}") {
        val hentBrevInfo: HentBrevInfoHandler by app.dependencies
        install(AuthorizeAnsattSakTilgangForBrev)

        get("/info") {
            val brevId = call.parameters.brevId()
            val hentBrevInfo = hentBrevInfo(HentBrevInfoHandler.Request(brevId))
            respondSuccess(hentBrevInfo?.asSuccess()) { respond(HttpStatusCode.OK, dto2ApiService.toApi(it)) }
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
            val reserverBrev: ReserverBrevHandler by app.dependencies
            get {
                val brevId = call.parameters.brevId()
                val reservasjon = reserverBrev(ReserverBrevHandler.Request(brevId = brevId))
                apiRespond(dto2ApiService, reservasjon)
            }

            val frigiReservasjon: FrigiReservasjonHandler by app.dependencies
            delete {
                val brevId = call.parameters.brevId()
                val result = frigiReservasjon(FrigiReservasjonHandler.Request(brevId = brevId))
                apiRespond(dto2ApiService, result)
            }
        }

        val tilbakestillBrev: TilbakestillBrevHandler by app.dependencies
        post("/tilbakestill") {
            val brevId = call.parameters.brevId()
            val resultat = tilbakestillBrev(TilbakestillBrevHandler.Request(brevId = brevId))
            apiRespond(dto2ApiService, resultat)
        }

        val diffBrev: DiffBrevHandler by app.dependencies
        post<Edit.Letter>("/diff") { request ->
            val brevId = call.parameters.brevId()
            val split = call.request.queryParameters["split"]?.toBoolean() ?: false

            val result = diffBrev(DiffBrevHandler.Request(brevId, request, split))
            respondSuccess(result?.asSuccess()) { respond(HttpStatusCode.OK, it) }
        }
    }
}

fun Parameters.brevId() = BrevId(getOrFail<Long>("brevId"))

fun Parameters.vedleggId(): VedleggId = VedleggId(getOrFail("vedleggId"))