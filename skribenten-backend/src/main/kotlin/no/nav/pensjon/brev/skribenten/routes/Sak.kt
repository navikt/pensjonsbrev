package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*

fun Route.sakRoute(
    penService: PenService,
    navansattService: NavansattService,
    legacyBrevService: LegacyBrevService,
    pdlService: PdlService,
    pensjonPersonDataService: PensjonPersonDataService,
    krrService: KrrService,
    brevmalService: BrevmalService,
) {
    route("/sak/{saksId}") {
        install(AuthorizeAnsattSakTilgang(navansattService, pdlService, penService))

        // TODO skal vi beholde denne?
        get {
            val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            call.respond(sak)
        }
        get("/sakContext") {
            val sak: PenService.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            val vedtaksId: String? = call.request.queryParameters["vedtaksId"]
            val hasAccessToEblanketter = principal().isInGroup(ADGroups.pensjonUtland)
            val brevmetadata = if (vedtaksId != null) {
                brevmalService.hentBrevmaler(
                    sakType = sak.sakType,
                    call = call,
                    includeEblanketter = hasAccessToEblanketter,
                    vedtaksId = vedtaksId
                )
            } else {
                brevmalService.hentBrevmaler(sak.sakType, hasAccessToEblanketter)
            }
            call.respond(SakContext(sak, brevmetadata))
        }
        route("/bestillBrev") {
            post<LegacyBrevService.BestillDoksysBrevRequest>("/doksys") { request ->
                val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                call.respond(legacyBrevService.bestillOgRedigerDoksysBrev(call, request, enhetsId = sak.enhetId, sak.saksId))
            }
            route("/exstream") {
                post<LegacyBrevService.BestillExstreamBrevRequest> { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                    call.respond(
                        legacyBrevService.bestillOgRedigerExstreamBrev(
                            call = call,
                            enhetsId = sak.enhetId,
                            gjelderPid = sak.foedselsnr,
                            request = request,
                            saksId = sak.saksId,
                        )
                    )
                }

                post<LegacyBrevService.BestillEblankettRequest>("/eblankett") { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                    call.respond(
                        legacyBrevService.bestillOgRedigerEblankett(
                            call = call,
                            enhetsId = sak.enhetId,
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

private data class SakContext(
    val sak: PenService.SakSelection,
    val brevMetadata: List<LetterMetadata>
)