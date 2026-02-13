package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.BestillOgRedigerBrevResponse.FailureType.*
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.DokumentkategoriCode.SED
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.*
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class LegacyBrevService(
    private val brevmetadataService: BrevmetadataService,
    private val safService: SafService,
    private val penService: PenService,
    private val navansattService: NavansattService,
) {
    private val logger = LoggerFactory.getLogger(LegacyBrevService::class.java)

    suspend fun bestillOgRedigerExstreamBrev(gjelderPid: String, request: Api.BestillExstreamBrevRequest, saksId: SaksId): Api.BestillOgRedigerBrevResponse {
        val brevMetadata = brevmetadataService.getMal(request.brevkode)
        val brevtittel = if (brevMetadata.isRedigerbarBrevtittel()) request.brevtittel else brevMetadata.dekode
        val navansatt = navansattService.hentNavansatt(PrincipalInContext.require().navIdent.id)

        return if (brevtittel.isNullOrBlank()) {
            Api.BestillOgRedigerBrevResponse(failureType = EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        } else if (navansatt == null) {
            Api.BestillOgRedigerBrevResponse(failureType = NAVANSATT_MANGLER_NAVN)
        } else {
            val result = bestillExstreamBrevPen(
                brevkode = request.brevkode,
                enhetsId = request.enhetsId,
                gjelderPid = gjelderPid,
                idTSSEkstern = request.idTSSEkstern,
                metadata = brevMetadata,
                saksId = saksId,
                spraak = request.spraak,
                brevtittel = brevtittel,
                vedtaksId = request.vedtaksId,
                saksbehandler = navansatt
            )

            if (result.failureType == null && result.journalpostId != null && brevMetadata.redigerbart) {
                redigerExstreamBrev(result.journalpostId)
            } else result
        }
    }

    suspend fun bestillOgRedigerEblankett(
        gjelderPid: String,
        request: Api.BestillEblankettRequest,
        saksId: SaksId,
    ): Api.BestillOgRedigerBrevResponse = coroutineScope {
        val brevMetadataDeffered = async { brevmetadataService.getMal(request.brevkode) }
        val navansatt = navansattService.hentNavansatt(PrincipalInContext.require().navIdent.id)
        val brevMetadata = brevMetadataDeffered.await()

        if (navansatt == null) {
            Api.BestillOgRedigerBrevResponse(failureType = NAVANSATT_MANGLER_NAVN)
        } else {
            val result = bestillExstreamBrevPen(
                brevkode = request.brevkode,
                enhetsId = request.enhetsId,
                gjelderPid = gjelderPid,
                metadata = brevMetadata,
                saksId = saksId,
                spraak = SpraakKode.NB,
                brevtittel = brevMetadata.dekode,
                landkode = request.landkode,
                mottakerText = request.mottakerText,
                saksbehandler = navansatt
            )

            if (result.failureType == null && result.journalpostId != null) {
                redigerExstreamBrev(result.journalpostId)
            } else result
        }
    }

    private suspend fun bestillExstreamBrevPen(
        brevkode: String,
        enhetsId: EnhetId,
        gjelderPid: String,
        idTSSEkstern: String? = null,
        metadata: BrevdataDto,
        saksId: SaksId,
        spraak: SpraakKode,
        brevtittel: String,
        vedtaksId: Long? = null,
        landkode: String? = null,
        mottakerText: String? = null,
        saksbehandler: Navansatt,
    ): Api.BestillOgRedigerBrevResponse {
        val isEblankett = metadata.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT
        val isNotat = metadata.dokType == BrevdataDto.DokumentType.N

        if (metadata.brevgruppe == null) {
            logger.warn("Fant ikke brevgruppe for exstream brev: $brevkode", HttpStatusCode.InternalServerError)
            return Api.BestillOgRedigerBrevResponse(failureType = EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        }

        return if (!harTilgangTilEnhet(enhetsId)) {
            Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
        } else {
            penService.bestillExstreamBrev(
                Pen.BestillExstreamBrevRequest(
                    brevKode = brevkode,
                    brevGruppe = metadata.brevgruppe,
                    redigerbart = metadata.redigerbart,
                    sprakKode = spraak.toString(),
                    brevMottakerNavn = mottakerText?.takeIf { isEblankett }, // custom felt kun for sed/eblankett,
                    sakskontekst = Pen.BestillExstreamBrevRequest.Sakskontekst(
                        journalenhet = enhetsId, // Nav org enhet nr som skriver brevet. Kommer med i signatur.
                        gjelder = gjelderPid, // Hvem gjelder brevet? Kan være ulik fra mottaker om det er verge.
                        dokumentdato = LocalDateTime.now(),
                        dokumenttype = metadata.dokType.toString(),
                        fagsystem = "PEN",
                        fagomradeKode = "PEN", // Fagområde pensjon uansett hva det faktisk er. Finnes det UFO?
                        innhold = brevtittel, // Visningsnavn
                        kategori = if (isEblankett) SED.toString() else metadata.dokumentkategori.toString(),
                        saksid = saksId,
                        saksbehandlernavn = saksbehandler.fornavn + " " + saksbehandler.etternavn,
                        saksbehandlerid = PrincipalInContext.require().navIdent.id,
                        kravtype = null, // TODO sett. Brukes dette for notater i det hele tatt?
                        land = landkode.takeIf { isEblankett },
                        mottaker = if (isEblankett || isNotat) null else idTSSEkstern ?: gjelderPid,
                        sensitivt = false
                    ),
                    vedtaksInformasjon = vedtaksId?.toString()
                )
            ).let { Api.BestillOgRedigerBrevResponse(journalpostId = it.journalpostId) }
        }
    }

    private suspend fun redigerExstreamBrev(journalpostId: String): Api.BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(journalpostId)) {
            ERROR -> Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
            NOT_READY -> Api.BestillOgRedigerBrevResponse(failureType = FERDIGSTILLING_TIMEOUT)
            READY -> {
                penService.redigerExstreamBrev(journalpostId)?.let {Api.BestillOgRedigerBrevResponse(url = it.uri) }
                    ?: Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
            }
        }

    private suspend fun harTilgangTilEnhet(enhetsId: EnhetId): Boolean =
        navansattService.harTilgangTilEnhet(PrincipalInContext.require().navIdent.id, enhetsId)

}