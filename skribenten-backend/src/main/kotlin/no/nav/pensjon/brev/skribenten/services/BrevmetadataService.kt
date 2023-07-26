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

class BrevmetadataService(config: Config) {
    private val brevmetadataUrl = config.getString("url")
    private val httpClient = HttpClient(CIO) {
        defaultRequest {
            url(brevmetadataUrl)
        }
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    suspend fun getRedigerbareBrevKategorier(sakstype: String): List<LetterCategory> {
        val metadata: List<BrevdataDto> =
            httpClient.get("/api/brevdata/brevdataForSaktype/$sakstype?includeXsd=false") {
                contentType(ContentType.Application.Json)
            }.body()
        return mapToCategories(metadata)
    }

    private fun mapToCategories(metadata: List<BrevdataDto>) =
        metadata
            .filter { it.redigerbart ?: false }
            .groupBy { it.brevkategori }
            .map {
                LetterCategory(
                    name = it.key ?: "Annet",
                    templates = it.value.map { template -> template.mapToMetadata() }
                )
            }

    private fun BrevdataDto.mapToMetadata() =
        LetterMetadata(
            name = dekode ?: "MissingName",
            id = brevkodeIBrevsystem ?: "MissingCode",
            spraak = sprak ?: emptyList(),
            brevsystem = when (brevsystem) {
                "DOKSYS" -> BrevSystem.DOKSYS
                "GAMMEL" -> BrevSystem.EXTERAM
                else -> throw IllegalStateException("Malformed metadata. Must be doksys or extream.")
            }
        )


    suspend fun getRedigerbareBrev(): List<LetterMetadata> {
        val metadata: List<BrevdataDto> = httpClient.get("/api/brevdata/allbrev/?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }.body()
        return metadata.filter { it.redigerbart ?: false }.map { it.mapToMetadata() }
    }
}

data class BrevdataDto(
    val redigerbart: Boolean?,
    val dekode: String?,
    val brevkategori: String?,
    val dokType: String?,
    val sprak: List<SpraakKode>?,
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


enum class SpraakKode {
    EN, // Engelsk
    FR, // Fransk
    NB, // Bokml
    NN, // Nynorsk
    SE // Nordsamisk
}


data class LetterCategory(
    val name: String,
    val templates: List<LetterMetadata>,
)

enum class BrevSystem { EXTERAM, DOKSYS, BREVBAKER }

data class LetterMetadata(
    val name: String,
    val id: String,
    val brevsystem: BrevSystem,
    val spraak: List<SpraakKode>?
)

