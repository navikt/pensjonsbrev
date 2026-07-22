package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import io.ktor.utils.io.core.Closeable
import no.nav.pensjon.brev.skribenten.FoerstesideConfig
import no.nav.pensjon.brev.skribenten.SkribentenConfig
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.fagsystem.domain.Tema
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brev.skribenten.services.ServiceException
import no.nav.pensjon.brev.skribenten.services.callIdAndOnBehalfOfClient
import no.nav.pensjon.brev.skribenten.services.installRetry
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import org.slf4j.LoggerFactory

class FoerstesidegeneratorClient(config: FoerstesideConfig, authService: AuthService, clientEngine: HttpClientEngine = CIO.create()) : Closeable {

    @Suppress("unused") // Brukes av ktor-di
    constructor(config: SkribentenConfig, authService: AuthService) : this(config.services.foerstesideConfig, authService)
    private val logger = LoggerFactory.getLogger(javaClass)

    private val foerstesidegeneratorUrl = config.url
    private val foerstesidegeneratorScope = config.scope

    private val client = lagHttpClient(clientEngine) {
        defaultRequest {
            url(foerstesidegeneratorUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        installRetry(logger)
        callIdAndOnBehalfOfClient(foerstesidegeneratorScope, authService)

    }

    override fun close() {
        client.close()
    }

    suspend fun genererFoersteside(request: GenererFoerstesideRequest): GenererFoerstesideResponse {
        val response = client.post("/api/foerstesidegenerator/v1/foersteside") {
            setBody(request)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("Nav-Consumer-Id", "skribenten-backend")
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw FoerstesidegeneratorException("Klarte ikke lage førsteside for brev i sak ${request.arkivsak.arkivsaksnummer}: statuskode ${response.status}")
        }
    }

    class FoerstesidegeneratorException(message: String) : ServiceException(message)

    // Feltene som settes til null her er de vi tror vi ikke trenger å kunne sette
    // Forklaringa på feltene: https://confluence.adeo.no/spaces/BOA/pages/315211732/Tjeneste+for+%C3%A5+generere+f%C3%B8rsteside+til+skanning
    data class GenererFoerstesideRequest(
        val spraakkode: Spraakkode,
        val adresse: Any? = null, // Vi bruker ikke adresse her, så dropper for nå å modellere det
        val netsPostboks: Postboks,
        val avsender: Avsender? = null,
        val bruker: Bruker,
        val ukjentBrukerPersoninfo: String? = null,
        val tema: Tema,
        val behandlingstema: String? = null,
        val arkivtittel: String,
        val vedleggsliste: List<String>,
        val navSkjemaId: String? = null,
        val overskriftstittel: String,
        val dokumentlisteFoersteside: List<String>,
        val foerstesidetype: Foerstesidetype,
        val enhetsnummer: EnhetId,
        val arkivsak: Arkivsak,
    )

    data class GenererFoerstesideResponse(
        val foersteside: ByteArray,
        val loepenummer: Loepenummer,
    )

    @JvmInline
    value class Loepenummer(val value: String)

    @JvmInline
    value class Postboks(val value: String) {
        init {
            require(value.toIntOrNull() != null)
        }
    }

    data class Avsender(
        val avsenderId: BrevbakerType.Pid, // TODO, kan denne også vera orgid?
        val avsenderNavn: String,
    )

    data class Bruker(
        val brukerId: BrevbakerType.Pid,
        val brukerType: BrukerType,
    ) {
        enum class BrukerType {
            PERSON, ORGANISASJON
        }
    }

    enum class Foerstesidetype {
        ETTERSENDELSE,
        LOESPOST,
        SKJEMA,
        NAV_INTERN
    }

    data class Arkivsak(
        val arkivsaksystem: Arkivsaksystem,
        val arkivsaksnummer: SaksId,
    ) {
        enum class Arkivsaksystem {
            PSAK,
        }
    }

    enum class Spraakkode {
        NB, NN, EN
    }
}