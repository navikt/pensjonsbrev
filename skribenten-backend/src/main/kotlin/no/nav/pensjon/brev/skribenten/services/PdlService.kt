package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.headers
import io.ktor.client.request.options
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.model.Pdl
import org.slf4j.LoggerFactory

private const val HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE = "/pdl/HentAdressebeskyttelse.graphql"

private val hentAdressebeskyttelseQuery = PdlServiceHttp::class.java.getResource(HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE")

private val logger = LoggerFactory.getLogger(PdlService::class.java)

interface PdlService {
    suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<List<Pdl.Gradering>>
}

class PdlServiceHttp(config: Config, authService: AuthService) : PdlService, ServiceStatus {
    private val pdlUrl = config.getString("url")
    private val pdlScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(pdlUrl)
        }
        installRetry(logger)
        install(ContentNegotiation) {
            jackson()
        }
        callIdAndOnBehalfOfClient(pdlScope, authService)
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

    private data class DataWrapperPersonMedAdressebeskyttelse(val hentPerson: PersonMedAdressebeskyttelse?) {
        data class PersonMedAdressebeskyttelse(val adressebeskyttelse: List<Adressebeskyttelse>) {
            data class Adressebeskyttelse(val gradering: Pdl.Gradering)
        }
    }

    override suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<List<Pdl.Gradering>> {
        return client.post("") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                PDLQuery(
                    query = hentAdressebeskyttelseQuery,
                    variables = FnrVariables(fnr)
                )
            )
            headers {
                if(behandlingsnummer != null)  {
                    set("Behandlingsnummer", behandlingsnummer.name)
                }
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
    override suspend fun ping(): ServiceResult<Boolean> =
        client.options("")
            .toServiceResult<String>()
            .map { true }

}
