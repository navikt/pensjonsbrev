package no.nav.pensjon.brev

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.date.*
import no.nav.brev.brevbaker.AllTemplates
import no.nav.brev.brevbaker.LatexCompileException
import no.nav.brev.brevbaker.LatexInvalidException
import no.nav.brev.brevbaker.LatexTimeoutException
import no.nav.pensjon.brev.Metrics.configureMetrics
import no.nav.pensjon.brev.api.ParseLetterDataException
import no.nav.pensjon.brev.converters.LetterResponseFileConverter
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.routing.brevRouting
import no.nav.pensjon.brev.routing.useBrevkodeFromCallContext
import no.nav.pensjon.brev.template.brevbakerConfig

fun Application.brevbakerModule(templates: AllTemplates) {
    val brevbakerConfig = environment.config.config("brevbaker")

    monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
        FeatureToggleHandler.shutdown()
    }
    monitor.subscribe(ServerReady) { it.log.info("Ferdig med å sette opp applikasjonen") }

    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
        filter {
            !ignorePaths.contains(it.request.path())
        }
        mdc("x_response_code") { it.response.status()?.value?.toString() }
        mdc("x_response_time") { it.processingTimeMillis(::getTimeMillis).toString() }
        mdc("x_brevkode") { it.useBrevkodeFromCallContext() }
    }

    install(StatusPages) {
        exception<JacksonException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown cause")
        }
        // Work-around to print proper error message when call.receive<T> fails.
        exception<BadRequestException> { call, cause ->
            if (cause.cause is JacksonException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    cause.cause?.message ?: "Failed to deserialize json body: unknown reason"
                )
            } else {
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "Unknown failure")
            }
        }
        exception<LatexTimeoutException> { call, cause ->
            call.application.log.info("Latex compilation timed out", cause)
            call.respond(HttpStatusCode.ServiceUnavailable, cause.message ?: "Timed out while compiling latex")
        }
        exception<LatexCompileException> { call, cause ->
            call.application.log.info("Latex compilation failed with internal server error", cause)
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Latex compilation failed")
        }
        exception<LatexInvalidException> { call, cause ->
            call.application.log.info("Latex compilation failed due to invalid latex", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                cause.message ?: "Latex compilation failed due to invalid latex"
            )
        }
        exception<ParameterConversionException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                cause.message ?: "Failed to convert path parameter to required type: unknown cause"
            )
        }
        exception<ParseLetterDataException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize letterData: Unknown cause")
        }
    }

    install(CallId) {
        retrieveFromHeader("Nav-Call-Id")
        header("X-Request-ID")
        generate()
        verify { it.isNotEmpty() }
    }

    install(ContentNegotiation) {
        jackson {
            brevbakerConfig()
        }
        register(ContentType.Text.Html, LetterResponseFileConverter)
        register(ContentType.Application.Pdf, LetterResponseFileConverter)
    }

    val jwtConfigs = if (!developmentMode) {
        val configs = listOf(JwtConfig.requireAzureADConfig(brevbakerConfig.config("azureAD")))
        install(Authentication) {
            configs.forEach {
                brevbakerJwt(it)
            }
        }
        configs
    } else null


    val latexCompilerService = LaTeXCompilerService(
        pdfByggerUrl = brevbakerConfig.property("pdfByggerUrl").getString(),
        maxRetries = brevbakerConfig.propertyOrNull("pdfByggerMaxRetries")?.getString()?.toInt() ?: 30,
    )

    konfigurerUnleash(brevbakerConfig)

    configureMetrics()
    brevRouting(jwtConfigs?.map { it.name }?.toTypedArray(), latexCompilerService, templates)
}

private fun konfigurerUnleash(brevbakerConfig: ApplicationConfig) {
    with(brevbakerConfig.config("unleash")) {
        FeatureToggleHandler.configure {
            useFakeUnleash = booleanProperty("useFakeUnleash")
            fakeUnleashEnableAll = booleanProperty("fakeUnleashEnableAll")
            appName = stringProperty("appName")
            environment = stringProperty("environment")
            host = stringProperty("host")
            apiToken = stringProperty("apiToken")
        }
    }
}

private fun ApplicationConfig.booleanProperty(path: String, default: Boolean = false): Boolean = propertyOrNull(path)?.getString()?.toBoolean() ?: default
private fun ApplicationConfig.stringProperty(path: String): String? = propertyOrNull(path)?.getString()