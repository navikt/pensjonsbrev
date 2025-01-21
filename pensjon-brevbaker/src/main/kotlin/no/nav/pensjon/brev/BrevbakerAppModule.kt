package no.nav.pensjon.brev

import com.fasterxml.jackson.core.JacksonException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopPreparing
import io.ktor.server.application.ServerReady
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.auth.Authentication
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ParameterConversionException
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.calllogging.processingTimeMillis
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.util.date.getTimeMillis
import no.nav.pensjon.brev.Metrics.configureMetrics
import no.nav.pensjon.brev.api.ParseLetterDataException
import no.nav.pensjon.brev.converters.LetterResponseFileConverter
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.LatexCompileException
import no.nav.pensjon.brev.latex.LatexInvalidException
import no.nav.pensjon.brev.latex.LatexTimeoutException
import no.nav.pensjon.brev.maler.AllTemplates
import no.nav.pensjon.brev.routing.brevRouting
import no.nav.pensjon.brev.template.brevbakerConfig

fun Application.brevbakerModule(
    templates: AllTemplates,
    konfigurerFeatureToggling: (ApplicationConfig) -> Unit = { konfigurerUnleash(it) }
) {
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

    val jwtConfigs = listOf(JwtConfig.requireAzureADConfig(brevbakerConfig.config("azureAD")))
    install(Authentication) {
        jwtConfigs.forEach {
            brevbakerJwt(it)
        }
    }

    val latexCompilerService = LaTeXCompilerService(
        pdfByggerUrl = brevbakerConfig.property("pdfByggerUrl").getString(),
        maxRetries = brevbakerConfig.propertyOrNull("pdfByggerMaxRetries")?.getString()?.toInt() ?: 30,
    )

    konfigurerFeatureToggling(brevbakerConfig)

    configureMetrics()
    brevRouting(jwtConfigs.map { it.name }.toTypedArray(), latexCompilerService, templates)
}

private fun konfigurerUnleash(brevbakerConfig: ApplicationConfig) {
    FeatureToggleHandler.configure {
        with(brevbakerConfig.config("unleash")) {
            appName = stringProperty("appName")
            environment = stringProperty("environment")
            this@configure.host = stringProperty("host")
            apiToken = stringProperty("apiToken")
        }
    }
}

private fun ApplicationConfig.stringProperty(path: String): String = this.property(path).getString()