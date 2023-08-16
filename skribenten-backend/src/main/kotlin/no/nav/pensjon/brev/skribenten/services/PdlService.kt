package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.MottakerSearchRequest
import no.nav.pensjon.brev.skribenten.MottakerSearchRequest.Place.INNLAND
import no.nav.pensjon.brev.skribenten.MottakerSearchRequest.Place.UTLAND
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.services.PdlService.Criteria.CriteriaLogic
import no.nav.pensjon.brev.skribenten.services.PdlService.Criteria.Criterion
import no.nav.pensjon.brev.skribenten.services.PdlService.PdlCompletionParameters.FieldValue

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class PDLResponse<T : Any>(
        val data: T?,
    )

    private data class DataWrapperPersonMedNavn(val hentPerson: PersonMedNavn?)
    private data class PersonMedNavn(val navn: List<Navn>? = null)
    data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String) {
        fun format() = "$fornavn ${mellomnavn?.plus(" ") ?: ""}${etternavn}"
    }

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
        val equals: String? = null,
        val boost: Double? = null,
    )

    private sealed class Criteria {
        data class Criterion(
            val fieldName: String?,
            val searchRule: SearchRule?,
            val searchHistorical: Boolean = false,
        ) : Criteria()

        data class CriteriaLogic(
            val and: List<Criteria>? = null,
            val or: List<Criteria>? = null,
            val not: List<Criteria>? = null,
        ) : Criteria()
    }


    private data class PersonSoekVariables(val paging: Paging, val criteria: List<Criteria>)
    private data class Paging(val pageNumber: Int, val resultsPerPage: Int)

    private data class PersonSoekResult(val sokPerson: SokPerson) {
        data class SokPerson(val pageNumber: Int, val totalHits: Int, val hits: List<Hit>)

        data class Hit(val person: Person)
        data class Foedsel(val foedselsdato: String)
        data class FolkeregisterIdentifikator(val identifikasjonsnummer: String)
        data class Person(
            val navn: List<Navn>,
            val foedsel: List<Foedsel>,
            val folkeregisteridentifikator: List<FolkeregisterIdentifikator>
        )
    }


    data class PersonSoekResponse(val totalHits: Int, val resultat: List<Result>) {
        data class Result(val navn: String, val id: String, val foedselsdato: String)
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

            val searchCriteria = mutableListOf<Criteria>(
                Criterion(
                    fieldName = "fritekst.navn",
                    searchRule = SearchRule(contains = request.soeketekst),
                    searchHistorical = false
                ),
            )

            if (request.place == INNLAND && request.kommuneNummer != null) {
                searchCriteria.add(CriteriaLogic(
                    or = listOf(
                        "person.oppholdsadresse.vegadresse.kommunenummer",
                        "person.bostedsadresse.vegadresse.kommunenummer",
                        "person.kontaktadresse.vegadresse.kommunenummer"
                    ).map {
                        Criterion(fieldName = it, SearchRule(equals = request.kommuneNummer.toString()))
                    }
                ))
            } else if (request.place == UTLAND && request.land != null) {
                searchCriteria.add(
                    Criterion(
                        fieldName = "fritekst.adresser",
                        searchRule = SearchRule(contains = request.land)
                    )
                )
            }

            setBody(
                PDLQuery(
                    query = personSoekQuery,
                    variables = PersonSoekVariables(
                        paging = Paging(pageNumber = 1, resultsPerPage = 20),
                        listOf(CriteriaLogic(and = searchCriteria))
                    ),
                )
            )
        }.toServiceResult<PDLResponse<PersonSoekResult>, String>()
            .map { result ->
                val data = result.data!!.sokPerson
                //TODO error handling
                PersonSoekResponse(
                    totalHits = data.totalHits,
                    resultat = data.hits.mapNotNull {
                        PersonSoekResponse.Result(
                            foedselsdato = it.person.foedsel.firstOrNull()?.foedselsdato ?: return@mapNotNull null,
                            navn = it.person.navn.firstOrNull()?.format() ?: return@mapNotNull null,
                            id = it.person.folkeregisteridentifikator.firstOrNull()?.identifikasjonsnummer
                                ?: return@mapNotNull null
                        )
                    }
                )
            }
    }

    // TODO delete this if we end up using pensjon-person's address service
//    private val hentAdresserQuery = PdlService::class.java.getResource(SOEK_MOTTAKER_QUERY_RESOURCE)?.readText()
//        ?: throw IllegalStateException("Kunne ikke hente query ressurs $SOEK_MOTTAKER_QUERY_RESOURCE")
//
//    private data class HentAdresserVariables(val fnr: String, val historikk: Boolean = false)
//
//    private data class HentAdresserPdlResponse(val hentPerson: Person) {
//        data class Person(
//            val bostedsadresse: Adresse?,
//            val kontaktadresse: Adresse?,
//            val oppholdsadresse: Adresse?,
//        )
//
//        abstract class Adresse {
//            abstract fun format(): List<String>
//            abstract fun type(): String
//        }
//
//        data class VegAdresse(
//            val adressenavn: String?,
//            val husnummer: String?,
//            val husbokstav: String?,
//            val postnummer: String?,
//            val bruksenhetsnummer: String?,
//        ) : Adresse() {
//            override fun format(): List<String> {
//
//            }
//
//            override fun type(): String {
//                TODO("Not yet implemented")
//            }
//        }
//    }

    data class HentAdresserResponse(val addressLine: List<String>)

    // TODO integrer mot pensjon sin egen persondata-tjeneste og hent adresse.
    fun hentAdresseForPersonFake(call: ApplicationCall, pid: String): ServiceResult<HentAdresserResponse, String> {
        return ServiceResult.Ok(
            HentAdresserResponse(
                listOf(
                    "AdresseLinje 1",
                    "AdresseLinje 2",
                    "AdresseLinje 3",
                    "AdresseLinje 4",
                    "AdresseLinje 5",
                )
            )
        )
    }

    //      "completionField": "vegadresse.adressenavn",
    //      "maxSuggestions": 10,
    //      "fieldValues": [
    //        {"fieldName": "vegadresse.adressenavn", "fieldValue": "oslo"}
    //      ]
    private data class PdlCompletionParameters(
        val completionField: String,
        val maxSuggestions: Int,
        val fieldValues: List<FieldValue>
    ){
        data class FieldValue(
            val fieldName: String,
            val fieldValue: String,
        )
    }

    suspend fun hentKommuneForslag(call: ApplicationCall, searchText: String): ServiceResult<String, String> {
        return client.post(call, "") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            headers {
                set("Tema", "PEN")
            }
            setBody(
                PDLQuery(
                    query = hentNavnQuery,
                    variables = PdlCompletionParameters(
                        completionField = "vegadresse.kommunenummer",
                        maxSuggestions = 10,
                        fieldValues = listOf(
                            FieldValue(
                                fieldName = "vegadresse.kommunenummer",
                                fieldValue = searchText
                            )
                        )
                    )
                )
            )
        }.toServiceResult<String, String>()
    }
}
