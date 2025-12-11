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
    skjermingService: SkjermingServiceHttp,
    p1Service: P1ServiceImpl,
    pensjonRepresentasjonService: PensjonRepresentasjonService,
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
                    sakstype = sak.sakType.toBrevbaker(),
                    includeEblanketter = hasAccessToEblanketter,
                    vedtaksId = vedtaksId
                )
            } else {
                brevmalService.hentBrevmalerForSak(sak.sakType.toBrevbaker(), hasAccessToEblanketter)
            }
            val erSkjermet = skjermingService.hentSkjerming(sak.foedselsnr) ?: false
            val harVerge = pensjonRepresentasjonService.harVerge(sak.foedselsnr)
            val person = pdlService.hentBrukerContext(sak.foedselsnr, sak.sakType.behandlingsnummer)
            if (person != null) {
                call.respond(
                    Api.SakContext(
                        sak = sak,
                        brevmalKoder = brevmetadata.map { it.id },
                        adressebeskyttelse = person.adressebeskyttelse,
                        doedsfall = person.doedsdato,
                        erSkjermet = erSkjermet,
                        vergemaal = harVerge
                    )
                )
            } else {
                call.respond(status = HttpStatusCode.NotFound, message = "Person ikke funnet i PDL")
            }
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

        get("/adresse") {
            val sak = call.attributes[SakKey]
            val adresse = pensjonPersonDataService.hentKontaktadresse(sak.foedselsnr)

            if (adresse != null) {
                call.respond(adresse)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/foretrukketSpraak") {
            val sak = call.attributes[SakKey]
            call.respond(krrService.getPreferredLocale(sak.foedselsnr))
        }

        get("/pdf/{journalpostId}") {
            val journalpostId = call.parameters.getOrFail("journalpostId")
            val pdf = safService.hentPdfForJournalpostId(journalpostId)
            if (pdf != null) {
                call.respondBytes(pdf, ContentType.Application.Pdf, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        sakBrev(dto2ApiService, brevredigeringService, p1Service)
    }
}