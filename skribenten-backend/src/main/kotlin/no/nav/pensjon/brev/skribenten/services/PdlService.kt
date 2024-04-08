package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.typesafe.config.Config
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.util.*
import no.nav.pensjon.brev.skribenten.auth.AzureADOnBehalfOfAuthorizedHttpClient
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import no.nav.pensjon.brev.skribenten.services.PenService.SakType
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.ALDER
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.BARNEP
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.GJENLEV
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.UFOREP
import org.slf4j.LoggerFactory

private const val HENT_NAVN_QUERY_RESOURCE = "/pdl/HentNavn.graphql"
private const val HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE = "/pdl/HentAdressebeskyttelse.graphql"

private val hentNavnQuery = PdlService::class.java.getResource(HENT_NAVN_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_NAVN_QUERY_RESOURCE")

private val hentAdressebeskyttelseQuery = PdlService::class.java.getResource(HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE")

private val logger = LoggerFactory.getLogger(PdlService::class.java)

class PdlService(config: Config, authService: AzureADService) : ServiceStatus {
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

    private data class PDLQuery<T : Any>(
        val query: String,
        val variables: T
    )

    private data class FnrVariables(
        val ident: String,
        val historikk: Boolean = false
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class PDLResponse<T : Any>(
        val data: T?,
        val errors: List<PDLError>?,
        val extensions: JsonNode?,
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class PDLError(val message: String, val extensions: PDLExtensions?) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class PDLExtensions(val code: ErrorCode, val details: Details?) {
                @Suppress("EnumEntryName")
                enum class ErrorCode { unauthenticated, unauthorized, not_found, bad_request, server_error }
                @JsonIgnoreProperties(ignoreUnknown = true)
                data class Details(val type: String?, val cause: String?, val policy: String?, val errors: List<String>?)
            }
        }
    }

    enum class Behandlingsnummer(val saktyper: SakType) {
        B280(ALDER),
        B359(BARNEP),
        B222(GJENLEV),
        B255(UFOREP);
        companion object {
            infix fun from(saktyper: SakType): Behandlingsnummer? = entries.firstOrNull { it.saktyper == saktyper }
        }
    }

    private data class DataWrapperPersonMedNavn(val hentPerson: PersonMedNavn?) {
        data class PersonMedNavn(val navn: List<Navn>? = null) {
            data class Navn(val fornavn: String, val mellomnavn: String?, val etternavn: String) {
                fun format() = "$fornavn ${mellomnavn?.plus(" ") ?: ""}${etternavn}"
            }
        }
    }

    private data class DataWrapperPersonMedAdressebeskyttelse(val hentPerson: PersonMedAdressebeskyttelse?) {
        data class PersonMedAdressebeskyttelse(val adressebeskyttelse: List<Adressebeskyttelse>) {
            data class Adressebeskyttelse(val gradering: Gradering)
        }
    }

    enum class Behandling {
        FORTROLIG,
        STRENGT_FORTROLIG,
        STRENGT_FORTROLIG_UTLAND,
        INGEN
    }

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

    suspend fun hentAdressebeskyttelse(call: ApplicationCall, fnr: String): ServiceResult<List<Gradering>> {
        return client.post(call, "") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                PDLQuery(
                    query = hentAdressebeskyttelseQuery,
                    variables = FnrVariables(fnr)
                )
            )
            headers {
                set("Behandlingsnummer", call.attributes[AttributeKey("saktype")]) // Settes av AuthorizeAnsattSakTilgang
            }
        }.toServiceResult<PDLResponse<DataWrapperPersonMedAdressebeskyttelse>>()
            .handleGraphQLErrors()
            .map {
                it.hentPerson?.adressebeskyttelse?.map { b -> b.gradering } ?: emptyList()
            }
    }

    private fun <T : Any> ServiceResult<PDLResponse<T>>.handleGraphQLErrors(): ServiceResult<T> =
        when (this) {
            is ServiceResult.Error -> ServiceResult.Error(error, statusCode)
            is ServiceResult.Ok ->
                if (result.errors?.isNotEmpty() == true) {
                    result.errors.also { it.logErrors() }.first().mapError()
                } else if (result.data != null) {
                    ServiceResult.Ok(result.data)
                } else {
                    ServiceResult.Error("Fant ikke person", HttpStatusCode.NotFound)
                }
        }

    private fun <T> PDLResponse.PDLError.mapError(): ServiceResult.Error<T> =
        ServiceResult.Error(
            error = message,
            statusCode = when (extensions?.code) {
                PDLResponse.PDLError.PDLExtensions.ErrorCode.unauthenticated -> HttpStatusCode.Unauthorized
                PDLResponse.PDLError.PDLExtensions.ErrorCode.unauthorized -> HttpStatusCode.Forbidden
                PDLResponse.PDLError.PDLExtensions.ErrorCode.not_found -> HttpStatusCode.NotFound
                PDLResponse.PDLError.PDLExtensions.ErrorCode.bad_request -> HttpStatusCode.BadRequest
                PDLResponse.PDLError.PDLExtensions.ErrorCode.server_error -> HttpStatusCode.InternalServerError
                null -> HttpStatusCode.InternalServerError
            }
        )

    private fun List<PDLResponse.PDLError>.logErrors() {
        if (size > 1) {
            logger.warn("Got multiple errors from PDL, only first is included in response.")
        }
        filter { it.extensions?.code != PDLResponse.PDLError.PDLExtensions.ErrorCode.not_found }
            .forEach { logger.info("${it.message}: {}", it.extensions) }
    }

    override val name = "PDL"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        client.options(call, "")
            .toServiceResult<String>()
            .map { true }

}
