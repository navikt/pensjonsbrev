package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.JsonNode
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.pensjon.brev.skribenten.auth.AuthorizedHttpClientResult
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

private const val HENT_JOURNAL_STATUS_QUERY_RESOURCE = "/saf/HentJournalpostStatus.graphql"
private const val HENT_DOKUMENTER_QUERY_RESOURCE = "/saf/HentDokumenter.graphql"
private val hentJournalStatusQuery = SafService::class.java.getResource(HENT_JOURNAL_STATUS_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_JOURNAL_STATUS_QUERY_RESOURCE")
private val hentDokumenterQuery = SafService::class.java.getResource(HENT_DOKUMENTER_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_JOURNAL_STATUS_QUERY_RESOURCE")

private const val TIMEOUT = 60

data class JournalVariables(val journalpostId: String)
data class JournalQuery(
    val query: String,
    val variables: JournalVariables
)

enum class JournalpostLoadingResult {
    ERROR, NOT_READY, READY
}

class SafService(config: Config, authService: AzureADService) : ServiceStatus {
    private val safUrl = config.getString("url")
    private val safRestUrl = config.getString("rest_url")
    private val safScope = config.getString("scope")
    private val logger = LoggerFactory.getLogger(this::class.java)

    //TODO vurder å bruke en egen client for graphql: (https://opensource.expediagroup.com/graphql-kotlin/docs/client/client-overview/)
    private val client = AzureADOnBehalfOfAuthorizedHttpClient(safScope, authService) {
        defaultRequest {
            url(safUrl)
        }
        install(ContentNegotiation) {
            jackson()
        }
    }

    data class HentJournalStatusResponse(val data: HentJournalpostData?, val errors: JsonNode?)
    data class HentJournalpostData(val journalpost: JournalPost)

    data class HentDokumenterResponse(val data: Journalposter?, val errors: JsonNode?) {
        data class Journalposter(val journalpost: Journalpost)
        data class Journalpost(val journalpostId: String, val dokumenter: List<Dokument>)
        data class Dokument(val dokumentInfoId: String)
    }

    data class JournalPost(val journalpostId: String, val journalstatus: Journalstatus)
    enum class Journalstatus {
        MOTTATT, JOURNALFOERT, FERDIGSTILT, EKSPEDERT, UNDER_ARBEID, FEILREGISTRERT, UTGAAR, AVBRUTT, UKJENT_BRUKER, RESERVERT, OPPLASTING_DOKUMENT, UKJENT,
    }

    private suspend fun getStatus(
        call: ApplicationCall,
        journalpostId: String
    ): JournalpostLoadingResult =
        client.post(call, "") {
            contentType(ContentType.Application.Json)
            setBody(
                JournalQuery(
                    query = hentJournalStatusQuery,
                    variables = JournalVariables(journalpostId)
                )
            )
        }.toServiceResult<HentJournalStatusResponse>()
            .map {
                if (it.data != null) {
                    return if (it.data.journalpost.journalstatus == Journalstatus.UNDER_ARBEID) {
                        JournalpostLoadingResult.READY
                    } else {
                        JournalpostLoadingResult.NOT_READY
                    }
                } else if (it.errors != null) {
                    logger.error("Feil ved henting a journalstatus fra SAF. JournalpostId: $journalpostId Errors: ${it.errors}")
                    JournalpostLoadingResult.ERROR
                } else {
                    logger.error("Tom response ved henting av jouranlpoststatus fra SAF.  JournalpostId: $journalpostId")
                    JournalpostLoadingResult.ERROR
                }
            }.catch { message, status ->
                logger.error("Feil ved henting a journalstatus fra SAF. JournalpostId: $journalpostId, Status: $status, Melding: $message")
                JournalpostLoadingResult.ERROR
            }

    suspend fun waitForJournalpostStatusUnderArbeid(
        call: ApplicationCall,
        journalpostId: String
    ): JournalpostLoadingResult =
        withTimeoutOrNull(TIMEOUT.seconds) {
            for (i in 1..TIMEOUT) {
                delay(1000)
                when (val result = getStatus(call, journalpostId)) {
                    JournalpostLoadingResult.READY,
                    JournalpostLoadingResult.ERROR -> return@withTimeoutOrNull result

                    JournalpostLoadingResult.NOT_READY -> {}
                }
            }
            return@withTimeoutOrNull JournalpostLoadingResult.NOT_READY
        } ?: JournalpostLoadingResult.NOT_READY

    private suspend fun getDocumentsInJournal(call: ApplicationCall, journalpostId: String) =
        client.post(call, "") {
            contentType(ContentType.Application.Json)
            setBody(
                JournalQuery(
                    query = hentDokumenterQuery,
                    variables = JournalVariables(journalpostId)
                )
            )
        }.toServiceResult<HentDokumenterResponse>()

    suspend fun getFirstDocumentInJournal(
        call: ApplicationCall,
        journalpostId: String
    ): ServiceResult<HentDokumenterResponse> =
        getDocumentsInJournal(call, journalpostId)

    /*
     * man kan spesifisere hvilket 'variantFormat' vi vil ha - per nå er vi bare interesert i 'ARKIV' versjonen
     */
    suspend fun hentPdfForJournalpostId(call: ApplicationCall, journalpostId: String): ServiceResult<ByteArray> =
        hentFørsteDokumentInfoIdFraJournalpost(call, journalpostId).map { dokumentInfoId ->
            return when (val res =
                client.get(call = call, url = "$safRestUrl/hentdokument/$journalpostId/$dokumentInfoId/ARKIV")) {
                is AuthorizedHttpClientResult.Error -> ServiceResult.Error(
                    "Feil ved token-utveksling correlation_id: ${res.error.correlation_id} Description:${res.error.error_description}",
                    HttpStatusCode.Unauthorized
                )

                is AuthorizedHttpClientResult.Response -> ServiceResult.Ok(res.response.readBytes())
            }
        }

    /*
     *  Vi sender 1 dokument per journalpost
     */
    private suspend fun hentFørsteDokumentInfoIdFraJournalpost(
        call: ApplicationCall,
        journalpostId: String
    ): ServiceResult<String> {
        val dokumentInfoId = getDocumentsInJournal(call, journalpostId)
            .map { it.data?.journalpost?.dokumenter?.firstOrNull()?.dokumentInfoId }

        return dokumentInfoId.resultOrNull().let {
            when (it) {
                null -> ServiceResult.Error(
                    "Fant ingen dokumenter for journalpostId: $journalpostId",
                    HttpStatusCode.NotFound
                )

                else -> ServiceResult.Ok(it)
            }
        }
    }

    override val name = "SAF"
    override suspend fun ping(call: ApplicationCall) =
        client.options(call, "").toServiceResult<String>().map { true }
}