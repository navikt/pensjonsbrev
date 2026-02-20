package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.JsonNode
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.services.SafService.HentDokumenterResponse
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

private const val HENT_JOURNAL_STATUS_QUERY_RESOURCE = "/saf/HentJournalpostStatus.graphql"
private const val HENT_DOKUMENTER_QUERY_RESOURCE = "/saf/HentDokumenter.graphql"
private val hentJournalStatusQuery = SafService::class.java.getResource(HENT_JOURNAL_STATUS_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_JOURNAL_STATUS_QUERY_RESOURCE")
private val hentDokumenterQuery = SafService::class.java.getResource(HENT_DOKUMENTER_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_JOURNAL_STATUS_QUERY_RESOURCE")

private const val TIMEOUT = 60

private data class JournalVariables(val journalpostId: String)
private data class JournalQuery(
    val query: String,
    val variables: JournalVariables
)

enum class JournalpostLoadingResult {
    ERROR, NOT_READY, READY
}

interface SafService {
    suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: JournalpostId): JournalpostLoadingResult
    suspend fun getFirstDocumentInJournal(journalpostId: JournalpostId): HentDokumenterResponse
    suspend fun hentPdfForJournalpostId(journalpostId: JournalpostId): ByteArray?

    data class HentDokumenterResponse(val data: Journalposter?, val errors: JsonNode?) {
        data class Journalposter(val journalpost: Journalpost)
        data class Journalpost(val journalpostId: JournalpostId, val dokumenter: List<Dokument>)
        data class Dokument(val dokumentInfoId: String)
    }
}

class SafServiceException(message: String) : ServiceException(message)

class SafServiceHttp(config: Config, authService: AuthService) : SafService, ServiceStatus {
    private val safUrl = config.getString("url")
    private val safRestUrl = config.getString("rest_url")
    private val safScope = config.getString("scope")
    private val logger = LoggerFactory.getLogger(this::class.java)

    //TODO vurder å bruke en egen client for graphql: (https://opensource.expediagroup.com/graphql-kotlin/docs/client/client-overview/)
    private val client = HttpClient(CIO) {
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

    data class JournalPost(val journalpostId: JournalpostId, val journalstatus: Journalstatus)
    @Suppress("unused")
    enum class Journalstatus {
        MOTTATT, JOURNALFOERT, FERDIGSTILT, EKSPEDERT, UNDER_ARBEID, FEILREGISTRERT, UTGAAR, AVBRUTT, UKJENT_BRUKER, RESERVERT, OPPLASTING_DOKUMENT, UKJENT,
    }

    private suspend fun getStatus(journalpostId: JournalpostId): JournalpostLoadingResult {
        val response = client.post("") {
            contentType(ContentType.Application.Json)
            setBody(
                JournalQuery(
                    query = hentJournalStatusQuery,
                    variables = JournalVariables(journalpostId.id.toString())
                )
            )
        }
        return if (response.status.isSuccess()) {
            response.body<HentJournalStatusResponse>().let {
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
            }
        } else {
            val body = response.bodyAsText()
            logger.error("Feil ved henting a journalstatus fra SAF. JournalpostId: $journalpostId, Status: ${response.status}, Melding: $body")
            throw SafServiceException("Feil ved henting av journalpoststatus fra SAF for journalpostId $journalpostId: $body")
        }
    }

    override suspend fun waitForJournalpostStatusUnderArbeid(journalpostId: JournalpostId): JournalpostLoadingResult =
        withTimeoutOrNull(TIMEOUT.seconds) {
            for (i in 1..TIMEOUT) {
                delay(1000)
                when (val result = getStatus(journalpostId)) {
                    JournalpostLoadingResult.READY,
                    JournalpostLoadingResult.ERROR -> return@withTimeoutOrNull result

                    JournalpostLoadingResult.NOT_READY -> {}
                }
            }
            return@withTimeoutOrNull JournalpostLoadingResult.NOT_READY
        } ?: JournalpostLoadingResult.NOT_READY

    private suspend fun getDocumentsInJournal(journalpostId: JournalpostId): HentDokumenterResponse {
        val response = client.post("") {
            contentType(ContentType.Application.Json)
            setBody(
                JournalQuery(
                    query = hentDokumenterQuery,
                    variables = JournalVariables(journalpostId.id.toString())
                )
            )
        }

        return if (response.status.isSuccess()) {
            response.body()
        } else {
            val bodyAsText = response.bodyAsText()
            logger.error("Feil ved henting av dokumenter fra SAF. JournalpostId: $journalpostId Status: ${response.status} Melding: $bodyAsText")
            throw SafServiceException("Feil ved henting av dokumenter fra SAF for journalpostId $journalpostId: $bodyAsText")
        }
    }

    override suspend fun getFirstDocumentInJournal(journalpostId: JournalpostId): HentDokumenterResponse =
        getDocumentsInJournal(journalpostId)

    /*
     * man kan spesifisere hvilket 'variantFormat' vi vil ha - per nå er vi bare interesert i 'ARKIV' versjonen
     */
    override suspend fun hentPdfForJournalpostId(journalpostId: JournalpostId): ByteArray? {
        val dokumentInfoId = hentFoersteDokumentInfoIdFraJournalpost(journalpostId)
        return if (dokumentInfoId != null) {
            val response = client.get("$safRestUrl/hentdokument/${journalpostId.id}/$dokumentInfoId/ARKIV")
            if (response.status.isSuccess()) {
                response.body()
            } else {
                val body = response.bodyAsText()
                logger.error("Feil ved henting av pdf fra SAF. JournalpostId: $journalpostId DokumentInfoId: $dokumentInfoId Status: ${response.status} Melding: $body")
                throw SafServiceException("Feil ved henting av pdf fra SAF for journalpostId $journalpostId og dokumentInfoId $dokumentInfoId: $body")
            }
        } else {
            logger.error("Fant ikke dokumentInfoId for journalpostId: $journalpostId")
            null
        }
    }

    /*
     *  Vi sender 1 dokument per journalpost
     */
    private suspend fun hentFoersteDokumentInfoIdFraJournalpost(journalpostId: JournalpostId): String? =
        getDocumentsInJournal(journalpostId).data?.journalpost?.dokumenter?.firstOrNull()?.dokumentInfoId

    override suspend fun ping() =
        ping("SAF") { client.options("") }
}