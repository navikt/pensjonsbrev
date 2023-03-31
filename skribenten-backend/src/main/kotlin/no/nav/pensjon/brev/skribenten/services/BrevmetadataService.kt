package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*

data class BrevdataDto(
    val redigerbart: Boolean?,
    val dekode: String?,
    val brevkategori: String?,
    val dokType: String?,
    val sprak: List<String>?,
    val visIPselv: Boolean?,
    val utland: String?,
    val brevregeltype: String?,
    val brevkravtype: String?,
    val brevkontekst: String?,
    val dokumentkategori: String?,
    val synligForVeileder: Boolean?,
    val prioritet: Int?,
    val brevkodeIBrevsystem: String?,
    val brevsystem: String?,
)


data class LetterTemplateInfo(
    val categories: List<LetterCategory>,
    val favourites: List<LetterMetadata>,
)

data class LetterCategory(
    val name: String,
    val templates: List<LetterMetadata>,
)

data class LetterMetadata(
    val name: String,
    val id: String,
)


class BrevmetadataService(config: Config) {
    private val brevmetadataUrl = config.getString("url")
    private val httpClient = HttpClient(CIO) {
        defaultRequest {
            url(brevmetadataUrl)
        }
        install(ContentNegotiation) {
            jackson{
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    //TODO get saksinfo from pen for å bestemme filterer for sakstyper.
    suspend fun getRedigerbareBrev(): List<LetterCategory> {
        val metadata: List<BrevdataDto> = httpClient.get("/api/brevdata/brevdataForSaktype/UFOREP?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }.body()
        return metadata
            .filter { it.redigerbart ?: false }
            .groupBy {
                it.brevkategori
            }.map {
                LetterCategory(
                    name = it.key ?: "Annet",
                    templates = it.value.map { template ->
                        LetterMetadata(
                            name = template.dekode ?: "MissingName",
                            id = template.brevkodeIBrevsystem ?: "MissingCode",
                        )
                    }
                )
            }
    }
}