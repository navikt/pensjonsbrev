package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import no.nav.pensjon.brev.skribenten.OboClientConfig
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.core.Closeable
import kotlinx.io.IOException
import no.nav.pensjon.brev.skribenten.SkribentenConfig
import no.nav.pensjon.brev.skribenten.auth.AuthService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.services.HttpClientFactory.lagHttpClient
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Pid
import org.slf4j.LoggerFactory
import java.nio.channels.UnresolvedAddressException

class KrrService(config: OboClientConfig, authService: AuthService, engine: HttpClientEngine) : ServiceStatus, Closeable {

    @Suppress("unused") // Brukes av ktor-di
    constructor(config: SkribentenConfig, authService: AuthService, engine: HttpClientEngine): this(config.services.krr, authService, engine)

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val client = lagHttpClient(engine) {
        defaultRequest {
            url(config.url)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
        installRetry(logger)
        callIdAndOnBehalfOfClient(config.scope, authService)
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
        val response = try {
            client.post("/rest/v1/personer") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(KontaktinfoRequest(listOf(pid.value)))
            }
        } catch (e: IOException) {
            logger.warn("IO-feil ved kall mot KRR: ${e.message}", e)
            return KontaktinfoResponse(KontaktinfoResponse.FailureType.ERROR)
        } catch (e: UnresolvedAddressException) {
            logger.warn("IO-feil ved kall mot KRR: ${e.message}, e")
            return KontaktinfoResponse(KontaktinfoResponse.FailureType.ERROR)
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

    override fun close() { client.close() }
}
