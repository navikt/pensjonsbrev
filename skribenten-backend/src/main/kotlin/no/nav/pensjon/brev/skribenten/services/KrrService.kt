package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import no.nav.pensjon.brev.skribenten.auth.AzureADService
import org.slf4j.LoggerFactory

class KrrService(config: Config, authService: AzureADService): ServiceStatus {
    private val krrUrl = config.getString("url")
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val client = HttpClient(CIO) {
        defaultRequest {
            url(krrUrl)
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
    data class KontaktinfoKRRResponse(val personer: Map<String, KontaktinfoKRRResponseEnkeltperson>, val feil: Map<String, Feiltype>)


    data class KontaktinfoResponse(val spraakKode: SpraakKode?, val failure: FailureType?) {
        constructor(failure: FailureType) : this(null, failure)
        constructor(spraakKode: SpraakKode?) : this(spraakKode, null)

        enum class FailureType {
            NOT_FOUND,
            ERROR,
        }
    }

    data class KontaktinfoRequest(val personident: String)

    suspend fun getPreferredLocale(pid: String): KontaktinfoResponse {
        return client.post("/rest/v1/personer") {
            headers {
                accept(ContentType.Application.Json)
                setBody(KontaktinfoRequest(pid))
            }
        }.toServiceResult<KontaktinfoKRRResponse>()
            .map { response ->
                if (response.feil.isEmpty()) {
                    KontaktinfoResponse(
                        when (response.personer[pid]!!.spraak) {
                            KontaktinfoKRRResponseEnkeltperson.SpraakKode.nb -> SpraakKode.NB
                            KontaktinfoKRRResponseEnkeltperson.SpraakKode.nn -> SpraakKode.NN
                            KontaktinfoKRRResponseEnkeltperson.SpraakKode.en -> SpraakKode.EN
                            KontaktinfoKRRResponseEnkeltperson.SpraakKode.se -> SpraakKode.SE
                            null -> null
                        }
                    )
                } else {
                    val feilen = response.feil[pid]!!
                    KontaktinfoResponse(
                        failure = when(feilen) {
                            Feiltype.person_ikke_funnet -> KontaktinfoResponse.FailureType.NOT_FOUND
                            Feiltype.fortrolig_adresse,
                            Feiltype.strengt_fortrolig_adresse,
                            Feiltype.strengt_fortrolig_utenlandsk_adresse,
                            Feiltype.skjermet,
                            Feiltype.noen_andre -> KontaktinfoResponse.FailureType.ERROR
                        }
                    )
                }
            }.catch { message, status ->
                KontaktinfoResponse(KontaktinfoResponse.FailureType.ERROR).also { logger.error("Feil ved henting av kontaktinformasjon. Status: $status Melding: $message") }
            }
    }

    override val name = "KRR"
    override suspend fun ping(): ServiceResult<Boolean> =
        client.get("/internal/health/readiness")
            .toServiceResult<String>()
            .map { true }
}