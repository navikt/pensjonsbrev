package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringFacade
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.P1ServiceImpl
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*

fun Route.sakRoute(
    brevService: BrevService,
    brevmalService: BrevmalService,
    krrService: KrrService,
    pdlService: PdlService,
    fagsakService: FagsakService,
    pensjonPersonDataService: PensjonPersonDataService,
    safService: SafService,
    skjermingService: SkjermingServiceHttp,
    p1Service: P1ServiceImpl,
    pensjonRepresentasjonService: PensjonRepresentasjonService,
    brevredigeringFacade: BrevredigeringFacade,
    dto2ApiService: Dto2ApiService,
) {
    route("/sak/{saksId}") {
        install(AuthorizeAnsattSakTilgang) {
            this.pdlService = pdlService
            this.fagsakService = fagsakService
        }

        get {
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val vedtaksId: VedtaksId? = call.request.queryParameters["vedtaksId"]?.let { VedtaksId(it.toLong()) }
            val hasAccessToEblanketter = principal().isInGroup(ADGroups.pensjonUtland)
            val brevmetadata = if (vedtaksId != null) {
                brevmalService.hentBrevmalerForVedtak(
                    sakstype = sak.sakType,
                    includeEblanketter = hasAccessToEblanketter,
                    vedtaksId = vedtaksId
                )
            } else {
                brevmalService.hentBrevmalerForSak(sak.sakType, hasAccessToEblanketter)
            }
            call.respond(
                Api.SakContext(
                    sak = sak,
                    brevmalKoder = brevmetadata.map { it.id },
                )
            )
        }

        get("/brukerstatus") {
            coroutineScope {
                val sak: Pen.SakSelection = call.attributes[SakKey]
                val erSkjermet = async { skjermingService.hentSkjerming(sak.pid) ?: false }
                val harVerge = async { pensjonRepresentasjonService.harVerge(sak.pid) ?: false }
                val person = pdlService.hentBrukerContext(sak.pid, Pen.finnBehandlingsnummer(sak.sakType))
                if (person != null) {
                    call.respond(
                        Api.BrukerStatus(
                            adressebeskyttelse = person.adressebeskyttelse,
                            doedsfall = person.doedsdato,
                            erSkjermet = erSkjermet.await(),
                            vergemaal = harVerge.await()
                        )
                    )
                } else {
                    call.respond(status = HttpStatusCode.NotFound, message = "Person ikke funnet i PDL")
                }
            }
        }

        route("/bestillBrev") {
            route("/exstream") {
                post<Api.BestillExstreamBrevRequest> { request ->
                    val sak = call.attributes[SakKey]

                    call.respond(
                        brevService.bestillOgRedigerExstreamBrev(
                            gjelderPid = sak.pid,
                            request = request,
                            saksId = sak.saksId,
                        )
                    )
                }

                post<Api.BestillEblankettRequest>("/eblankett") { request ->
                    val sak = call.attributes[SakKey]

                    call.respond(
                        brevService.bestillOgRedigerEblankett(
                            gjelderPid = sak.pid,
                            request = request,
                            saksId = sak.saksId,
                        )
                    )
                }
            }
        }

        get("/adresse") {
            val sak = call.attributes[SakKey]
            val adresse = pensjonPersonDataService.hentKontaktadresse(sak.pid)

            if (adresse != null) {
                call.respond(adresseTilDto(adresse))
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/foretrukketSpraak") {
            val sak = call.attributes[SakKey]
            call.respond(krrService.getPreferredLocale(sak.pid))
        }

        get("/pdf/{journalpostId}") {
            val journalpostId = JournalpostId(call.parameters.getOrFail<Long>("journalpostId"))
            val pdf = safService.hentPdfForJournalpostId(journalpostId)
            if (pdf != null) {
                call.respondBytes(pdf, ContentType.Application.Pdf, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        sakBrev(brevmalService, p1Service, brevredigeringFacade, dto2ApiService)
    }
}

private fun adresseTilDto(adresse: KontaktAdresseResponseDto) = KontaktAdresseDto(
    adresseString = adresse.adresseString,
    adresselinjer = adresse.adresselinjer,
    type = when (adresse.type) {
        KontaktAdresseResponseDto.Adressetype.MATRIKKELADRESSE -> KontaktAdresseDto.Adressetype.MATRIKKELADRESSE
        KontaktAdresseResponseDto.Adressetype.POSTADRESSE_I_FRITT_FORMAT -> KontaktAdresseDto.Adressetype.POSTADRESSE_I_FRITT_FORMAT
        KontaktAdresseResponseDto.Adressetype.POSTBOKSADRESSE -> KontaktAdresseDto.Adressetype.POSTBOKSADRESSE
        KontaktAdresseResponseDto.Adressetype.REGOPPSLAG_ADRESSE -> KontaktAdresseDto.Adressetype.REGOPPSLAG_ADRESSE
        KontaktAdresseResponseDto.Adressetype.UKJENT_BOSTED -> KontaktAdresseDto.Adressetype.UKJENT_BOSTED
        KontaktAdresseResponseDto.Adressetype.UTENLANDSK_ADRESSE -> KontaktAdresseDto.Adressetype.UTENLANDSK_ADRESSE
        KontaktAdresseResponseDto.Adressetype.UTENLANDSK_ADRESSE_I_FRITT_FORMAT -> KontaktAdresseDto.Adressetype.UTENLANDSK_ADRESSE_I_FRITT_FORMAT
        KontaktAdresseResponseDto.Adressetype.VEGADRESSE -> KontaktAdresseDto.Adressetype.VEGADRESSE
        KontaktAdresseResponseDto.Adressetype.VERGE_PERSON_POSTADRESSE -> KontaktAdresseDto.Adressetype.VERGE_PERSON_POSTADRESSE
        KontaktAdresseResponseDto.Adressetype.VERGE_SAMHANDLER_POSTADRESSE -> KontaktAdresseDto.Adressetype.VERGE_SAMHANDLER_POSTADRESSE
    }
)

data class KontaktAdresseDto(
    val adresseString: String,
    val adresselinjer: List<String>,
    val type: Adressetype
) {
    enum class Adressetype {
        MATRIKKELADRESSE,
        POSTADRESSE_I_FRITT_FORMAT,
        POSTBOKSADRESSE,
        REGOPPSLAG_ADRESSE,
        UKJENT_BOSTED,
        UTENLANDSK_ADRESSE,
        UTENLANDSK_ADRESSE_I_FRITT_FORMAT,
        VEGADRESSE,
        VERGE_PERSON_POSTADRESSE,
        VERGE_SAMHANDLER_POSTADRESSE,
    }
}