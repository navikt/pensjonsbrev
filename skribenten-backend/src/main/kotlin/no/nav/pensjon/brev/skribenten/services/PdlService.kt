package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.JsonNode
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

private const val HENT_NAVN_QUERY_RESOURCE = "/pdl/HentNavn.graphql"

class PdlService(config: Config, authService: AzureADService): ServiceStatus {
    private val pdlUrl = config.getString("url")
    private val pdlScope = config.getString("scope")

    private val client = AzureADOnBehalfOfAuthorizedHttpClient(pdlScope, authService) {
        defaultRequest {
            url(pdlUrl)
            headers {
                // TODO vi har to behandlinger, skal vi sende med begge?, Skal vi sende ulik avhengig av tema?
                set("Behandlingsnummer", "B280")
            }
        }
        install(ContentNegotiation) {
            jackson()
        }
    }


    data class PDLQuery<T : Any>(
        val query: String,
        val variables: T
    )

    data class FnrVariables(
        val ident: String,
        val historikk: Boolean = false
    )

    private data class PDLResponse<T : Any>(
        val data: T?,
        val errors: JsonNode?,
        val extensions: JsonNode?,
    )

    // PersonMed navn
    private data class DataWrapperPersonMedNavn(val hentPerson: PersonMedNavn?)
    private data class PersonMedNavn(val navn: List<Navn>? = null)
    data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String) {
        fun format() = "$fornavn ${mellomnavn?.plus(" ") ?: ""}${etternavn}"
    }

    // PersonMedAdressebeskyttelse
    private data class DataWrapperPersonMedAdressebeskyttelse(val hentPerson: PersonMedAdressebeskyttelse?)
    private data class PersonMedAdressebeskyttelse(val gradering: Gradering?)
    enum class Gradering(val gruppetilgang: String) {
        FORTROLIG("AD-rollen 0000-GA-Fortrolig_Adresse"),
        STRENGT_FORTROLIG("AD-rollen 0000-GA-Strengt_Fortrolig_Adresse"),
        STRENGT_FORTROLIG_UTLAND("AD-rolle 0000-GA-Person-EndreStrengtFortroligUtland"),
        INGEN("")
    }

    private val hentNavnQuery = PdlService::class.java.getResource(HENT_NAVN_QUERY_RESOURCE)?.readText()
        ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_NAVN_QUERY_RESOURCE")

    suspend fun hentNavn(call: ApplicationCall, fnr: String): ServiceResult<String> {
        return client.post(call, "") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            headers {
                set("Tema", "PEN")
            }
            setBody(
                PDLQuery(
                    query = hentNavnQuery,
                    variables = FnrVariables(fnr)
                )
            )
        }.toServiceResult<PDLResponse<DataWrapperPersonMedNavn>>()
            .map {
                it.data?.hentPerson?.navn?.firstOrNull()?.format() ?: "" // TODO hvordan får vi error her?
            }
    }

    suspend fun hentAdressebeskyttelse(call: ApplicationCall, fnr: String): ServiceResult<Gradering> {
        return client.post(call, "") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                PDLQuery(
                    query = hentNavnQuery,
                    variables = FnrVariables(fnr)
                )
            )
        }.toServiceResult<PDLResponse<DataWrapperPersonMedAdressebeskyttelse>>()
            .map {
                it.data?.hentPerson?.gradering// ?: Gradering.INGEN
            }
    }

    override val name = "PDL"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        client.options(call, "")
            .toServiceResult<String>()
            .map { true }

}
