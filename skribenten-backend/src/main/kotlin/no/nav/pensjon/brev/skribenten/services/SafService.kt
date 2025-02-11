package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.JsonNode
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

private const val HENT_JOURNAL_STATUS_QUERY_RESOURCE = "/saf/HentJournalpostStatus.graphql"
private const val HENT_DOKUMENTER_QUERY_RESOURCE = "/saf/HentDokumenter.graphql"
private val hentJournalStatusQuery =
    SafService::class.java.getResource(HENT_JOURNAL_STATUS_QUERY_RESOURCE)?.readText()
        ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_JOURNAL_STATUS_QUERY_RESOURCE")
private val hentDokumenterQuery =
    SafService::class.java.getResource(HENT_DOKUMENTER_QUERY_RESOURCE)?.readText()
        ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_JOURNAL_STATUS_QUERY_RESOURCE")

private const val TIMEOUT = 60

data class JournalVariables(val journalpostId: String)

data class JournalQuery(
    val query: String,
    val variables: JournalVariables,
)

enum class JournalpostLoadingResult {
    ERROR,
    NOT_READY,
    READY,
}

class SafService(config: Config, authService: AzureADService) : ServiceStatus {
    private val safUrl = config.getString("url")
    private val safRestUrl = config.getString("rest_url")
    private val safScope = config.getString("scope")
    private val logger = LoggerFactory.getLogger(this::class.java)

    // TODO vurder å bruke en egen client for graphql: (https://opensource.expediagroup.com/graphql-kotlin/docs/client/client-overview/)
    private val client =
        HttpClient(CIO) {
            defaultRequest {
                url(safUrl)
            }
            install(ContentNegotiation) {
                jackson()
            }
            callIdAndOnBehalfOfClient(safScope, authService)
        }

    data class HentJournalStatusResponse(val data: HentJournalpostData?, val errors: JsonNode?)

    data class HentJournalpostData(val journalpost: JournalPost)

    data class HentDokumenterResponse(val data: Journalposter?, val errors: JsonNode?) {
        data class Journalposter(val journalpost: Journalpost)

        data class Journalpost(val journalpostId: String, val dokumenter: List<Dokument>)

        data class Dokument(val dokumentInfoId: String)
    }

    data class JournalPost(val journalpostId: String, val journalstatus: Journalstatus)

    @Suppress("unused")
    enum class Journalstatus {
        MOTTATT,
        JOURNALFOERT,
        FERDIGSTILT,
        EKSPEDERT,
        UNDER_ARBEID,
        FEILREGISTRERT,
        UTGAAR,
        AVBRUTT,
        UKJENT_BRUKER,
        RESERVERT,
        OPPLASTING_DOKUMENT,
        UKJENT,
    }

    private suspend fun getStatus(journalpostId: String): JournalpostLoadingResult =
        client.post("") {
            contentType(ContentType.Application.Json)
            setBody(
                JournalQuery(
                    query = hentJournalStatusQuery,
                    variables = JournalVariables(journalpostId),
                ),
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

    suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: String): JournalpostLoadingResult =
        withTimeoutOrNull(TIMEOUT.seconds) {
            for (i in 1..TIMEOUT) {
                delay(1000)
                when (val result = getStatus(journalpostId)) {
                    JournalpostLoadingResult.READY,
                    JournalpostLoadingResult.ERROR,
                    -> return@withTimeoutOrNull result

                    JournalpostLoadingResult.NOT_READY -> {}
                }
            }
            return@withTimeoutOrNull JournalpostLoadingResult.NOT_READY
        } ?: JournalpostLoadingResult.NOT_READY

    private suspend fun getDocumentsInJournal(journalpostId: String) =
        client.post("") {
            contentType(ContentType.Application.Json)
            setBody(
                JournalQuery(
                    query = hentDokumenterQuery,
                    variables = JournalVariables(journalpostId),
                ),
            )
        }.toServiceResult<HentDokumenterResponse>()

    suspend fun getFirstDocumentInJournal(journalpostId: String): ServiceResult<HentDokumenterResponse> =
        getDocumentsInJournal(journalpostId)

    /*
     * man kan spesifisere hvilket 'variantFormat' vi vil ha - per nå er vi bare interesert i 'ARKIV' versjonen
     */
    suspend fun hentPdfForJournalpostId(journalpostId: String): ServiceResult<ByteArray> =
        hentFoersteDokumentInfoIdFraJournalpost(journalpostId).then { dokumentInfoId ->
            client.get("$safRestUrl/hentdokument/$journalpostId/$dokumentInfoId/ARKIV").toServiceResult()
        }

    /*
     *  Vi sender 1 dokument per journalpost
     */
    private suspend fun hentFoersteDokumentInfoIdFraJournalpost(journalpostId: String): ServiceResult<String> {
        return getDocumentsInJournal(journalpostId)
            .map { it.data?.journalpost?.dokumenter?.firstOrNull()?.dokumentInfoId }
            .nonNull(
                "Fant ingen dokumenter for journalpostId: $journalpostId",
                HttpStatusCode.NotFound,
            )
    }

    override val name = "SAF"

    override suspend fun ping() =
        client.options("").toServiceResult<String>().map { true }
}
