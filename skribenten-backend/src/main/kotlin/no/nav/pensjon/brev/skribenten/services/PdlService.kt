package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.model.Pdl
import org.slf4j.LoggerFactory
import java.time.LocalDate

private const val HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE = "/pdl/HentAdressebeskyttelse.graphql"
private const val HENT_BRUKER_CONTEXT = "/pdl/HentBrukerContext.graphql"

private val hentAdressebeskyttelseQuery = PdlServiceHttp::class.java.getResource(HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_ADRESSEBESKYTTELSE_QUERY_RESOURCE")

private val hentBrukerContextQuery = PdlServiceHttp::class.java.getResource(HENT_BRUKER_CONTEXT)?.readText()
    ?: throw IllegalStateException("Kunne ikke hente query ressurs $HENT_BRUKER_CONTEXT")

private val logger = LoggerFactory.getLogger(PdlService::class.java)

interface PdlService {
    suspend fun hentAdressebeskyttelse(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<List<Pdl.Gradering>>
    suspend fun hentBrukerContext(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<Pdl.PersonContext>
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
            jackson { registerModule(JavaTimeModule()) }
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
    data class Adressebeskyttelse(val gradering: Pdl.Gradering) {
        fun erGradert(): Boolean = when(gradering) {
            Pdl.Gradering.FORTROLIG -> true
            Pdl.Gradering.STRENGT_FORTROLIG -> true
            Pdl.Gradering.STRENGT_FORTROLIG_UTLAND -> true
            Pdl.Gradering.UGRADERT -> false
        }
    }
    data class VergemaalEllerFremtidsfullmakt(val type: String)
    data class Doedsfall(val doedsdato: LocalDate)

    private data class DataWrapperPersonMedAdressebeskyttelse(val hentPerson: PersonMedAdressebeskyttelse?) {
        data class PersonMedAdressebeskyttelse(val adressebeskyttelse: List<Adressebeskyttelse>) {
            data class Adressebeskyttelse(val gradering: Pdl.Gradering)
        }
    }

    private data class DataWrapperPersonSakKontekst(val hentPerson: PersonForSakKontekst?) {
        data class PersonForSakKontekst(
            val adressebeskyttelse: List<Adressebeskyttelse>,
            val doedsfall: List<Doedsfall>,
        )
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

    override suspend fun hentBrukerContext(fnr: String, behandlingsnummer: Pdl.Behandlingsnummer?): ServiceResult<Pdl.PersonContext> {
        return client.post("") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(
                PDLQuery(
                    query = hentBrukerContextQuery,
                    variables = FnrVariables(fnr)
                )
            )
            headers {
                if(behandlingsnummer != null)  {
                    set("Behandlingsnummer", behandlingsnummer.name)
                }
            }
        }.toServiceResult<PDLResponse<DataWrapperPersonSakKontekst>>()
            .handleGraphQLErrors()
            .map { response ->
                val person = response.hentPerson
                Pdl.PersonContext(
                    adressebeskyttelse = person?.adressebeskyttelse?.any { it.erGradert() }?: false,
                    doedsdato = person?.doedsfall?.firstNotNullOfOrNull { it.doedsdato }
                )
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
