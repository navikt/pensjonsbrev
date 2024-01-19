package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInName
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.routes.BestillOgRedigerBrevResponse.FailureType.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExtreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExtreamBrevResponseDto.Failure.FailureType
import no.nav.pensjon.brev.skribenten.services.*
import org.slf4j.LoggerFactory
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

private val logger = LoggerFactory.getLogger("bestillBrevRoute")
fun Route.bestillBrevRoute(
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    brevmetadataService: BrevmetadataService,
    safService: SafService,
) {

    post("/bestillbrev") {
        val request = call.receive<OrderLetterRequest>()
        val navIdent =
            getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
        val navn = getLoggedInName() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker i claim")
        val metadata: BrevdataDto = brevmetadataService.getMal(request.brevkode)
        //TODO bestill doksys / extream brev avhengig av system i metadata
        //TODO logg error message med correlation id istedenfor å sende den til front-end og bruk call-id.
        // Front end bør kun bruke correlation id + type.
        val result = bestillExtreamBrev(
            tjenestebussIntegrasjonService = tjenestebussIntegrasjonService,
            call = call,
            request = request,
            navIdent = navIdent,
            metadata = metadata,
            navn = navn,
            safService = safService
        )
        call.respond(
            when (result.failureType) {
                DOKSYS_UNDER_REDIGERING -> HttpStatusCode.Conflict
                DOKSYS_IKKE_REDIGERBART -> HttpStatusCode.Forbidden
                DOKSYS_IKKE_FUNNET -> HttpStatusCode.NotFound
                DOKSYS_IKKE_TILGANG -> HttpStatusCode.Unauthorized
                DOKSYS_LUKKET -> HttpStatusCode.Locked

                FERDIGSTILLING_TIMEOUT,
                SAF_ERROR,
                EXTREAM_REDIGERING_GENERELL,
                TJENESTEBUSS_INTEGRASJON,
                SKRIBENTEN_TOKEN_UTVEKSLING -> HttpStatusCode.InternalServerError

                DOKSYS_VALIDERING_FEILET,
                EXTREAM_BESTILLING_ADRESSE_MANGLER,
                EXTREAM_BESTILLING_HENTE_BREVDATA,
                EXTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT,
                EXTREAM_BESTILLING_OPPRETTE_JOURNALPOST -> HttpStatusCode.BadRequest

                null -> HttpStatusCode.OK
            }, result
        )
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
): BestillOgRedigerBrevResponse {
    val bestillingsResult: ServiceResult<BestillExtreamBrevResponseDto.Success, BestillExtreamBrevResponseDto.Failure> =
        tjenestebussIntegrasjonService.bestillExtreamBrev(call, request, navIdent, metadata, navn)

    when (bestillingsResult) {
        is ServiceResult.Ok -> {
            val error = safService.waitForJournalpostStatusUnderArbeid(call, bestillingsResult.result.journalpostId)

            return if (error != null) {
                BestillOgRedigerBrevResponse(
                    when (error.type) {
                        SafService.JournalpostLoadingError.ErrorType.ERROR -> SAF_ERROR
                        SafService.JournalpostLoadingError.ErrorType.TIMEOUT -> FERDIGSTILLING_TIMEOUT
                    },
                )
            } else {
                return tjenestebussIntegrasjonService.redigerExtreamBrev(call, bestillingsResult.result.journalpostId)
            }
        }

        is ServiceResult.Error -> {
            val failureType = bestillingsResult.error.failureType
            logger.error("Feil ved bestilling av extream brev: $failureType")
            return BestillOgRedigerBrevResponse(
                when (failureType) {
                    FailureType.ADRESSE_MANGLER -> EXTREAM_BESTILLING_ADRESSE_MANGLER
                    FailureType.HENTE_BREVDATA -> EXTREAM_BESTILLING_HENTE_BREVDATA
                    FailureType.MANGLER_OBLIGATORISK_INPUT -> EXTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT
                    FailureType.OPPRETTE_JOURNALPOST -> EXTREAM_BESTILLING_OPPRETTE_JOURNALPOST
                }
            )
        }

        is ServiceResult.AuthorizationError -> {
            logger.error(bestillingsResult.error.logString())
            return BestillOgRedigerBrevResponse(SKRIBENTEN_TOKEN_UTVEKSLING)
        }
    }
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
)

data class BestillOgRedigerBrevResponse(
    val url: String?,
    val failureType: FailureType?,
) {
    constructor(failure: FailureType) : this(url = null, failureType = failure)

    enum class FailureType {
        DOKSYS_UNDER_REDIGERING,
        DOKSYS_IKKE_REDIGERBART,
        DOKSYS_VALIDERING_FEILET,
        DOKSYS_IKKE_FUNNET,
        DOKSYS_IKKE_TILGANG,
        DOKSYS_LUKKET,
        FERDIGSTILLING_TIMEOUT,
        SAF_ERROR,
        SKRIBENTEN_TOKEN_UTVEKSLING,
        EXTREAM_REDIGERING_GENERELL,
        TJENESTEBUSS_INTEGRASJON,
        EXTREAM_BESTILLING_ADRESSE_MANGLER,
        EXTREAM_BESTILLING_HENTE_BREVDATA,
        EXTREAM_BESTILLING_MANGLER_OBLIGATORISK_INPUT,
        EXTREAM_BESTILLING_OPPRETTE_JOURNALPOST,
    }
}