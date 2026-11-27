package no.nav.pensjon.brev.brevoppskrift

import com.auth0.jwk.JwkProvider
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.http.content.DefaultContentTypesAttribute
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentTypeWithQuality
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.responseType
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.brevoppskrift.auth.draftAuthentication
import no.nav.pensjon.brev.brevoppskrift.auth.AzureSettings
import no.nav.pensjon.brev.brevoppskrift.api.Problem
import no.nav.pensjon.brev.brevoppskrift.integrasjoner.PrJobWorker

const val MAX_DRAFT_BYTES = 5 * 1024 * 1024

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused")
fun Application.brevoppskriftModule() {
    configureDependencies()
    configureApplication()
}

fun Application.configureApplication() {
    val config: AzureSettings by dependencies
    val jwks: JwkProvider by dependencies
    val prWorker: PrJobWorker by dependencies
    val metrics: PrometheusMeterRegistry by dependencies
    configureLogging()
    configureMetrics()
    install(ContentNegotiation) {
        val converter = JacksonConverter(draftObjectMapper, streamBody = false)
        register(ContentType.Application.Json, converter)
        register(ContentType.Application.ProblemJson, converter)
        accept { call, accepted ->
            if (call.response.responseType?.type == Problem::class) {
                listOf(ContentTypeWithQuality(ContentType.Application.ProblemJson))
            } else {
                accepted
            }
        }
    }
    attributes[DefaultContentTypesAttribute] = listOf(ContentType.Application.Json)
    install(Authentication) { draftAuthentication(config, jwks) }
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
