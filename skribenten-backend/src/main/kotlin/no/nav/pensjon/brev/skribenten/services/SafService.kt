package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

private const val HENT_JOURNAL_STATUS_QUERY_RESOURCE = "/saf/HentJournalpostStatus.graphql"
private val hentJournalStatusQuery = SafService::class.java.getResource(HENT_JOURNAL_STATUS_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_JOURNAL_STATUS_QUERY_RESOURCE")

data class JournalVariables(val journalpostId: String)
data class JournalQuery(
    val query: String,
    val variables: JournalVariables
)

class SafService(config: Config, authService: AzureADService) {
    private val safUrl = config.getString("url")
    private val safScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(safScope, authService) {
        defaultRequest {
            url(safUrl)
        }
        install(ContentNegotiation) {
            jackson()
        }
    }

    data class HentJournalStatusResponse(val data: HentJournalpostData)
    data class HentJournalpostData(val journalpost: JournalPost)
    data class JournalPost(val journalpostId: String, val journalstatus: Journalstatus)
    enum class Journalstatus {
        MOTTATT, JOURNALFOERT, FERDIGSTILT, EKSPEDERT, UNDER_ARBEID, FEILREGISTRERT, UTGAAR, AVBRUTT, UKJENT_BRUKER, RESERVERT, OPPLASTING_DOKUMENT, UKJENT,
    }

    suspend fun getStatus(
        call: ApplicationCall,
        journalpostId: String
    ): ServiceResult<HentJournalStatusResponse, String> =
        client.post(call, "") {
            contentType(ContentType.Application.Json)
            setBody(
                JournalQuery(
                    query = hentJournalStatusQuery,
                    variables = JournalVariables(journalpostId)
                )
            )
        }.toServiceResult<HentJournalStatusResponse, String>()


    //TODO replace string return with a class or something. Better error handling and some cleanup.
    suspend fun waitForJournalpostStatusUnderArbeid(call: ApplicationCall, journalpostId: String): String? {
        for (i in 1..10) {
            println("venter på journalpost $journalpostId runde $i")
            when (val result = getStatus(call, journalpostId)) {
                is ServiceResult.Ok -> {
                    if (result.result.data.journalpost.journalstatus == Journalstatus.UNDER_ARBEID) {
                        return null
                    }
                }

                is ServiceResult.Error -> {
                    return result.error
                }

                is ServiceResult.AuthorizationError -> {
                    return result.error.error
                }
            }
            delay(3000)
        }
        return "Timeout"
    }

}