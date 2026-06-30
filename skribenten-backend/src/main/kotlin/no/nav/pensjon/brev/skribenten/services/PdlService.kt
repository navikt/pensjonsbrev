package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import no.nav.pensjon.brev.skribenten.OboClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.core.Closeable
import no.nav.pensjon.brev.skribenten.SkribentenConfig
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
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
    suspend fun hentAdressebeskyttelse(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): List<Pdl.Gradering>?
    suspend fun hentBrukerContext(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): Pdl.PersonContext?
}

class PdlServiceException(message: String, status: HttpStatusCode = HttpStatusCode.InternalServerError) : ServiceException(message, status = status)

class PdlServiceHttp(config: OboClientConfig, authService: AuthService) : PdlService, ServiceStatus, Closeable {

    @Suppress("unused") // Brukes av ktor-di
    constructor(config: SkribentenConfig, authService: AuthService): this(config.services.pdl, authService)

    private val pdlUrl = config.url
    private val pdlScope = config.scope

    private val client = lagHttpClient {
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

    private data class IdentVariables(
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

    override suspend fun hentAdressebeskyttelse(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): List<Pdl.Gradering>? =
        postQuery<DataWrapperPersonMedAdressebeskyttelse>(
            query = PDLQuery<IdentVariables>(hentAdressebeskyttelseQuery, IdentVariables(ident.value)),
            behandlingsnumre = behandlingsnumre,
        ).handleGraphQLErrors()
            ?.let {
                it.hentPerson?.adressebeskyttelse?.map { b -> b.gradering }
            }

    override suspend fun hentBrukerContext(ident: Pid, behandlingsnumre: List<Behandlingsnummer>): Pdl.PersonContext? =
        postQuery<DataWrapperPersonSakKontekst>(
            query = PDLQuery(query = hentBrukerContextQuery, variables = IdentVariables(ident.value)),
            behandlingsnumre = behandlingsnumre,
        ).handleGraphQLErrors()
            ?.let { response ->
                val person = response.hentPerson
                Pdl.PersonContext(
                    adressebeskyttelse = person?.adressebeskyttelse?.any { it.erGradert() }?: false,
                    doedsdato = person?.doedsfall?.firstNotNullOfOrNull { it.doedsdato }
                )
            }

    private suspend inline fun <reified T : Any> postQuery(query: PDLQuery<*>, behandlingsnumre: List<Behandlingsnummer>): PDLResponse<T> {
        val response = client.post("") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(query)
            headers {
                set("Behandlingsnummer", behandlingsnumre.joinToString(",") { it.value })
            }
        }
        return if (response.status.isSuccess()) {
            response.body()
        } else {
            throw PdlServiceException(response.bodyAsText())
        }
    }

    private fun <T : Any> PDLResponse<T>.handleGraphQLErrors(): T? =
        if (errors?.isNotEmpty() == true) {
            val error = errors.also { it.logErrors() }.first()
            throw PdlServiceException(
                message = error.message,
                status = when (error.extensions?.code) {
                    PDLResponse.PDLError.PDLExtensions.ErrorCode.unauthenticated -> HttpStatusCode.InternalServerError
                    PDLResponse.PDLError.PDLExtensions.ErrorCode.unauthorized -> HttpStatusCode.InternalServerError
                    PDLResponse.PDLError.PDLExtensions.ErrorCode.not_found -> HttpStatusCode.NotFound
                    PDLResponse.PDLError.PDLExtensions.ErrorCode.bad_request -> HttpStatusCode.BadRequest
                    PDLResponse.PDLError.PDLExtensions.ErrorCode.server_error -> HttpStatusCode.InternalServerError
                    null -> HttpStatusCode.InternalServerError
                }
            )
        } else {
            data
        }

    private fun List<PDLResponse.PDLError>.logErrors() {
        if (size > 1) {
            logger.warn("Got multiple errors from PDL, only first is included in response.")
        }
        filter { it.extensions?.code != PDLResponse.PDLError.PDLExtensions.ErrorCode.not_found }
            .forEach { logger.info("${it.message}: {}", it.extensions) }
    }

    override suspend fun ping() =
        ping("PDL") { client.options("") }

    override fun close() { client.close() }

}
