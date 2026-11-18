package no.nav.pensjon.brev.brevoppskrift

import com.auth0.jwk.JwkProvider
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.di.dependencies
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.brevoppskrift.auth.draftAuthentication
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.PrJobWorker

const val MAX_DRAFT_BYTES = 5 * 1024 * 1024

fun main(args: Array<String>) {
    EngineMain.main(args)
}

// Er satt i application.conf slik at EngineMain kaller på skribentenApp.
@Suppress("unused")
fun Application.brevoppskriftModule() {
    configureDependencies()
    configureApplication()
}

fun Application.configureApplication() {
    val config: BrevoppskriftConfig by dependencies
    val jwks: JwkProvider by dependencies
    val prWorker: PrJobWorker by dependencies
    val metrics: PrometheusMeterRegistry by dependencies
    configureLogging()
    configureMetrics()
    install(ContentNegotiation) { register(ContentType.Application.Json, JacksonConverter(draftObjectMapper, streamBody = false)) }
    install(Authentication) { draftAuthentication(config.azure, jwks) }
    configureErrors(metrics)
    configureRouting()
    monitor.subscribe(ApplicationStarted) { prWorker.start() }
}

data class ValidationResult(val valid: Boolean)

data class Capabilities(
    val formatVersion: Int,
    val validation: Boolean = true,
    val persistence: Boolean = false,
    val generation: Boolean = false,
    val ai: Boolean = false,
    val slack: Boolean = false,
    val pullRequests: Boolean = false,
)
