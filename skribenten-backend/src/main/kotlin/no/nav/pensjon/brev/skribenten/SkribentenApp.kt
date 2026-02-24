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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nav.brev.BrevExceptionDto
import no.nav.pensjon.brev.skribenten.Metrics.configureMetrics
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.serialize.BrevkodeJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.EditLetterJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import no.nav.pensjon.brev.skribenten.serialize.SakstypeModule
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException
import no.nav.pensjon.brev.skribenten.services.BrevredigeringException.*
import no.nav.pensjon.brev.skribenten.services.P1Exception
import no.nav.pensjon.brev.skribenten.services.PenDataException
import no.nav.pensjon.brev.skribenten.services.ServiceException
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.skribenten.SkribentenApp")

fun main() {
    try {
        run()
    } catch (e: Exception) {
        logger.error(e.message, e)
        throw e
    }
}

private fun run() {
    val skribentenConfig: Config =
        ConfigFactory.load(ConfigParseOptions.defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
            .resolveWith(
                ConfigFactory.load("azuread"),
                ConfigResolveOptions.defaults().setAllowUnresolved(true)
            ) // loads azuread secrets for local
            .resolveWith(ConfigFactory.load("unleash"))
            .getConfig("skribenten")

    ADGroups.init(skribentenConfig.getConfig("groups"))
    KrypteringService.init(skribentenConfig.getString("krypteringsnoekkel"))

    embeddedServer(
        Netty,
        configure = {
            connectors.add(EngineConnectorBuilder().apply {
                host = "0.0.0.0"
                port = skribentenConfig.getInt("port")
            })
            shutdownGracePeriod = 25.seconds.inWholeMilliseconds
            shutdownTimeout = 29.seconds.inWholeMilliseconds
        },
    ) {
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
        mdc("x_userId") { call ->
            call.principal<JwtUserPrincipal>()?.navIdent?.id
        }
    }
    install(CallId) {
        header("X-Request-ID")
        generate()
        verify { it.isNotEmpty() }
    }

    install(StatusPages) {
        exception<JacksonException> { call, cause ->
            logger.info("Bad request, kunne ikke deserialisere json")
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Failed to deserialize json body: unknown cause")
        }
        exception<UnauthorizedException> { call, cause ->
            logger.info(cause.message, cause)
            call.respond(HttpStatusCode.Unauthorized, cause.msg)
        }
        exception<BadRequestException> { call, cause ->
            if (cause.cause is JsonConvertException) {
                logger.info(cause.message, cause)
                call.respond(
                    HttpStatusCode.BadRequest,
                    cause.cause?.message ?: "Failed to deserialize json body: unknown reason"
                )
            } else {
                logger.info("Bad request, men ikke knytta til deserialisering")
                call.respond(HttpStatusCode.BadRequest, cause.message ?: "Bad request exception")
            }
        }
        exception<BrevredigeringException> { call, cause ->
            logger.info(cause.message, cause)
            when (cause) {
                is ArkivertBrevException -> call.respond(HttpStatusCode.Conflict, cause.message)
                is BrevIkkeKlartTilSendingException -> call.respond(HttpStatusCode.UnprocessableEntity,
                    BrevExceptionDto(tittel = "Brev ikke klart", melding = cause.message))
                is NyereVersjonFinsException -> call.respond(HttpStatusCode.BadRequest, cause.message)
                is KanIkkeReservereBrevredigeringException -> call.respond(HttpStatusCode.Locked, cause.response)
                is BrevmalFinnesIkke -> call.respond(HttpStatusCode.InternalServerError, cause.message)
            }
        }
        exception<P1Exception> { call, cause ->
            logger.info(cause.message, cause)
            when(cause) {
                is P1Exception.ManglerDataException -> call.respond(HttpStatusCode.UnprocessableEntity, cause.message)
            }
        }
        exception<PenDataException> { call, cause ->
            logger.info("${cause.status} - Feil ved oppdatering av brev: ${cause.message}")
            call.respond(status = cause.status, cause.feil)
        }
        exception<ServiceException> { call, cause ->
            logger.error(cause.message, cause)
            call.respond(status = cause.status, message = cause.message)
        }
        exception<Exception> { call, cause ->
            logger.error(cause.message, cause)
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Ukjent intern feil")
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
            val schemes = it.getStringList("schemes")
            it.getString("host").split(",").forEach { host ->
                allowHost(host, schemes = schemes)
            }
        }
    }

    val valkeyConfig = skribentenConfig.getConfig("valkey")
    val cache = if (valkeyConfig.getBoolean("enabled")) {
        Valkey(valkeyConfig)
    } else {
        log.warn("Valkey is disabled, this is not recommended for production")
        InMemoryCache()
    }

    val azureADConfig = skribentenConfig.requireAzureADConfig()
    install(Authentication) {
        skribentenJwt(azureADConfig)
    }
    configureRouting(azureADConfig, skribentenConfig, cache)
    configureMetrics()

    monitor.subscribe(ServerReady) {
        launch {
            delay(5.minutes)
            oneShotJobs(skribentenConfig) {
                // Sett opp evt. jobber her
            }
        }
    }

    monitor.subscribe(ApplicationStopPreparing) {
        Features.shutdown()
    }
}

fun Application.skribentenContenNegotiation() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            registerModule(EditLetterJacksonModule)
            registerModule(BrevkodeJacksonModule)
            registerModule(SakstypeModule)
            registerModule(LetterMarkupJacksonModule)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
}