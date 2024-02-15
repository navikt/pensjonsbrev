package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import io.ktor.util.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInName
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExstreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.*
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.*
import org.slf4j.LoggerFactory

class LegacyBrevService(
    private val tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    private val brevmetadataService: BrevmetadataService,
    private val safService: SafService,
    private val penService: PenService,
    private val navansattService: NavansattService
) {
    private val logger = LoggerFactory.getLogger(LegacyBrevService::class.java)
    suspend fun bestillBrev(call: ApplicationCall, request: OrderLetterRequest): BestillOgRedigerBrevResponse =
        penService.hentSak(call, request.sakId.toString()).map { serviceResult ->
            val sakEnhetId = serviceResult.enhetId
            if (sakEnhetId == null) {
                return BestillOgRedigerBrevResponse(ENHETSID_MANGLER)
            } else if (!navansattService.harAnsattTilgangTilEnhet(call = call, enhetsId = sakEnhetId)) {
                return BestillOgRedigerBrevResponse(NAVANSATT_ENHETER_ERROR)
            } else {
                val brevMetadata = brevmetadataService.getMal(request.brevkode)
                return when (brevMetadata.brevsystem) {
                    BrevdataDto.BrevSystem.DOKSYS -> bestillDoksysBrev(call, request, sakEnhetId)
                    BrevdataDto.BrevSystem.GAMMEL -> bestillExstreamBrev(
                        call = call,
                        request = request,
                        navIdent = fetchLoggedInNavIdent(call),
                        metadata = brevMetadata,
                        navn = fetchLoggedInName(call),
                        enhetsId = serviceResult.enhetId
                    )
                }
            }
        }.catch { message, httpStatusCode ->
            logger.error("En feil oppstod under henting av Sak med sakId. ${request.sakId} , med message: $message , statuscode: $httpStatusCode")
            return BestillOgRedigerBrevResponse(PenService.BestillDoksysBrevResponse.FailureType.INTERNAL_SERVICE_CALL_FAILIURE)
        }

    private suspend fun bestillExstreamBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        navIdent: String,
        metadata: BrevdataDto,
        navn: String,
        enhetsId: String,
    ): BestillOgRedigerBrevResponse =
        tjenestebussIntegrasjonService.bestillExstreamBrev(call, request, navIdent, metadata, navn, enhetsId)
            .map {
                if (it.failureType != null) {
                    BestillOgRedigerBrevResponse(it.failureType)
                } else if (it.journalpostId != null) {
                    ventPaaJournalOgRedigerExstreamBrev(call, it.journalpostId)
                } else {
                    logger.error("Tom response fra tjenestebuss-integrasjon")
                    BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                }
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved bestilling av brev fra exstream mot tjenestebuss-integrasjon: $message Status:$httpStatusCode")
                BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
            }

    private suspend fun ventPaaJournalOgRedigerExstreamBrev(
        call: ApplicationCall,
        journalpostId: String,
    ): BestillOgRedigerBrevResponse {
        val status = safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)
        when (status) {
            ERROR -> return BestillOgRedigerBrevResponse(SAF_ERROR)
            NOT_READY -> return BestillOgRedigerBrevResponse(FERDIGSTILLING_TIMEOUT)
            READY -> {
                return tjenestebussIntegrasjonService.redigerExstreamBrev(call, journalpostId)
                    .map { exstreamResponse ->
                        BestillOgRedigerBrevResponse(
                            url = exstreamResponse.url,
                            failureType = exstreamResponse.failure?.let {
                                logger.error("Feil ved redigering av exstream brev $it")
                                EXSTREAM_REDIGERING_GENERELL
                            }
                        )
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved bestilling av redigering av exstream brev mot tjenestebuss integrasjon: $message Status: $httpStatusCode")
                        BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                    }

            }
        }
    }

    private suspend fun bestillDoksysBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        enhetsId: String
    ): BestillOgRedigerBrevResponse =
        penService.bestillDoksysBrev(call, request, enhetsId)
            .map { response ->
                if (response.failure != null) {
                    BestillOgRedigerBrevResponse(response.failure)
                } else if (response.journalpostId != null) {
                    ventPaaJournalOgRedigerDoksysBrev(call, response.journalpostId)
                } else {
                    logger.error("Tom response fra pesys")
                    BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                }
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved bestilling av doksys brev $message status: $httpStatusCode")
                BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
            }

    private suspend fun ventPaaJournalOgRedigerDoksysBrev(call: ApplicationCall, journalpostId: String): BestillOgRedigerBrevResponse =
        when (safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)) {
            ERROR -> BestillOgRedigerBrevResponse(SAF_ERROR)
            NOT_READY -> BestillOgRedigerBrevResponse(FERDIGSTILLING_TIMEOUT)
            READY -> {
                safService.getFirstDocumentInJournal(call, journalpostId)
                    .map { safResponse ->
                        if (safResponse.errors != null) {
                            logger.error("Feil fra saf ved henting av dokument med journalpostId $journalpostId ${safResponse.errors}")
                            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                        } else if (safResponse.data != null) {
                            val dokumentId = safResponse.data.journalpost.dokumenter.firstOrNull()?.dokumentInfoId
                            if (dokumentId != null) {
                                redigerDoksysBrev(tjenestebussIntegrasjonService, call, journalpostId, dokumentId)
                            } else {
                                logger.error("Fant ingen dokumenter for redigering ved henting av journalpostId $journalpostId")
                                BestillOgRedigerBrevResponse(SAF_ERROR)
                            }
                        } else {
                            logger.error("Tom response fra saf ved henting av dokument")
                            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                        }
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved henting av dokumentId fra SAF ved redigering av doksys brev $message status: $httpStatusCode")
                        BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                    }
            }
        }

    private suspend fun redigerDoksysBrev(
        tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
        call: ApplicationCall,
        journalpostId: String,
        dokumentId: String
    ) = tjenestebussIntegrasjonService.redigerDoksysBrev(call, journalpostId, dokumentId)
        .map {
            if (it.metaforceURI != null) {
                BestillOgRedigerBrevResponse(it.metaforceURI.replace("\"", ""))
            } else if (it.failure != null) {
                BestillOgRedigerBrevResponse(it.failure)
            } else {
                logger.error("Tom response ved redigering av doksys brev ved redigering av journal: $journalpostId")
                BestillOgRedigerBrevResponse(DOKSYS_REDIGERING_UFORVENTET)
            }
        }.catch { message, httpStatusCode ->
            logger.error("Feil ved redigering av doksys brev $message status: $httpStatusCode")
            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        }

    private fun fetchLoggedInNavIdent(call: ApplicationCall): String {
        return call.getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
    }

    private fun fetchLoggedInName(call: ApplicationCall): String {
        return call.getLoggedInName() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker i claim")
    }

    suspend fun harTilgangTilEnhet(call: ApplicationCall, enhetsId: String): Boolean {
        return navansattService.harAnsattTilgangTilEnhet(
            call = call,
            ansattId = fetchLoggedInNavIdent(call = call),
            enhetsId = enhetsId
        ).let { when (it) {
            is ServiceResult.Error -> false
            is ServiceResult.Ok -> it.result
        }}
    }

    /**
     * @param brevkode ID til brevet som bestilles
     * @param gjelderPid brukeren brevet gjelder
     * @param landkode      land som brevet skal til. Kun for e-blanketter
     * @param mottakerText  mottaker i fritekst. Kun for e-blanketter
     * @param isSensitive   om brevet inneholder sensitive opplysninger som ikke skal vises ved nivå 3 pålogging.
     * Brukes ikke i doksys brev ettersom det settes senere i prosessen.
     * @param vedtaksId     vedtakId dersom brevet er ett vedtaksbrev. Brukes for å hente opplysninger om vedtaket for vedtaksbrev.
     * @param idTSSEkstern  overstyring av mottaker til samhandler med TSS id
     * */
    data class OrderLetterRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val sakId: Long,
        val gjelderPid: String,
        val landkode: String? = null,
        val mottakerText: String? = null,
        val isSensitive: Boolean?,
        val vedtaksId: Long? = null,
        val idTSSEkstern: String? = null,
        )

    data class BestillOgRedigerBrevResponse(
        val url: String?,
        val failureType: FailureType?,
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
            NAVANSATT_ENHETER_ERROR
        }
    }
}