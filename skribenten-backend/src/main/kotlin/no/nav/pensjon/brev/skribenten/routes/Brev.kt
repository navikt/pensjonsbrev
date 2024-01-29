package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInName
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.routes.BestillOgRedigerBrevResponse.FailureType.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExtreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.RedigerDoksysDokumentResponseDto
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.services.JournalpostLoadingResult.*
import org.slf4j.LoggerFactory
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

private val logger = LoggerFactory.getLogger("bestillBrevRoute")
fun Route.bestillBrevRoute(
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    brevmetadataService: BrevmetadataService,
    safService: SafService,
    penService: PenService,
) {

    post("/bestillbrev") {
        val request = call.receive<OrderLetterRequest>()
        val navIdent = getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
        val navn = getLoggedInName() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker i claim")
        val metadata: BrevdataDto = brevmetadataService.getMal(request.brevkode)


        val result =
            when (metadata.brevsystem) {
                BrevdataDto.BrevSystem.DOKSYS -> bestillDoksysBrev(call, penService, request, safService, tjenestebussIntegrasjonService)
                BrevdataDto.BrevSystem.GAMMEL -> bestillExtreamBrev(
                    tjenestebussIntegrasjonService = tjenestebussIntegrasjonService,
                    call = call,
                    request = request,
                    navIdent = navIdent,
                    metadata = metadata,
                    navn = navn,
                    safService = safService
                )
            }
        //TODO logg error message med correlation id istedenfor å sende den til front-end og bruk call-id.
        call.respond(result)
    }
}

private suspend fun bestillExtreamBrev(
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    call: ApplicationCall,
    request: OrderLetterRequest,
    navIdent: String,
    metadata: BrevdataDto,
    navn: String,
    safService: SafService
): BestillOgRedigerBrevResponse =
    tjenestebussIntegrasjonService.bestillExtreamBrev(call, request, navIdent, metadata, navn)
        .map {
            if (it.failureType != null) {
                BestillOgRedigerBrevResponse(it.failureType)
            } else if (it.journalpostId != null) {
                ventPaaJournalOgRedigerExtreamBrev(safService, call, it.journalpostId, tjenestebussIntegrasjonService)
            } else {
                logger.error("Tom response fra tjenetebuss-integrasjon")
                BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
            }
        }.catch { message, httpStatusCode ->
            logger.error("Feil ved bestilling av brev fra extream mot tjenestebuss-integrasjon: $message Status:$httpStatusCode")
            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        }

private suspend fun ventPaaJournalOgRedigerExtreamBrev(
    safService: SafService,
    call: ApplicationCall,
    journalpostId: String,
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService
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
    penService: PenService,
    request: OrderLetterRequest,
    safService: SafService,
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
): BestillOgRedigerBrevResponse =
    penService.bestillDoksysBrev(call, request)
        .map { response ->
            if (response.failure != null) {
                BestillOgRedigerBrevResponse(response.failure)
            } else if (response.journalpostId != null) {
                ventPaaJournalOgRedigerDoksysBrev(safService, call, tjenestebussIntegrasjonService, response.journalpostId)
            } else {
                logger.error("Tom response fra pesys")
                BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
            }
        }.catch { message, httpStatusCode ->
            logger.error("Feil ved bestilling av doksys brev $message status: $httpStatusCode")
            BestillOgRedigerBrevResponse(SKRIBENTEN_INTERNAL_ERROR)
        }

private suspend fun ventPaaJournalOgRedigerDoksysBrev(
    safService: SafService,
    call: ApplicationCall,
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    journalpostId: String
): BestillOgRedigerBrevResponse =
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

fun getCurrentGregorianTime(): XMLGregorianCalendar {
    val cal = GregorianCalendar()
    cal.time = Date()
    return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
}


data class OrderLetterRequest(
    val brevkode: String,
    val spraak: SpraakKode,
    val sakId: Long,
    val gjelderPid: String,
    val landkode: String? = null,
    val mottakerText: String? = null,
    val enhetsId: String,
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
    }
}