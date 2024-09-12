package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*

fun Route.sakRoute(
    penService: PenService,
    legacyBrevService: LegacyBrevService,
    pdlService: PdlService,
    pensjonPersonDataService: PensjonPersonDataService,
    krrService: KrrService,
    brevmalService: BrevmalService,
    brevredigeringService: BrevredigeringService,
    safService: SafService,
) {
    route("/sak/{saksId}") {
        install(AuthorizeAnsattSakTilgang(pdlService, penService))

        get {
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            val vedtaksId: String? = call.request.queryParameters["vedtaksId"]
            val hasAccessToEblanketter = principal().isInGroup(ADGroups.pensjonUtland)
            val brevmetadata = if (vedtaksId != null) {
                brevmalService.hentBrevmalerForVedtak(
                    call = call,
                    sakType = sak.sakType,
                    includeEblanketter = hasAccessToEblanketter,
                    vedtaksId = vedtaksId
                )
            } else {
                brevmalService.hentBrevmalerForSak(call, sak.sakType, hasAccessToEblanketter)
            }
            call.respond(Api.SakContext(sak, brevmetadata))
        }
        route("/bestillBrev") {
            post<Api.BestillDoksysBrevRequest>("/doksys") { request ->
                val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

                call.respond(legacyBrevService.bestillOgRedigerDoksysBrev(call, request, sak.saksId))
            }

            route("/exstream") {
                post<Api.BestillExstreamBrevRequest> { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

                    call.respond(
                        legacyBrevService.bestillOgRedigerExstreamBrev(
                            call = call,
                            gjelderPid = sak.foedselsnr,
                            request = request,
                            saksId = sak.saksId,
                        )
                    )
                }

                post<Api.BestillEblankettRequest>("/eblankett") { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

                    call.respond(
                        legacyBrevService.bestillOgRedigerEblankett(
                            call = call,
                            gjelderPid = sak.foedselsnr,
                            request = request,
                            saksId = sak.saksId,
                        )
                    )
                }
            }
        }
        get("/navn") {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            respondWithResult(pdlService.hentNavn(call, sak.foedselsnr, sak.sakType.behandlingsnummer))
        }

        get("/adresse") {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            respondWithResult(pensjonPersonDataService.hentKontaktadresse(call, sak.foedselsnr))
        }

        get("/foretrukketSpraak") {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            call.respond(krrService.getPreferredLocale(call, sak.foedselsnr))
        }

        get("/pdf/{journalpostId}") {
            val journalpostId = call.parameters.getOrFail("journalpostId")
            safService.hentPdfForJournalpostId(call, journalpostId).onOk {
                call.respondBytes(it, ContentType.Application.Pdf, HttpStatusCode.OK)
            }.onError { message, _ ->
                call.respond(HttpStatusCode.InternalServerError, message)
            }
        }

        sakBrev(brevredigeringService)
    }
}