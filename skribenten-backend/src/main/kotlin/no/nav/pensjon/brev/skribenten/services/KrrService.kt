package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.jackson.jackson
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Pid
import org.slf4j.LoggerFactory

class KrrService(config: Config, authService: AuthService, engine: HttpClientEngine = CIO.create()) : ServiceStatus {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val client = HttpClient(engine) {
        defaultRequest {
            url(config.getString("url"))
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
        callIdAndOnBehalfOfClient(config.getString("scope"), authService)
    }

    @Suppress("EnumEntryName")
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KontaktinfoKRRResponseEnkeltperson(val spraak: SpraakKode? = null) {
        enum class SpraakKode {
            nb, // bokmål
            nn, //nynorsk
            en, //engelsk
            se, //nord-samisk
        }
    }

    @Suppress("EnumEntryName")
    // henta fra https://github.com/navikt/digdir-krr/wiki/Migrere-vekk-fra-GET%E2%80%90tjenesten-for-enkeltoppslag
    enum class Feiltype {
        person_ikke_funnet,
        skjermet,
        fortrolig_adresse,
        strengt_fortrolig_adresse,
        strengt_fortrolig_utenlandsk_adresse,
        noen_andre
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KontaktinfoKRRResponse(val personer: Map<Pid, KontaktinfoKRRResponseEnkeltperson>, val feil: Map<Pid, Feiltype>)

    private data class KontaktinfoRequest(val personidenter: List<String>)

    data class KontaktinfoResponse(val spraakKode: SpraakKode?, val failure: FailureType?) {
        constructor(failure: FailureType) : this(null, failure)
        constructor(spraakKode: SpraakKode?) : this(spraakKode, null)

        enum class FailureType {
            NOT_FOUND,
            ERROR,
        }
    }

    suspend fun getPreferredLocale(pid: Pid): KontaktinfoResponse {
        val response = client.post("/rest/v1/personer") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(KontaktinfoRequest(listOf(pid.value)))
        }
        return if (response.status.isSuccess()) {
            val body = response.body<KontaktinfoKRRResponse>()

            if (body.feil.isEmpty()) {
                KontaktinfoResponse(
                    when (body.personer[pid]?.spraak) {
                        KontaktinfoKRRResponseEnkeltperson.SpraakKode.nb -> SpraakKode.NB
                        KontaktinfoKRRResponseEnkeltperson.SpraakKode.nn -> SpraakKode.NN
                        KontaktinfoKRRResponseEnkeltperson.SpraakKode.en -> SpraakKode.EN
                        KontaktinfoKRRResponseEnkeltperson.SpraakKode.se -> SpraakKode.SE
                        null -> null
                    }
                )
            } else {
                KontaktinfoResponse(
                    failure = when (body.feil[pid]) {
                        Feiltype.person_ikke_funnet -> KontaktinfoResponse.FailureType.NOT_FOUND
                        Feiltype.fortrolig_adresse,
                        Feiltype.strengt_fortrolig_adresse,
                        Feiltype.strengt_fortrolig_utenlandsk_adresse,
                        Feiltype.skjermet,
                        Feiltype.noen_andre,
                        null -> KontaktinfoResponse.FailureType.ERROR
                    }
                )
            }
        } else {
            logger.error("Feil ved henting av kontaktinformasjon. Status: ${response.status} Melding: ${response.bodyAsText()}")
            KontaktinfoResponse(KontaktinfoResponse.FailureType.ERROR)
        }
    }

    override suspend fun ping() =
        ping("KRR") { client.get("/internal/health/readiness") }
}
