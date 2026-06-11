package no.nav.pensjon.brev.skribenten.foerstesidegenerator

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.ServiceException
import no.nav.pensjon.brev.skribenten.services.callIdAndOnBehalfOfClient
import no.nav.pensjon.brev.skribenten.services.installRetry
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import org.slf4j.LoggerFactory

class FoerstesidegeneratorClient(config: Config, authService: AuthService) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val foerstesidegeratorUrl = config.getString("url")
    private val foerstesidegeratorScope = config.getString("scope")

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(foerstesidegeratorUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
        installRetry(logger)
        callIdAndOnBehalfOfClient(foerstesidegeratorScope, authService)

    }

    suspend fun genererFoersteside(request: GenererFoerstesideRequest): GenererFoerstesideResponse {
        val response = client.post("/api/foerstesidegenerator/v1/foersteside") {
            setBody(request)
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Pdf)
            header("Nav-Consumer-Id", "skribenten-backend")
        }

        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw FoerstesidegeneratorException("Klarte ikke lage førsteside for ${request.arkivtittel}: ${response.status} - ${response.bodyAsText()}")
        }
    }

    class FoerstesidegeneratorException(message: String) : ServiceException(message)
    data class GenererFoerstesideRequest(
        val spraakkode: SpraakKode,
        val adresse: Any? = null,
        val netsPostboks: Postboks,
        val avsender: Avsender? = null,
        val bruker: Bruker,
        val ukjentBrukerPersoninfo: String? = null,
        val tema: Tema,
        val behandlingstema: String?, // TODO: korleis finn vi denne?
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
        val avsenderId: BrevbakerType.Pid, // TODO, kan også vera orgid
        val avsenderNavn: String
    )

    data class Bruker(
        val brukerId: BrevbakerType.Pid, // TODO, kan også vera orgid
        val brukerType: BrukerType,
    ) {
        enum class BrukerType {
            PERSON, ORGANISASJON
        }
    }

    enum class Tema {
        FOR; // TODO: kva har vi for pensjon eller uføre her? FOR er Foreldrepenger
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
    )

    enum class Arkivsaksystem {
        PSAK,
    }
}