package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
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
        route("/bestillBrev") {
            post<LegacyBrevService.BestillDoksysBrevRequest>("/doksys") { request ->
                val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                call.respond(sak.enhetId.let {
                    legacyBrevService.bestillOgRedigerDoksysBrev(call, request, enhetsId = it, sak.sakId)
                })
            }
            route("/exstream") {
                post<LegacyBrevService.BestillExstreamBrevRequest> { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                    call.respond(
                        sak.enhetId.let {
                            legacyBrevService.bestillOgRedigerExstreamBrev(
                                call = call,
                                enhetsId = it,
                                gjelderPid = sak.foedselsnr,
                                request = request,
                                sakId = sak.sakId,
                            )
                        }
                    )
                }

                post<LegacyBrevService.BestillEblankettRequest>("/eblankett") { request ->
                    val sak = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
                    call.respond(
                        sak.enhetId.let {
                            legacyBrevService.bestillOgRedigerEblankett(
                                call = call,
                                enhetsId = it,
                                gjelderPid = sak.foedselsnr,
                                request = request,
                                sakId = sak.sakId,
                            )
                        }
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