package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.MottakerSearchRequest
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService

private const val HENT_NAVN_QUERY_RESOURCE = "/pdl/HentNavn.graphql"
private const val SOEK_MOTTAKER_QUERY_RESOURCE = "/pdl/PersonSoek.graphql"

class PdlService(config: Config, authService: AzureADService) {
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
        val errors: List<String>? = null
    )

    private data class DataWrapperPersonMedNavn(val hentPerson: PersonMedNavn?)
    private data class PersonMedNavn(val navn: List<Navn>? = null)
    data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String)

    private val hentNavnQuery = PdlService::class.java.getResource(HENT_NAVN_QUERY_RESOURCE)?.readText()
        ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_NAVN_QUERY_RESOURCE")

    suspend fun hentNavn(call: ApplicationCall, fnr: String): ServiceResult<String, String> {
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
        }.toServiceResult<PDLResponse<DataWrapperPersonMedNavn>, String>()
            .map {
                it.data?.hentPerson?.navn?.firstOrNull()?.format() ?: "" // TODO hvordan får vi error her?
            }
    }

    private val personSoekQuery = PdlService::class.java.getResource(SOEK_MOTTAKER_QUERY_RESOURCE)?.readText()
        ?: throw IllegalStateException("Kunne ikke hente query ressurs $SOEK_MOTTAKER_QUERY_RESOURCE")

    private data class SearchRule(
        val contains: String? = null,
    )

    private data class Criterion(
        val fieldName: String,
        val searchRule: SearchRule,
        val searchHistorical: Boolean = false,
    )

    private data class PersonSoekVariables(val paging: Paging, val criteria: List<Criterion>)
    private data class Paging(val pageNumber: Int, val resultsPerPage: Int)

    private data class PersonSoekResult(val sokPerson: SokPerson)
    private data class SokPerson(val pageNumber: Int, val totalHits: Int, val hits: List<Hit>)

    private data class Hit(val person: Person)
    private data class Person(val navn: List<Navn>)

    data class PersonSoekResponse(val totalHits: Int, val resultat: List<Result>) {
        data class Result(val navn: String)
    }

    suspend fun personSoek(
        call: ApplicationCall,
        request: MottakerSearchRequest
    ): ServiceResult<PersonSoekResponse, String> {
        return client.post(call, "") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            headers {
                set("Tema", "PEN")
            }
            setBody(
                PDLQuery(
                    query = personSoekQuery,
                    variables = PersonSoekVariables(
                        paging = Paging(
                            pageNumber = 1,
                            resultsPerPage = 20,
                        ),
                        listOf(
                            Criterion(
                                fieldName = "fritekst.navn",
                                searchRule = SearchRule(contains = request.soeketekst),
                                searchHistorical = false
                            )
                        )
                    ),
                )
            )
        }.toServiceResult<PDLResponse<PersonSoekResult>, String>()
            .map { result ->
                val data = result.data!!.sokPerson
                PersonSoekResponse(
                    totalHits = data.totalHits,
                    resultat = data.hits.mapNotNull { hit ->
                        hit.person.navn.firstOrNull()?.format()?.let { navn -> PersonSoekResponse.Result(navn) }
                    }
                )
            }
    }


    private fun Navn.format() = "$fornavn ${mellomnavn?.plus(" ") ?: ""}${etternavn}"
}
