package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExstreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.*
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.FailureType.*
import org.slf4j.LoggerFactory

class LegacyBrevService(
    private val tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    private val brevmetadataService: BrevmetadataService,
    private val safService: SafService,
    private val penService: PenService,
) {
    private val logger = LoggerFactory.getLogger(LegacyBrevService::class.java)

    suspend fun bestillOgRedigerDoksysBrev(
        call: ApplicationCall,
        request: BestillDoksysBrevRequest,
        enhetsId: String,
        sakId: Long,
    ): BestillOgRedigerBrevResponse {
        val brevMetadata = brevmetadataService.getMal(request.brevkode)
        val result = bestillDoksysBrev(call, request, enhetsId, sakId)
        return if (result.failureType != null) {
            BestillOgRedigerBrevResponse(result)
        } else if (result.journalpostId == null) {
            logger.error("Tom response fra doksys bestilling")
            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        } else {
            if (brevMetadata.redigerbart) {
                BestillOgRedigerBrevResponse(ventPaaJournalpostOgRedigerDoksysBrev(call, result.journalpostId))
            } else {
                BestillOgRedigerBrevResponse(result)
            }
        }
    }

    suspend fun bestillOgRedigerExstreamBrev(
        call: ApplicationCall,
        enhetsId: String,
        gjelderPid: String,
        request: BestillExstreamBrevRequest,
        sakId: Long,
    ): BestillOgRedigerBrevResponse {
        val brevMetadata = brevmetadataService.getMal(request.brevkode)
        val result = bestillExstreamBrev(
            brevkode = request.brevkode,
            call = call,
            enhetsId = enhetsId,
            gjelderPid = gjelderPid,
            idTSSEkstern = request.idTSSEkstern,
            isSensitive = request.isSensitive,
            metadata = brevMetadata,
            sakId = sakId,
            spraak = request.spraak,
            vedtaksId = request.vedtaksId,
        )
        return if (result.failureType != null) {
            BestillOgRedigerBrevResponse(result)
        } else if (result.journalpostId == null) {
            logger.error("Tom response fra exstream bestilling")
            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        } else {
            if (brevMetadata.redigerbart) {
                BestillOgRedigerBrevResponse(redigerExstreamBrev(call, result.journalpostId))
            } else {
                BestillOgRedigerBrevResponse(result)
            }
        }
    }

    suspend fun bestillOgRedigerEblankett(
        call: ApplicationCall,
        enhetsId: String,
        gjelderPid: String,
        request: BestillEblankettRequest,
        sakId: Long,
    ): BestillOgRedigerBrevResponse {
        val brevMetadata = brevmetadataService.getMal(request.brevkode)
        val result = bestillExstreamBrev(
            brevkode = request.brevkode,
            call = call,
            enhetsId = enhetsId,
            gjelderPid = gjelderPid,
            isSensitive = request.isSensitive,
            metadata = brevMetadata,
            sakId = sakId,
            spraak = SpraakKode.NB,
        )
        return if (result.failureType != null) {
            BestillOgRedigerBrevResponse(result)
        } else if (result.journalpostId == null) {
            logger.error("Tom response fra exstream bestilling")
            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        } else {
            BestillOgRedigerBrevResponse(redigerExstreamBrev(call, result.journalpostId))
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
        sakId: Long,
        spraak: SpraakKode,
        vedtaksId: Long? = null,
    ): BestillBrevResponse =
        tjenestebussIntegrasjonService.bestillExstreamBrev(
            brevkode = brevkode,
            call = call,
            enhetsId = enhetsId,
            gjelderPid = gjelderPid,
            idTSSEkstern = idTSSEkstern,
            isSensitive = isSensitive,
            metadata = metadata,
            name = call.principal().fullName,
            navIdent = call.principal().navIdent,
            sakId = sakId,
            spraak = spraak,
            vedtaksId = vedtaksId,
        ).map {
            if (it.failureType != null) {
                BestillBrevResponse(it.failureType)
            } else if (it.journalpostId != null) {
                BestillBrevResponse(it.journalpostId)
            } else {
                logger.error("Tom response fra tjenestebuss-integrasjon")
                BestillBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
            }
        }.catch { message, httpStatusCode ->
            logger.error("Feil ved bestilling av brev fra exstream mot tjenestebuss-integrasjon: $message Status:$httpStatusCode")
            BestillBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        }

    private suspend fun redigerExstreamBrev(
        call: ApplicationCall,
        journalpostId: String,
    ): RedigerBrevResponse {
        val status = safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)
        when (status) {
            ERROR -> return RedigerBrevResponse(SAF_ERROR)
            NOT_READY -> return RedigerBrevResponse(FERDIGSTILLING_TIMEOUT)
            READY -> {
                return tjenestebussIntegrasjonService.redigerExstreamBrev(call, journalpostId)
                    .map { exstreamResponse ->
                        RedigerBrevResponse(
                            url = exstreamResponse.url,
                            failureType = exstreamResponse.failure?.let {
                                logger.error("Feil ved redigering av exstream brev $it")
                                EXSTREAM_REDIGERING_GENERELL
                            }
                        )
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved bestilling av redigering av exstream brev mot tjenestebuss integrasjon: $message Status: $httpStatusCode")
                        RedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                    }
            }
        }
    }

    private suspend fun bestillDoksysBrev(
        call: ApplicationCall,
        request: BestillDoksysBrevRequest,
        enhetsId: String,
        sakId: Long
    ): BestillBrevResponse =
        penService.bestillDoksysBrev(call, request, enhetsId, sakId)
            .map { response ->
                if (response.failure != null) {
                    BestillBrevResponse(response.failure)
                } else if (response.journalpostId != null) {
                    BestillBrevResponse(response.journalpostId)
                } else {
                    logger.error("Tom response fra pesys")
                    BestillBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                }
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved bestilling av doksys brev $message status: $httpStatusCode")
                BestillBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
            }

    private suspend fun ventPaaJournalpostOgRedigerDoksysBrev(call: ApplicationCall, journalpostId: String): RedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)) {
            ERROR -> RedigerBrevResponse(SAF_ERROR)
            NOT_READY -> RedigerBrevResponse(FERDIGSTILLING_TIMEOUT)
            READY -> {
                safService.getFirstDocumentInJournal(call, journalpostId)
                    .map { safResponse ->
                        if (safResponse.errors != null) {
                            logger.error("Feil fra saf ved henting av dokument med journalpostId $journalpostId ${safResponse.errors}")
                            RedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                        } else if (safResponse.data != null) {
                            val dokumentId = safResponse.data.journalpost.dokumenter.firstOrNull()?.dokumentInfoId
                            if (dokumentId != null) {
                                redigerDoksysBrev(call, journalpostId, dokumentId)
                            } else {
                                logger.error("Fant ingen dokumenter for redigering ved henting av journalpostId $journalpostId")
                                RedigerBrevResponse(SAF_ERROR)
                            }
                        } else {
                            logger.error("Tom response fra saf ved henting av dokument")
                            RedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                        }
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved henting av dokumentId fra SAF ved redigering av doksys brev $message status: $httpStatusCode")
                        RedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                    }
            }
        }

    private suspend fun redigerDoksysBrev(
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String,
    ) = tjenestebussIntegrasjonService.redigerDoksysBrev(call, journalpostId, dokumentId)
        .map {
            if (it.metaforceURI != null) {
                RedigerBrevResponse(it.metaforceURI.replace("\"", ""))
            } else if (it.failure != null) {
                RedigerBrevResponse(it.failure)
            } else {
                logger.error("Tom response ved redigering av doksys brev ved redigering av journal: $journalpostId")
                RedigerBrevResponse(DOKSYS_REDIGERING_UFORVENTET)
            }
        }.catch { message, httpStatusCode ->
            logger.error("Feil ved redigering av doksys brev $message status: $httpStatusCode")
            RedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        }

    data class BestillDoksysBrevRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val vedtaksId: Long? = null,
    )

    data class BestillExstreamBrevRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val isSensitive: Boolean,
        val vedtaksId: Long? = null,
        val idTSSEkstern: String? = null,
    )

    data class BestillEblankettRequest(
        val brevkode: String,
        val landkode: String,
        val mottakerText: String,
        val isSensitive: Boolean,
    )

    data class BestillBrevResponse(
        val journalpostId: String?,
        val failureType: FailureType?
    ) {
        constructor(failure: FailureType) : this(journalpostId = null, failureType = failure)
        constructor(journalpostId: String) : this(journalpostId = journalpostId, failureType = null)

        constructor(failure: PenService.BestillDoksysBrevResponse.FailureType) : this(
            when (failure) {
                PenService.BestillDoksysBrevResponse.FailureType.ADDRESS_NOT_FOUND -> DOKSYS_BESTILLING_ADDRESS_NOT_FOUND
                PenService.BestillDoksysBrevResponse.FailureType.UNAUTHORIZED -> DOKSYS_BESTILLING_UNAUTHORIZED
                PenService.BestillDoksysBrevResponse.FailureType.PERSON_NOT_FOUND -> DOKSYS_BESTILLING_PERSON_NOT_FOUND
                PenService.BestillDoksysBrevResponse.FailureType.UNEXPECTED_DOKSYS_ERROR -> DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR
                PenService.BestillDoksysBrevResponse.FailureType.INTERNAL_SERVICE_CALL_FAILIURE -> DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE
                PenService.BestillDoksysBrevResponse.FailureType.TPS_CALL_FAILIURE -> DOKSYS_BESTILLING_TPS_CALL_FAILIURE
            }
        )

        constructor(failure: BestillExstreamBrevResponseDto.FailureType) : this(
            when (failure) {
                BestillExstreamBrevResponseDto.FailureType.ADRESSE_MANGLER -> EXSTREAM_BESTILLING_ADRESSE_MANGLER
                BestillExstreamBrevResponseDto.FailureType.HENTE_BREVDATA -> EXSTREAM_BESTILLING_HENTE_BREVDATA
                BestillExstreamBrevResponseDto.FailureType.MANGLER_OBLIGATORISK_INPUT -> EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT
                BestillExstreamBrevResponseDto.FailureType.OPPRETTE_JOURNALPOST -> EXSTREAM_BESTILLING_OPPRETTE_JOURNALPOST
            }
        )
    }

    data class RedigerBrevResponse(
        val url: String?,
        val failureType: FailureType?
    ) {
        constructor(failure: FailureType) : this(url = null, failureType = failure)
        constructor(url: String) : this(url = url, failureType = null)
        constructor(failure: RedigerDoksysDokumentResponseDto.FailureType) : this(
            when (failure) {
                RedigerDoksysDokumentResponseDto.FailureType.UNDER_REDIGERING -> DOKSYS_REDIGERING_UNDER_REDIGERING
                RedigerDoksysDokumentResponseDto.FailureType.IKKE_REDIGERBART -> DOKSYS_REDIGERING_IKKE_REDIGERBART
                RedigerDoksysDokumentResponseDto.FailureType.VALIDERING_FEILET -> DOKSYS_REDIGERING_VALIDERING_FEILET
                RedigerDoksysDokumentResponseDto.FailureType.IKKE_FUNNET -> DOKSYS_REDIGERING_IKKE_FUNNET
                RedigerDoksysDokumentResponseDto.FailureType.IKKE_TILGANG -> DOKSYS_REDIGERING_IKKE_TILGANG
                RedigerDoksysDokumentResponseDto.FailureType.LUKKET -> DOKSYS_REDIGERING_LUKKET
                RedigerDoksysDokumentResponseDto.FailureType.UFORVENTET -> DOKSYS_REDIGERING_UFORVENTET
                RedigerDoksysDokumentResponseDto.FailureType.ENHETSID_MANGLER -> ENHETSID_MANGLER
            }
        )

    }

    data class BestillOgRedigerBrevResponse(
        val url: String?,
        val journalpostId: String?,
        val failureType: FailureType?,
    ) {
        constructor(failureType: FailureType?) : this(null, null, failureType)
        constructor(bestillBrevResponse: BestillBrevResponse) : this(
            url = null,
            journalpostId = bestillBrevResponse.journalpostId,
            failureType = bestillBrevResponse.failureType
        )

        constructor(redigerBrevResponse: RedigerBrevResponse) : this(
            url = redigerBrevResponse.url,
            journalpostId = null,
            failureType = redigerBrevResponse.failureType
        )
    }

    enum class FailureType {
        DOKSYS_BESTILLING_ADDRESS_NOT_FOUND,
        DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE,
        DOKSYS_BESTILLING_PERSON_NOT_FOUND,
        DOKSYS_BESTILLING_TPS_CALL_FAILIURE,
        DOKSYS_BESTILLING_UNAUTHORIZED,
        DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR,
        DOKSYS_REDIGERING_IKKE_FUNNET,
        DOKSYS_REDIGERING_IKKE_REDIGERBART,
        DOKSYS_REDIGERING_IKKE_TILGANG,
        DOKSYS_REDIGERING_LUKKET,
        DOKSYS_REDIGERING_UFORVENTET,
        DOKSYS_REDIGERING_UNDER_REDIGERING,
        DOKSYS_REDIGERING_VALIDERING_FEILET,
        EXSTREAM_BESTILLING_ADRESSE_MANGLER,
        EXSTREAM_BESTILLING_HENTE_BREVDATA,
        EXSTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT,
        EXSTREAM_BESTILLING_OPPRETTE_JOURNALPOST,
        EXSTREAM_REDIGERING_GENERELL,
        FERDIGSTILLING_TIMEOUT,
        SAF_ERROR,
        SKRIBENTEN_INTERNAL_ERROR,
        ENHETSID_MANGLER,
    }

}