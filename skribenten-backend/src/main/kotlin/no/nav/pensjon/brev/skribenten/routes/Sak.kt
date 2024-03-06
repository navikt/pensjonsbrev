package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillBrevResponse
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.FailureType.ENHETSID_MANGLER

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
        route("/bestillBrev") {
            post<LegacyBrevService.BestillDoksysBrevRequest>("/doksys") { request ->
                val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                call.respond(sak.enhetId?.let {
                    legacyBrevService.bestillOgRedigerDoksysBrev(call, request, enhetsId = sak.enhetId, sak.sakId)
                } ?: BestillBrevResponse(ENHETSID_MANGLER))
            }
            route("/exstream") {
                post<LegacyBrevService.BestillExstreamBrevRequest> { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                    call.respond(
                        sak.enhetId?.let {
                            legacyBrevService.bestillOgRedigerExstreamBrev(
                                call = call,
                                enhetsId = sak.enhetId,
                                gjelderPid = sak.foedselsnr,
                                request = request,
                                sakId = sak.sakId,
                            )
                        } ?: BestillOgRedigerBrevResponse(ENHETSID_MANGLER)
                    )
                }

                post<LegacyBrevService.BestillEblankettRequest>("/eblankett") { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                    call.respond(
                        sak.enhetId?.let {
                            legacyBrevService.bestillOgRedigerEblankett(
                                call = call,
                                enhetsId = sak.enhetId,
                                gjelderPid = sak.foedselsnr,
                                request = request,
                                sakId = sak.sakId,
                            )
                        } ?: BestillOgRedigerBrevResponse(ENHETSID_MANGLER)
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