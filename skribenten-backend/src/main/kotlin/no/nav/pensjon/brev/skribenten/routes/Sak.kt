package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*

fun Route.sakRoute(
    dto2ApiService: Dto2ApiService,
    brevmalService: BrevmalService,
    brevredigeringService: BrevredigeringService,
    krrService: KrrService,
    legacyBrevService: LegacyBrevService,
    pdlService: PdlService,
    penService: PenService,
    pensjonPersonDataService: PensjonPersonDataService,
    safService: SafService,
) {
    route("/sak/{saksId}") {
        install(AuthorizeAnsattSakTilgang) {
            this.pdlService = pdlService
            this.penService = penService
        }

        get {
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val vedtaksId: String? = call.request.queryParameters["vedtaksId"]
            val hasAccessToEblanketter = principal().isInGroup(ADGroups.pensjonUtland)
            val brevmetadata = if (vedtaksId != null) {
                brevmalService.hentBrevmalerForVedtak(
                    sakType = sak.sakType,
                    includeEblanketter = hasAccessToEblanketter,
                    vedtaksId = vedtaksId
                )
            } else {
                brevmalService.hentBrevmalerForSak(sak.sakType, hasAccessToEblanketter)
            }
            call.respond(Api.SakContext(sak, brevmetadata))
        }
        route("/bestillBrev") {
            post<Api.BestillDoksysBrevRequest>("/doksys") { request ->
                val sak = call.attributes[SakKey]

                call.respond(legacyBrevService.bestillOgRedigerDoksysBrev(request, sak.saksId))
            }

            route("/exstream") {
                post<Api.BestillExstreamBrevRequest> { request ->
                    val sak = call.attributes[SakKey]

                    call.respond(
                        legacyBrevService.bestillOgRedigerExstreamBrev(
                            gjelderPid = sak.foedselsnr,
                            request = request,
                            saksId = sak.saksId,
                        )
                    )
                }

                post<Api.BestillEblankettRequest>("/eblankett") { request ->
                    val sak = call.attributes[SakKey]

                    call.respond(
                        legacyBrevService.bestillOgRedigerEblankett(
                            gjelderPid = sak.foedselsnr,
                            request = request,
                            saksId = sak.saksId,
                        )
                    )
                }
            }
        }
        get("/navn") {
            val sak = call.attributes[SakKey]
            respondWithResult(pdlService.hentNavn(sak.foedselsnr, sak.sakType.behandlingsnummer))
        }

        get("/adresse") {
            val sak = call.attributes[SakKey]
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(sak.foedselsnr))
        }

        get("/foretrukketSpraak") {
            val sak = call.attributes[SakKey]
            call.respond(krrService.getPreferredLocale(sak.foedselsnr))
        }

        get("/pdf/{journalpostId}") {
            val journalpostId = call.parameters.getOrFail("journalpostId")
            safService.hentPdfForJournalpostId(journalpostId).onOk {
                call.respondBytes(it, ContentType.Application.Pdf, HttpStatusCode.OK)
            }.onError { message, _ ->
                call.respond(HttpStatusCode.InternalServerError, message)
            }
        }

        sakBrev(dto2ApiService, brevredigeringService)
    }
}