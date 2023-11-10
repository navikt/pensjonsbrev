package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.headers
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class KodeverkService(config: Config) {
    private val kodeverkServiceUrl = config.getString("url")
    private val logger = LoggerFactory.getLogger(this::class.java)

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
        logger.info("CHECK 1")
        val kommuner = getKommunenummer(call)
        logger.info("CHECK 3")
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
        logger.info("CHECK 2")
        try {
            val dateString = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))
            val response =
                client.get("Kommuner/koder/betydninger?ekskluderUgyldige=true&oppslagsdato=$dateString&spraak=nb") {
                    headers {
                        call.callId?.let { append("Nav-Call-Id", it) }
                        append("Nav-Consumer-Id", "skribenten-backend-lokal")
                    }
                }

            if (response.status == HttpStatusCode.OK) {
                return response.body<KodeverkResponse>()
            } else {
                logger.info("Request failed with status code: ${response.status}")
            }
        } catch (e: ClientRequestException) {
            // Handle exceptions related to the request itself
            println("ClientRequestException: ${e.message}")
        } catch (e: ServerResponseException) {
            // Handle exceptions related to the server's response
            println("ServerResponseException: ${e.message}")
        } catch (e: Exception) {
            // Handle other exceptions
            println("An unexpected error occurred: ${e.message}")
        }

        return KodeverkResponse(betydninger = mapOf("a" to listOf()))
    }
}