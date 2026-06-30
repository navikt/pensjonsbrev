package no.nav.pensjon.brev.skribenten.services

import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.OboClientConfig
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.Cacheomraade
import no.nav.pensjon.brev.skribenten.common.cached
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.minutes

interface SkjermingService {
    suspend fun hentSkjerming(pid: Pid): Boolean?
}

private val logger = LoggerFactory.getLogger(SkjermingService::class.java)

class SkjermingServiceHttp(
    config: OboClientConfig,
    authService: AuthService,
    private val cache: Cache
) : SkjermingService {
    private val url: String = config.url
    private val scope: String = config.scope

    private val client = lagHttpClient {
        defaultRequest { url(this@SkjermingServiceHttp.url) }
        installRetry(logger)
        install(ContentNegotiation) { jackson() }
        callIdAndOnBehalfOfClient(scope, authService)
    }

    override suspend fun hentSkjerming(pid: Pid): Boolean? =
        cache.cached(Cacheomraade.SKJERMING, pid, ttl = { 5.minutes }) {
            val response = client.post {
                contentType(ContentType.Application.Json)
                setBody(mapOf("personident" to pid.value))
            }
            return@cached if (response.status.isSuccess()) {
                response.body<Boolean>()
            } else {
                logger.error("Kunne ikke hente skjermings-status for bruker. Status: ${response.status} Message: ${response.bodyAsText()}")
                null
            }
        }


}
