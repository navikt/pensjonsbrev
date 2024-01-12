package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.getLoggedInName
import no.nav.pensjon.brev.skribenten.getLoggedInNavIdent
import no.nav.pensjon.brev.skribenten.routes.BestillOgRedigerBrevResponse.FailureType.*
import no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto.BestillExtreamBrevResponseDto
import no.nav.pensjon.brev.skribenten.services.*
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

fun Route.bestillBrevRoute(
    penService: PenService,
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    brevmetadataService: BrevmetadataService,
    safService: SafService,
) {
    post("/bestillbrev") {
        // TODO gjennomgående call id
        val request = call.receive<OrderLetterRequest>()
        val navIdent =
            getLoggedInNavIdent() ?: throw UnauthorizedException("Fant ikke ident på innlogget bruker i claim")
        val navn = getLoggedInName() ?: throw UnauthorizedException("Fant ikke navn på innlogget bruker i claim")

        respondWithResult(
            bestillOgRedigerBrev(
                brevmetadataService,
                request,
                tjenestebussIntegrasjonService,
                navIdent,
                navn,
                call,
                safService
            )
        )
    }
}

private suspend fun bestillOgRedigerBrev(
    brevmetadataService: BrevmetadataService,
    request: OrderLetterRequest,
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    navIdent: String,
    navn: String,
    call: ApplicationCall,
    safService: SafService
): ServiceResult<BestillOgRedigerBrevResponse.Success, BestillOgRedigerBrevResponse.Failure> {
    val metadata: BrevdataDto = brevmetadataService.getMal(request.brevkode)
    //TODO bestill doksys / extream brev avhengig av system i metadata
    //TODO logg error message med correlation id istedenfor å sende den til front-end og bruk call-id.
    // Front end bør kun bruke correlation id + type.
    return bestillExtreamBrev(tjenestebussIntegrasjonService, call, request, navIdent, metadata, navn, safService)
}

private suspend fun bestillExtreamBrev(
    tjenestebussIntegrasjonService: TjenestebussIntegrasjonService,
    call: ApplicationCall,
    request: OrderLetterRequest,
    navIdent: String,
    metadata: BrevdataDto,
    navn: String,
    safService: SafService
): ServiceResult<BestillOgRedigerBrevResponse.Success, BestillOgRedigerBrevResponse.Failure> {
    val bestillingsResult: ServiceResult<BestillExtreamBrevResponseDto.Success, BestillExtreamBrevResponseDto.Failure> =
        tjenestebussIntegrasjonService.bestillExtreamBrev(call, request, navIdent, metadata, navn)

    return bestillingsResult.map { result ->
        val error = safService.waitForJournalpostStatusUnderArbeid(call, result.journalpostId)

        return if (error != null) {
            ServiceResult.Error(
                BestillOgRedigerBrevResponse.Failure(
                    message = null,
                    type = when (error.type) {
                        SafService.JournalpostLoadingError.ErrorType.ERROR -> SAF_ERROR
                        SafService.JournalpostLoadingError.ErrorType.TIMEOUT -> FERDIGSTILLING_TIMEOUT
                    },
                ), HttpStatusCode.Accepted
            )
        } else {
            tjenestebussIntegrasjonService.redigerExtreamBrev(call, result.journalpostId).map {
                BestillOgRedigerBrevResponse.Success(it.url)
            }.catch {
                BestillOgRedigerBrevResponse.Failure(
                    message = it.message,
                    type = EXTREAM_GENERELL
                )
            }
        }
    }.catch { error ->
        BestillOgRedigerBrevResponse.Failure(message = error.message, EXTREAM_GENERELL)
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

sealed class BestillOgRedigerBrevResponse {
    data class Success(val url: String) : BestillOgRedigerBrevResponse()
    data class Failure(val message: String?, val type: FailureType)
    enum class FailureType {
        DOKSYS_LASING,
        DOKSYS_IKKE_TILLATT,
        DOKSYS_VALIDERING_FEILET,
        DOKSYS_IKKE_FUNNET,
        DOKSYS_IKKE_TILGANG,
        DOKSYS_LUKKET,
        FERDIGSTILLING_TIMEOUT,
        SAF_ERROR,
        EXTREAM_GENERELL,
    }
}