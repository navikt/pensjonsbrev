package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInName
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExtreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.ERROR
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.NOT_READY
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.READY
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_BESTILLING_ADDRESS_NOT_FOUND
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_BESTILLING_INTERNAL_SERVICE_CALL_FAILIURE
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_BESTILLING_PERSON_NOT_FOUND
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_BESTILLING_TPS_CALL_FAILIURE
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_BESTILLING_UNAUTHORIZED
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_BESTILLING_UNEXPECTED_DOKSYS_ERROR
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_REDIGERING_IKKE_FUNNET
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_REDIGERING_IKKE_REDIGERBART
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_REDIGERING_IKKE_TILGANG
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_REDIGERING_LUKKET
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_REDIGERING_UFORVENTET
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_REDIGERING_UNDER_REDIGERING
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.DOKSYS_REDIGERING_VALIDERING_FEILET
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.ENHETSID_MANGLER
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.EXTREAM_BESTILLING_ADRESSE_MANGLER
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.EXTREAM_BESTILLING_HENTE_BREVDATA
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.EXTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.EXTREAM_BESTILLING_OPPRETTE_JOURNALPOST
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.EXTREAM_REDIGERING_GENERELL
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.FERDIGSTILLING_TIMEOUT
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.NAVANSATT_ENHETER_ERROR
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.SAF_ERROR
import no.nav.pensjon.brev.skribenten.services.LegacyBrevService.BestillOgRedigerBrevResponse.FailureType.SKRIBENTEN_INTERNAL_ERROR
import org.slf4j.LoggerFactory

class LegacyBrevService(
    private val tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    private val brevmetadataService: BrevmetadataService,
    private val safService: SafService,
    private val penService: PenService,
    private val navansattService: NavansattService
) {
    private val logger = LoggerFactory.getLogger(LegacyBrevService::class.java)
    suspend fun bestillBrev(call: ApplicationCall, request: OrderLetterRequest): BestillOgRedigerBrevResponse {

        return when (val serviceResult = penService.hentSak(call, request.sakId.toString())) {
            is ServiceResult.Error -> BestillOgRedigerBrevResponse(PenService.BestillDoksysBrevResponse.FailureType.INTERNAL_SERVICE_CALL_FAILIURE)
            is ServiceResult.Ok -> {
                val sakEnhetId = serviceResult.result.enhetId
                if (sakEnhetId == null) {
                    return BestillOgRedigerBrevResponse(ENHETSID_MANGLER)
                } else if (!harTilgangTilEnhet(call = call, enhetsId = sakEnhetId)) {
                    return BestillOgRedigerBrevResponse(NAVANSATT_ENHETER_ERROR)
                } else {
                    return when (brevmetadataService.getMal(request.brevkode).brevsystem) {
                        BrevdataDto.BrevSystem.DOKSYS -> bestillDoksysBrev(call, request, sakEnhetId)
                        BrevdataDto.BrevSystem.GAMMEL -> bestillExtreamBrev(
                            call = call,
                            request = request,
                            navIdent = fetchLoggedInNavIdent(call),
                            metadata = brevmetadataService.getMal(request.brevkode),
                            navn = fetchLoggedInName(call),
                            enhetsId = serviceResult.result.enhetId
                        )
                    }
                }
            }
        }
    }

    private suspend fun bestillExtreamBrev(
        call: ApplicationCall,
        request: OrderLetterRequest,
        navIdent: String,
        metadata: BrevdataDto,
        navn: String,
        enhetsId: String,
    ): BestillOgRedigerBrevResponse =
        tjenestebussIntegrasjonService.bestillExtreamBrev(call, request, navIdent, metadata, navn, enhetsId)
            .map {
                if (it.failureType != null) {
                    BestillOgRedigerBrevResponse(it.failureType)
                } else if (it.journalpostId != null) {
                    ventPaaJournalOgRedigerExtreamBrev(call, it.journalpostId)
                } else {
                    logger.error("Tom response fra tjenestebuss-integrasjon")
                    BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
                }
            }.catch { message, httpStatusCode ->
                logger.error("Feil ved bestilling av brev fra extream mot tjenestebuss-integrasjon: $message Status:$httpStatusCode")
                BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
            }

    private suspend fun ventPaaJournalOgRedigerExtreamBrev(
        call: ApplicationCall,
        journalpostId: String,
    ): BestillOgRedigerBrevResponse {
        val status = safService.waitForJournalpostStatusUnderArbeid(call, journalpostId)
        when (status) {
            ERROR -> return BestillOgRedigerBrevResponse(SAF_ERROR)
            NOT_READY -> return BestillOgRedigerBrevResponse(FERDIGSTILLING_TIMEOUT)
            READY -> {
                return tjenestebussIntegrasjonService.redigerExtreamBrev(call, journalpostId)
                    .map { extreamResponse ->
                        BestillOgRedigerBrevResponse(
                            url = extreamResponse.url,
                            failureType = extreamResponse.failure?.let {
                                logger.error("Feil ved redigering av extream brev $it")
                                EXTREAM_REDIGERING_GENERELL
                            }
                        )
                    }.catch { message, httpStatusCode ->
                        logger.error("Feil ved bestilling av redigering av extream brev mot tjenestebuss integrasjon: $message Status: $httpStatusCode")
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

    data class OrderLetterRequest(
        val brevkode: String,
        val spraak: SpraakKode,
        val sakId: Long,
        val gjelderPid: String,
        val landkode: String? = null,
        val mottakerText: String? = null,
        val isSensitive: Boolean,
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

        constructor(failure: BestillExtreamBrevResponseDto.FailureType) : this(
            when (failure) {
                BestillExtreamBrevResponseDto.FailureType.ADRESSE_MANGLER -> EXTREAM_BESTILLING_ADRESSE_MANGLER
                BestillExtreamBrevResponseDto.FailureType.HENTE_BREVDATA -> EXTREAM_BESTILLING_HENTE_BREVDATA
                BestillExtreamBrevResponseDto.FailureType.MANGLER_OBLIGATORISK_INPUT -> EXTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT
                BestillExtreamBrevResponseDto.FailureType.OPPRETTE_JOURNALPOST -> EXTREAM_BESTILLING_OPPRETTE_JOURNALPOST
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
            EXTREAM_BESTILLING_ADRESSE_MANGLER,
            EXTREAM_BESTILLING_HENTE_BREVDATA,
            EXTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT,
            EXTREAM_BESTILLING_OPPRETTE_JOURNALPOST,
            EXTREAM_REDIGERING_GENERELL,
            FERDIGSTILLING_TIMEOUT,
            SAF_ERROR,
            SKRIBENTEN_INTERNAL_ERROR,
            ENHETSID_MANGLER,
            NAVANSATT_ENHETER_ERROR
        }
    }
}