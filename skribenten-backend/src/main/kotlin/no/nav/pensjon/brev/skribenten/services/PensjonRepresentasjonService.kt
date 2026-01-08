package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.Cacheomraade
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.cached
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class PensjonRepresentasjonService(
    config: Config,
    authService: AuthService,
    private val cache: Cache,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val pensjonPersondataURL = config.getString("url")
    private val scope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(pensjonPersondataURL)
        }
        engine {
            requestTimeout = 10.seconds.inWholeMilliseconds
        }
        install(ContentNegotiation) { jackson() }
        callIdAndOnBehalfOfClient(scope, authService)
    }

    data class HasRepresentantRequest(
        val representertPid: String,
        val validRepresentasjonstyper: List<RelevanteRepresentasjonstyper>,
    )

    data class HasRepresentantResponse(val value: Boolean)

    enum class RelevanteRepresentasjonstyper {
        PENSJON_VERGE,
        PENSJON_PENGEMOTTAKER,
        PENSJON_VERGE_PENGEMOTTAKER,
    }

    suspend fun harVerge(fnr: String): Boolean? =
        cache.cached(
            Cacheomraade.PENSJON_REPRESENTASJON,
            fnr,
            ttl = { 10.minutes }
        ){
            try {
                val response = client.post("/representasjon/hasRepresentant") {
                    contentType(ContentType.Application.Json)
                    setBody(HasRepresentantRequest(fnr, RelevanteRepresentasjonstyper.entries))
                }
                return@cached if (response.status.isSuccess()) {
                    response.body<HasRepresentantResponse>().value
                } else {
                    logger.error("Klarte ikke å hente representasjonsforhold Status: ${response.status} Response: ${response.bodyAsText()}")
                    null
                }
            } catch (e: Exception) {
                logger.error("Klarte ikke å hente representasjonsforhold: ${e.message}")
                null
            }

        }
}