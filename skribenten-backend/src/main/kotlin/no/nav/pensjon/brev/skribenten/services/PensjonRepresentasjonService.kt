package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.Cacheomraade
import no.nav.pensjon.brev.skribenten.common.cached
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
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

    private val client = lagHttpClient {
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
        val representertPid: Pid,
        val validRepresentasjonstyper: List<RelevanteRepresentasjonstyper>,
    )

    data class HasRepresentantResponse(val value: Boolean)

    enum class RelevanteRepresentasjonstyper {
        VERGE_PENSJON_LES,
        VERGE_PENSJON_SKRIV,
        VERGE_UFORETRYGD_LES,
        VERGE_UFORETRYGD_SKRIV
    }

    suspend fun harVerge(pid: Pid): Boolean? =
        cache.cached(
            Cacheomraade.PENSJON_REPRESENTASJON,
            pid,
            ttl = { 10.minutes }
        ){
            try {
                val response = client.post("/representasjon/hasRepresentant") {
                    contentType(ContentType.Application.Json)
                    setBody(HasRepresentantRequest(pid, RelevanteRepresentasjonstyper.entries))
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