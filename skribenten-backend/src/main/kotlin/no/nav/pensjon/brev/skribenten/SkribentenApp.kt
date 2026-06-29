package no.nav.pensjon.brev.skribenten

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.getAs
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.di.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.*
import no.nav.pensjon.brev.skribenten.Metrics.configureMetrics
import no.nav.pensjon.brev.skribenten.auth.*
import no.nav.pensjon.brev.skribenten.common.*
import no.nav.pensjon.brev.skribenten.db.*
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.serialize.*
import no.nav.pensjon.brev.skribenten.services.*
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.minutes

private val logger = LoggerFactory.getLogger("no.nav.pensjon.brev.skribenten.SkribentenApp")

fun main(args: Array<String>) = try {
    EngineMain.main(args)
} catch (e: Exception) {
    logger.error(e.message, e)
    throw e
}

// Er satt i application.conf slik at EngineMain kaller på skribentenApp.
@Suppress("unused")
suspend fun Application.skribentenApp() {
    val skribentenConfig = environment.config.config("skribenten").getAs<SkribentenConfig>()
    val skribentenConfigOld = ConfigFactory.parseMap(environment.config.config("skribenten").toMap())
    ADGroups.init(skribentenConfigOld.getConfig("groups"))
    KrypteringService.init(skribentenConfigOld.getString("krypteringsnoekkel"))

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
        exception<Dto2ApiService.BrevmalFinnesIkke> { call, cause ->
            logger.info(cause.message, cause)
            call.respond(HttpStatusCode.InternalServerError, cause.message)
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
        exception<PenFeilIDatabyggerException> { call, cause ->
            logger.info("${cause.status} - Feil ved henting av brevdata: ${cause.message}")
            call.respond(status = cause.status, "Teknisk feil ved henting av brevdata, prøv igjen litt senere")

        }
        exception<ServiceException> { call, cause ->
            logger.error(cause.message, cause)
            call.respond(status = cause.status, message = cause.message)
        }
        exception<Exception> { call, cause ->
            cleanSensitiveDataAndLog(cause)
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
        skribentenConfigOld.getConfig("cors").also {
            val schemes = it.getStringList("schemes")
            it.getString("host").split(",").forEach { host ->
                allowHost(host, schemes = schemes)
            }
        }
    }

    install(Authentication) {
        skribentenJwt(skribentenConfig.azureAD)
    }

    dependencies {
        provide { skribentenConfig }

        provide<Cache>(::cacheFactory)
        provide<AuthService>(AzureADService::class)
        provide<HikariDataSource>(::dataSourceFactory)
        provide<FeatureToggleService>(UnleashService::class)
    }

    launch {
        Database.connect(dependencies.resolve<HikariDataSource>())
        databaseReady.set(true)
    }
    Features.init(dependencies.resolve())

    val cache: Cache by dependencies
    configureRouting(skribentenConfig.azureAD, skribentenConfigOld, cache)
    configureMetrics()

    monitor.subscribe(ServerReady) {
        launch {
            delay(5.minutes)
            oneShotJobs(skribentenConfigOld) {
                job("2026-06-24-document-vedlegghash") {
                    val dokumentIder = transaction {
                        DocumentTable.select(DocumentTable.id).map { it[DocumentTable.id].value }
                    }
                    dokumentIder.chunked(40).forEach { idChunk ->
                        transaction {
                            DocumentEntity.forEntityIds(idChunk.map { EntityID(it, DocumentTable) }).forEach { document ->
                                document.vedleggHash = document.brevredigering.vedleggHash
                            }
                        }
                    }
                }
                // Sett opp evt. jobber her
            }
        }
    }

    monitor.subscribe(ApplicationStopping) {
        HttpClientFactory.close()
    }
}

// Vi må gjøre denne vaskingen fordi Exposed sin BasicBinaryColumnType::valueFromDB kaster en IllegalStateException med toString av Edit.Letter.
// Meldt inn til exposed: https://youtrack.jetbrains.com/issue/EXPOSED-1012
private fun cleanSensitiveDataAndLog(cause: Exception) {
    if (cause is IllegalStateException && cause.messageHasEditedLetter()) {
        val cleansedException = IllegalStateException(
            "Unexpected value ***stripped sensitive Edit.Letter value*** of type ${Edit.Letter::class.qualifiedName}",
            cause.cause
        ).apply {
            stackTrace = cause.stackTrace
        }
        logger.error(cleansedException.message, cleansedException)
    } else {
        logger.error(cause.message, cause)
    }
}

private fun IllegalStateException.messageHasEditedLetter(): Boolean = message?.let { msg ->
    msg.startsWith("Unexpected value") && msg.endsWith("of type ${Edit.Letter::class.qualifiedName}")
} ?: false

fun Application.skribentenContenNegotiation() {
    install(ContentNegotiation) {
        jackson {
            skribentenServerJackson()
        }
    }
}

fun ObjectMapper.skribentenServerJackson() = apply {
    registerModule(JavaTimeModule())
    registerMixin(TemplateModelSpecificationMixins)
    registerModule(LetterMarkupJacksonModule)
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}