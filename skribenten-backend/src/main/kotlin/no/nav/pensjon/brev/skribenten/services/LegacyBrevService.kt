package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Api.BestillOgRedigerBrevResponse.FailureType.*
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExstreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.*
import org.slf4j.LoggerFactory

class LegacyBrevService(
    private val tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    private val brevmetadataService: BrevmetadataService,
    private val safService: SafService,
    private val penService: PenService,
    private val navansattService: NavansattService,
) {
    private val logger = LoggerFactory.getLogger(LegacyBrevService::class.java)

    suspend fun bestillOgRedigerDoksysBrev(
        call: ApplicationCall,
        request: Api.BestillDoksysBrevRequest,
        saksId: Long,
        enhetsTilganger: List<NAVEnhet>,
    ): Api.BestillOgRedigerBrevResponse =
        coroutineScope {
            val brevMetadata = async { brevmetadataService.getMal(request.brevkode) }

            if (!harTilgangTilEnhet(request.enhetsId, enhetsTilganger)) {
                Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
            } else {
                val result = bestillDoksysBrev(call, request, request.enhetsId, saksId)

                if (result.failureType == null && result.journalpostId != null && brevMetadata.await().redigerbart) {
                    ventPaaJournalpostOgRedigerDoksysBrev(call, result.journalpostId).let {
                        Api.BestillOgRedigerBrevResponse(url = it.url, failureType = it.failureType)
                    }
                } else result
            }
        }

    private fun harTilgangTilEnhet(enhetsId: String, enhetsTilganger: List<NAVEnhet>): Boolean =
        enhetsTilganger.any { enhet -> enhet.id == enhetsId }

    suspend fun bestillOgRedigerExstreamBrev(
        call: ApplicationCall,
        gjelderPid: String,
        request: Api.BestillExstreamBrevRequest,
        saksId: Long,
        enhetsTilganger: List<NAVEnhet>,
    ): Api.BestillOgRedigerBrevResponse {
        val brevMetadata = brevmetadataService.getMal(request.brevkode)
        val brevtittel = if (brevMetadata.isRedigerbarBrevtittel()) request.brevtittel else brevMetadata.dekode
        val navansatt = navansattService.hentNavansatt(call, call.principal().navIdent).resultOrNull()

        return if (!harTilgangTilEnhet(request.enhetsId, enhetsTilganger)) {
            Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
        } else if (brevtittel.isNullOrBlank()) {
            Api.BestillOgRedigerBrevResponse(failureType = EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT)
        } else if (navansatt == null) {
            Api.BestillOgRedigerBrevResponse(failureType = NAVANSATT_MANGLER_NAVN)
        } else {
            val result = bestillExstreamBrev(
                brevkode = request.brevkode,
                call = call,
                enhetsId = request.enhetsId,
                gjelderPid = gjelderPid,
                idTSSEkstern = request.idTSSEkstern,
                isSensitive = request.isSensitive,
                metadata = brevMetadata,
                saksId = saksId,
                spraak = request.spraak,
                brevtittel = brevtittel,
                vedtaksId = request.vedtaksId,
                navn = navansatt.navn
            )

            if (result.failureType == null && result.journalpostId != null && brevMetadata.redigerbart) {
                redigerExstreamBrev(call, result.journalpostId)
            } else result
        }
    }

    suspend fun bestillOgRedigerEblankett(
        call: ApplicationCall,
        gjelderPid: String,
        request: Api.BestillEblankettRequest,
        saksId: Long,
        enhetsTilganger: List<NAVEnhet>,
    ): Api.BestillOgRedigerBrevResponse = coroutineScope {
        val brevMetadataDeffered = async { brevmetadataService.getMal(request.brevkode) }
        val navansatt = navansattService.hentNavansatt(call, call.principal().navIdent).resultOrNull()
        val brevMetadata = brevMetadataDeffered.await()

        if (!harTilgangTilEnhet(request.enhetsId, enhetsTilganger)) {
            Api.BestillOgRedigerBrevResponse(failureType = ENHET_UNAUTHORIZED)
        } else if (navansatt == null) {
            Api.BestillOgRedigerBrevResponse(failureType = NAVANSATT_MANGLER_NAVN)
        } else {
            val result = bestillExstreamBrev(
                brevkode = request.brevkode,
                call = call,
                enhetsId = request.enhetsId,
                gjelderPid = gjelderPid,
                isSensitive = request.isSensitive,
                metadata = brevMetadata,
                saksId = saksId,
                spraak = SpraakKode.NB,
                brevtittel = brevMetadata.dekode,
                landkode = request.landkode,
                mottakerText = request.mottakerText,
                navn = navansatt.navn
            )

            if (result.failureType == null && result.journalpostId != null) {
                redigerExstreamBrev(call, result.journalpostId)
            } else result
        }
    }

    private suspend fun bestillExstreamBrev(
        brevkode: String,
        call: ApplicationCall,
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
        navn: String,
    ): Api.BestillOgRedigerBrevResponse =
        tjenestebussIntegrasjonService.bestillExstreamBrev(
            brevkode = brevkode,
            call = call,
            enhetsId = enhetsId,
            gjelderPid = gjelderPid,
            idTSSEkstern = idTSSEkstern,
            isSensitive = isSensitive,
            metadata = metadata,
            name = navn,
            navIdent = call.principal().navIdent,
            saksId = saksId,
            spraak = spraak,
            vedtaksId = vedtaksId,
            landkode = landkode,
            mottakerText = mottakerText,
            brevtittel = brevtittel,
        ).map {
            if (it.failureType != null || it.journalpostId != null) {
                Api.BestillOgRedigerBrevResponse(journalpostId = it.journalpostId, failureType = it.failureType?.toApi())
            } else {
                logger.error("Tom response fra tjenestebuss-integrasjon")
                Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
            }
        }.catch { message, httpStatusCode ->
            logger.error("Feil ved bestilling av brev fra exstream mot tjenestebuss-integrasjon: $message Status:$httpStatusCode")
            Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
        }

    private suspend fun redigerExstreamBrev(
        call: ApplicationCall,
        journalpostId: String,
    ): Api.BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)) {
            ERROR -> Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
            NOT_READY -> Api.BestillOgRedigerBrevResponse(failureType = FERDIGSTILLING_TIMEOUT)
            READY -> {
                tjenestebussIntegrasjonService.redigerExstreamBrev(call, journalpostId)
                    .map { exstreamResponse ->
                        Api.BestillOgRedigerBrevResponse(
                            url = exstreamResponse.url,
                            failureType = exstreamResponse.failure?.let {
                                logger.error("Feil ved redigering av exstream brev $it")
                                EXSTREAM_REDIGERING_GENERELL
                            }
                        )
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved bestilling av redigering av exstream brev mot tjenestebuss integrasjon: $message Status: $httpStatusCode")
                        Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                    }
            }
        }

    private suspend fun bestillDoksysBrev(
        call: ApplicationCall,
        request: Api.BestillDoksysBrevRequest,
        enhetsId: String,
        saksId: Long
    ): Api.BestillOgRedigerBrevResponse =
        penService.bestillDoksysBrev(call, request, enhetsId, saksId)
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

    private suspend fun ventPaaJournalpostOgRedigerDoksysBrev(call: ApplicationCall, journalpostId: String): Api.BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)) {
            ERROR -> Api.BestillOgRedigerBrevResponse(failureType = SAF_ERROR)
            NOT_READY -> Api.BestillOgRedigerBrevResponse(failureType = FERDIGSTILLING_TIMEOUT)
            READY -> {
                safService.getFirstDocumentInJournal(call, journalpostId)
                    .map { safResponse ->
                        if (safResponse.errors != null) {
                            logger.error("Feil fra saf ved henting av dokument med journalpostId $journalpostId ${safResponse.errors}")
                            Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
                        } else if (safResponse.data != null) {
                            val dokumentId = safResponse.data.journalpost.dokumenter.firstOrNull()?.dokumentInfoId

                            if (dokumentId != null) {
                                redigerDoksysBrev(call, journalpostId, dokumentId)
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

    private suspend fun redigerDoksysBrev(
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String,
    ): Api.BestillOgRedigerBrevResponse =
        tjenestebussIntegrasjonService.redigerDoksysBrev(call, journalpostId, dokumentId)
            .map {
                if (it.metaforceURI != null) {
                    Api.BestillOgRedigerBrevResponse(url = it.metaforceURI.replace("\"", ""))
                } else if (it.failure != null) {
                    Api.BestillOgRedigerBrevResponse(failureType = it.failure.toApi())
                } else {
                    logger.error("Tom response ved redigering av doksys brev ved redigering av journal: $journalpostId")
                    Api.BestillOgRedigerBrevResponse(failureType = DOKSYS_REDIGERING_UFORVENTET)
                }
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved redigering av doksys brev $message status: $httpStatusCode")
                Api.BestillOgRedigerBrevResponse(failureType = SKRIBENTEN_INTERNAL_ERROR)
            }

    private fun Pen.BestillDoksysBrevResponse.FailureType.toApi(): Api.BestillOgRedigerBrevResponse.FailureType =
        when (this) {
            Pen.BestillDoksysBrevResponse.FailureType.ADDRESS_NOT_FOUND -> DOKSYS_BESTILLING_ADDRESS_NOT_FOUND
            Pen.BestillDoksysBrevResponse.FailureType.UNAUTHORIZED -> DOKSYS_BESTILLING_UNAUTHORIZED
            Pen.BestillDoksysBrevResponse.FailureType.PERSON_NOT_FOUND -> DOKSYS_BESTILLING_PERSON_NOT_FOUND
            Pen.BestillDoksysBrevResponse.FailureType.UNEXPECTED_DOKSYS_ERROR -> DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR
            Pen.BestillDoksysBrevResponse.FailureType.INTERNAL_SERVICE_CALL_FAILIURE -> DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE
            Pen.BestillDoksysBrevResponse.FailureType.TPS_CALL_FAILIURE -> DOKSYS_BESTILLING_TPS_CALL_FAILIURE
        }

    private fun BestillExstreamBrevResponseDto.FailureType.toApi(): Api.BestillOgRedigerBrevResponse.FailureType =
        when (this) {
            BestillExstreamBrevResponseDto.FailureType.ADRESSE_MANGLER -> EXSTREAM_BESTILLING_ADRESSE_MANGLER
            BestillExstreamBrevResponseDto.FailureType.HENTE_BREVDATA -> EXSTREAM_BESTILLING_HENTE_BREVDATA
            BestillExstreamBrevResponseDto.FailureType.MANGLER_OBLIGATORISK_INPUT -> EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT
            BestillExstreamBrevResponseDto.FailureType.OPPRETTE_JOURNALPOST -> EXSTREAM_BESTILLING_OPPRETTE_JOURNALPOST
        }

    private fun RedigerDoksysDokumentResponseDto.FailureType.toApi(): Api.BestillOgRedigerBrevResponse.FailureType =
        when (this) {
            RedigerDoksysDokumentResponseDto.FailureType.UNDER_REDIGERING -> DOKSYS_REDIGERING_UNDER_REDIGERING
            RedigerDoksysDokumentResponseDto.FailureType.IKKE_REDIGERBART -> DOKSYS_REDIGERING_IKKE_REDIGERBART
            RedigerDoksysDokumentResponseDto.FailureType.VALIDERING_FEILET -> DOKSYS_REDIGERING_VALIDERING_FEILET
            RedigerDoksysDokumentResponseDto.FailureType.IKKE_FUNNET -> DOKSYS_REDIGERING_IKKE_FUNNET
            RedigerDoksysDokumentResponseDto.FailureType.IKKE_TILGANG -> DOKSYS_REDIGERING_IKKE_TILGANG
            RedigerDoksysDokumentResponseDto.FailureType.LUKKET -> DOKSYS_REDIGERING_LUKKET
            RedigerDoksysDokumentResponseDto.FailureType.UFORVENTET -> DOKSYS_REDIGERING_UFORVENTET
            RedigerDoksysDokumentResponseDto.FailureType.ENHETSID_MANGLER -> ENHETSID_MANGLER
        }
}