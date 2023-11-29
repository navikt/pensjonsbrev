package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.callId
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class KodeverkService(config: Config) {
    private val kodeverkServiceUrl = config.getString("url")

    val client = HttpClient(CIO) {
        defaultRequest {
            url(kodeverkServiceUrl)
        }
        install(ContentNegotiation) {
            jackson {
                registerModule(JavaTimeModule())
            }
        }
    }

    private data class KodeverkResponse(val betydninger: Map<String, List<Resultat>>) {
        data class Resultat(
            val gyldigFra: LocalDate,
            val gyldigTil: LocalDate,
            val beskrivelser: Map<String, Besrkivelse>
        ) {
            data class Besrkivelse(val term: String, val tekst: String)
        }
    }

    data class KommuneResultat(val kommunenavn: String, val kommunenummer: List<String>)

    suspend fun getKommuner(call: ApplicationCall): List<KommuneResultat> {
        val kommuner = getKommunenummer(call)
        return kommuner.betydninger.flatMap { kommuneNrEntry ->
            kommuneNrEntry.value.mapNotNull { beskrivelse ->
                beskrivelse.beskrivelser["nb"]?.let {
                    it.tekst to kommuneNrEntry.key
                }
            }
        }.groupBy({ it.first }, { it.second })
            .map { KommuneResultat(it.key, it.value) }
    }

    private suspend fun getKommunenummer(call: ApplicationCall): KodeverkResponse {
        val dateString = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))
        return client.get("Kommuner/koder/betydninger?ekskluderUgyldige=true&oppslagsdato=$dateString&spraak=nb") {
            headers {
                callId(call)
                append("Nav-Consumer-Id", "skribenten-backend-lokal")
            }
        }.body<KodeverkResponse>()
    }
}