package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions
import com.typesafe.config.ConfigResolveOptions
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import no.nav.pensjon.brev.skribenten.Metrics.configureMetrics
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UnauthorizedException
import no.nav.pensjon.brev.skribenten.auth.requireAzureADConfig
import no.nav.pensjon.brev.skribenten.auth.skribentenJwt
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.routes.BrevkodeModule
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.LetterMarkupModule


fun main() {
    val skribentenConfig: Config =
        ConfigFactory.load(ConfigParseOptions.defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
            .resolveWith(ConfigFactory.load("azuread"), ConfigResolveOptions.defaults().setAllowUnresolved(true)) // loads azuread secrets for local
            .resolveWith(ConfigFactory.load("unleash"))
            .getConfig("skribenten")

    ADGroups.init(skribentenConfig.getConfig("groups"))

    embeddedServer(Netty, port = skribentenConfig.getInt("port"), host = "0.0.0.0") {
        skribentenApp(skribentenConfig)
    }.start(wait = true)
}

fun Application.skribentenApp(skribentenConfig: Config) {
    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        val ignorePaths = setOf("/isAlive", "/isReady", "/metrics")
        filter {
            !ignorePaths.contains(it.request.path())
        }
    }
    install(CallId) {
        header("X-Request-ID")
        generate()
        verify { it.isNotEmpty() }
    }

    install(StatusPages) {
        exception<JacksonException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown cause")
        }
        exception<UnauthorizedException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.msg)
        }
        exception<BadRequestException> { call, cause ->
            if (cause.cause is JsonConvertException) {
                call.application.log.info(cause.message, cause)
                call.respond(
                    HttpStatusCode.BadRequest,
                    cause.cause?.message ?: "Failed to deserialize json body: unknown reason"
                )
            } else {
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "Bad request exception")
            }
        }
        exception<BrevredigeringException> { call, cause ->
            if (cause is KanIkkeReservereBrevredigering) {
                call.respond(
                    cause.type.statusCode,
                    Api.ErrorResponse.Brevredigering(cause.type, cause.message, cause.response)
                )
            } else {
                call.respond(
                    cause.type.statusCode,
                    Api.ErrorResponse.Brevredigering(cause.type, cause.message)
                )
            }
        }
        exception<Exception> { call, cause ->
            call.application.log.error(cause.message, cause)
            call.respond(HttpStatusCode.InternalServerError, "Ukjent intern feil")
        }
    }

    skribentenContenNegotiation()

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("X-Request-ID")
        skribentenConfig.getConfig("cors").also {
            allowHost(it.getString("host"), schemes = it.getStringList("schemes"))
        }
    }

    val azureADConfig = skribentenConfig.requireAzureADConfig()
    install(Authentication) {
        skribentenJwt(azureADConfig)
    }
    configureRouting(azureADConfig, skribentenConfig)
    configureMetrics()

    monitor.subscribe(ApplicationStopPreparing) {
        Features.shutdown()
    }
}

fun Application.skribentenContenNegotiation() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            registerModule(Edit.JacksonModule)
            registerModule(BrevkodeModule)
            registerModule(LetterMarkupModule)
            registerModule(PrimitiveModule)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
}