package no.nav.pensjon.brev

import com.fasterxml.jackson.core.JacksonException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.date.*
import no.nav.pensjon.brev.Metrics.configureMetrics
import no.nav.pensjon.brev.api.ParseLetterDataException
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.latex.LatexCompileException
import no.nav.pensjon.brev.latex.LatexInvalidException
import no.nav.pensjon.brev.latex.LatexTimeoutException
import no.nav.pensjon.brev.template.brevbakerConfig

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun requireEnv(key: String) =
    System.getenv(key) ?: throw IllegalStateException("The environment variable $key is missing.")

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    environment.monitor.subscribe(ApplicationStopPreparing) {
        it.log.info("Application preparing to shutdown gracefully")
    }

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
                call.respond(HttpStatusCode.BadRequest, cause.cause?.message ?: "Failed to deserialize json body: unknown reason")
            } else {
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "Unknown failure")
            }
        }
        exception<LatexTimeoutException>{ call, cause ->
            call.application.log.info("Latex compilation timed out", cause)
            call.respond(HttpStatusCode.ServiceUnavailable, cause.message ?: "Timed out while compiling latex")
        }
        exception<LatexCompileException>{ call, cause ->
            call.application.log.info("Latex compilation failed with internal server error", cause)
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Latex compilation failed")
        }
        exception<LatexInvalidException>{ call, cause ->
            call.application.log.info("Latex compilation failed due to invalid latex", cause)
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Latex compilation failed due to invalid latex")
        }
        exception<ParameterConversionException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message?: "Failed to convert path parameter to required type: unknown cause")
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
    }

    val jwtConfigs = listOf(JwtConfig.requireAzureADConfig())
    install(Authentication) {
        jwtConfigs.forEach {
            brevbakerJwt(it)
        }
    }

    val latexCompilerService = LaTeXCompilerService(
        pdfByggerUrl = environment.config.property("brevbaker.pdfByggerUrl").getString(),
        maxRetries = environment.config.propertyOrNull("brevbaker.pdfByggerMaxRetries")?.getString()?.toInt() ?: 30,
    )

    configureMetrics()
    brevbakerRouting(jwtConfigs.map { it.name }.toTypedArray(), latexCompilerService)
}