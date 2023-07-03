package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

class PdlService(config: Config, authService: AzureADService) {
    private val pdlUrl = config.getString("url")
    private val pdlScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(pdlScope, authService) {
        defaultRequest {
            url(pdlUrl)
        }
        install(ContentNegotiation) {
            jackson{
                registerModule(JavaTimeModule())
            }
        }
    }


    data class PersonGraphqlQuery(
        val query: String,
        val variables: FnrVariables
    )

    data class FnrVariables(
        val ident: String,
        val historikk: Boolean = false
    )

    val hentNavnQuery = PdlService::class.java.getResource("/pdl/HentNavn.graphql")?.readText()!!
    suspend fun hentNavn(call: ApplicationCall, fnr: String): ServiceResult<String, Any> {
        return client.post(call, ""){
            setBody(PersonGraphqlQuery(
                query = hentNavnQuery,
                variables = FnrVariables(fnr)
            ))
        }.toServiceResult<String, String>()
    }

}