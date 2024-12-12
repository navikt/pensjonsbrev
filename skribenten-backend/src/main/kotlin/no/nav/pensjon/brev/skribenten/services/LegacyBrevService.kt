package no.nav.pensjon.brev.skribenten.services

import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.BestillOgRedigerBrevResponse.FailureType.*
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.DokumentkategoriCode.SED
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.*
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class LegacyBrevException(message: String) : Exception(message)

class LegacyBrevService(
    private val brevmetadataService: BrevmetadataService,
    private val safService: SafService,
    private val penService: PenService,
    private val navansattService: NavansattService,
) {
    private val logger = LoggerFactory.getLogger(LegacyBrevService::class.java)

    suspend fun bestillOgRedigerDoksysBrev(request: Api.BestillDoksysBrevRequest, saksId: Long): Api.BestillOgRedigerBrevResponse =
        coroutineScope {
            val brevMetadata = async { brevmetadataService.getMal(request.brevkode) }

            val result = bestillDoksysBrev(request, request.enhetsId, saksId)

            if (result.failureType == null && result.journalpostId != null && brevMetadata.await().redigerbart) {
                ventPaaJournalpostOgRedigerDoksysBrev(result.journalpostId).let {
                    Api.BestillOgRedigerBrevResponse(url = it.url, failureType = it.failureType)
                }
            } else result
        }

    suspend fun bestillOgRedigerExstreamBrev(gjelderPid: String, request: Api.BestillExstreamBrevRequest, saksId: Long): Api.BestillOgRedigerBrevResponse {
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
                isSensitive = request.isSensitive,
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
        saksId: Long,
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
                isSensitive = request.isSensitive,
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
        enhetsId: String,
        gjelderPid: String,
        idTSSEkstern: String? = null,
        isSensitive: Boolean,
        metadata: BrevdataDto,
        saksId: Long,
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
            return Api.BestillOgRedigerBrevResponse(failureType = Api.BestillOgRedigerBrevResponse.FailureType.EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        }

        return if (!harTilgangTilEnhet(enhetsId)) {
            Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
        } else penService.bestillExstreamBrev(
            Pen.BestillExstreamBrevRequest(
                brevKode = brevkode,
                brevGruppe = metadata.brevgruppe,
                redigerbart = metadata.redigerbart,
                sprakKode = spraak.toString(),
                brevMottakerNavn = mottakerText?.takeIf { isEblankett },    // custom felt kun for sed/eblankett,
                sakskontekst = Pen.BestillExstreamBrevRequest.Sakskontekst(
                    journalenhet = enhetsId,                                // Nav org enhet nr som skriver brevet. Kommer med i signatur.
                    gjelder = gjelderPid,                                   // Hvem gjelder brevet? Kan være ulik fra mottaker om det er verge.
                    dokumentdato = LocalDateTime.now(),
                    dokumenttype = metadata.dokType.toString(),
                    fagsystem = "PEN",
                    fagomradeKode = "PEN",                                  // Fagområde pensjon uansett hva det faktisk er. Finnes det UFO?
                    innhold = brevtittel,                                   // Visningsnavn
                    kategori = if (isEblankett) SED.toString() else metadata.dokumentkategori.toString(),
                    saksid = saksId.toString(),
                    saksbehandlernavn = saksbehandler.fornavn + " " + saksbehandler.etternavn,
                    saksbehandlerid = PrincipalInContext.require().navIdent.id,
                    kravtype = null, // TODO sett. Brukes dette for notater i det hele tatt?
                    land = landkode.takeIf { isEblankett },
                    mottaker = if (isEblankett || isNotat) null else idTSSEkstern ?: gjelderPid,

                    sensitivt = isSensitive
                ),
                vedtaksInformasjon = vedtaksId?.toString()
            )
        ).map {
            Api.BestillOgRedigerBrevResponse(journalpostId = it.journalpostId)
        }.catch { message, statusCode ->
            logger.error("Feil ved bestilling av brev fra exstream mot PEN: $message - status: $statusCode")
            Api.BestillOgRedigerBrevResponse(failureType = Api.BestillOgRedigerBrevResponse.FailureType.EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        }
    }

    private suspend fun redigerExstreamBrev(journalpostId: String): Api.BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(journalpostId)) {
            ERROR -> Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
            NOT_READY -> Api.BestillOgRedigerBrevResponse(failureType = FERDIGSTILLING_TIMEOUT)
            READY -> {
                penService.redigerExstreamBrev(journalpostId)
                    .map {
                        Api.BestillOgRedigerBrevResponse(url = it.uri)
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved bestilling av redigering av exstream brev mot PEN: $message Status: $httpStatusCode")
                        Api.BestillOgRedigerBrevResponse(failureType = EXSTREAM_REDIGERING_GENERELL)
                    }
            }
        }

    private suspend fun bestillDoksysBrev(request: Api.BestillDoksysBrevRequest, enhetsId: String, saksId: Long): Api.BestillOgRedigerBrevResponse =
        if (!harTilgangTilEnhet(enhetsId)) {
            Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
        } else penService.bestillDoksysBrev(request, enhetsId, saksId)
            .map { response ->
                if (response.failure != null || response.journalpostId != null) {
                    Api.BestillOgRedigerBrevResponse(journalpostId = response.journalpostId, failureType = response.failure?.toApi())
                } else {
                    logger.error("Tom response fra doksys bestilling")
                    Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                }
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved bestilling av doksys brev $message status: $httpStatusCode")
                Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
            }

    private suspend fun ventPaaJournalpostOgRedigerDoksysBrev(journalpostId: String): Api.BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(journalpostId)) {
            ERROR -> Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
            NOT_READY -> Api.BestillOgRedigerBrevResponse(failureType = FERDIGSTILLING_TIMEOUT)
            READY -> {
                safService.getFirstDocumentInJournal(journalpostId)
                    .map { safResponse ->
                        if (safResponse.errors != null) {
                            logger.error("Feil fra saf ved henting av dokument med journalpostId $journalpostId ${safResponse.errors}")
                            Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                        } else if (safResponse.data != null) {
                            val dokumentId = safResponse.data.journalpost.dokumenter.firstOrNull()?.dokumentInfoId

                            if (dokumentId != null) {
                                redigerDoksysBrev(journalpostId, dokumentId)
                            } else {
                                logger.error("Fant ingen dokumenter for redigering ved henting av journalpostId $journalpostId")
                                Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
                            }
                        } else {
                            logger.error("Tom response fra saf ved henting av dokument")
                            Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                        }
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved henting av dokumentId fra SAF ved redigering av doksys brev $message status: $httpStatusCode")
                        Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                    }
            }
        }

    private suspend fun redigerDoksysBrev(journalpostId: String, dokumentId: String): Api.BestillOgRedigerBrevResponse =
        penService.redigerDoksysBrev(journalpostId, dokumentId)
            .map {
                Api.BestillOgRedigerBrevResponse(url = it.uri)
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved redigering av doksys brev $message status: $httpStatusCode")
                Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
            }

    private suspend fun harTilgangTilEnhet(enhetsId: String): Boolean =
        navansattService.harTilgangTilEnhet(PrincipalInContext.require().navIdent.id, enhetsId)
            .catch { message, httpStatusCode -> throw LegacyBrevException("Kunne ikke hente NavEnheter - $httpStatusCode: $message") }

    private fun Pen.BestillDoksysBrevResponse.FailureType.toApi(): Api.BestillOgRedigerBrevResponse.FailureType =
        when (this) {
            Pen.BestillDoksysBrevResponse.FailureType.ADDRESS_NOT_FOUND -> DOKSYS_BESTILLING_ADDRESS_NOT_FOUND
            Pen.BestillDoksysBrevResponse.FailureType.UNAUTHORIZED -> DOKSYS_BESTILLING_UNAUTHORIZED
            Pen.BestillDoksysBrevResponse.FailureType.PERSON_NOT_FOUND -> DOKSYS_BESTILLING_PERSON_NOT_FOUND
            Pen.BestillDoksysBrevResponse.FailureType.UNEXPECTED_DOKSYS_ERROR -> DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR
            Pen.BestillDoksysBrevResponse.FailureType.INTERNAL_SERVICE_CALL_FAILIURE -> DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE
            Pen.BestillDoksysBrevResponse.FailureType.TPS_CALL_FAILIURE -> DOKSYS_BESTILLING_TPS_CALL_FAILIURE
        }

}