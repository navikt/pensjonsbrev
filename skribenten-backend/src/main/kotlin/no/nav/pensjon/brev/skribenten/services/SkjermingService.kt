package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.Cache
import no.nav.pensjon.brev.skribenten.Cacheomraade
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.cached
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.minutes

interface SkjermingService {
    suspend fun hentSkjerming(fnr: Foedselsnummer): Boolean?
}

private val logger = LoggerFactory.getLogger(SkjermingService::class.java)

class SkjermingServiceHttp(config: Config, authService: AuthService, private val cache: Cache) : SkjermingService {
    private val url = config.getString("url")
    private val scope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest { url(this@SkjermingServiceHttp.url) }
        installRetry(logger)
        install(ContentNegotiation) { jackson() }
        callIdAndOnBehalfOfClient(scope, authService)
    }

    override suspend fun hentSkjerming(fnr: Foedselsnummer): Boolean? =
        cache.cached(Cacheomraade.SKJERMING, fnr, ttl = { 5.minutes }) {
            val response = client.post {
                contentType(ContentType.Application.Json)
                setBody(mapOf("personident" to fnr.value))
            }
            return@cached if (response.status.isSuccess()) {
                response.body<Boolean>()
            } else {
                logger.error("Kunne ikke hente skjermings-status for bruker. Status: ${response.status} Message: ${response.bodyAsText()}")
                null
            }
        }


}
