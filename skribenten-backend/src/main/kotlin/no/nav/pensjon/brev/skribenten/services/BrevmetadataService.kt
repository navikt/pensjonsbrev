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
            .filter { it.redigerbart }
            .groupBy { it.brevkategori }
            .map {
                LetterCategory(
                    name = it.key?.toString() ?: "Annet",
                    templates = it.value.map { template -> template.mapToMetadata() }
                )
            }

    private fun BrevdataDto.mapToMetadata() =
        LetterMetadata(
            name = dekode ?: "MissingName",     // TODO handle missing fields in front-end instead.
            id = brevkodeIBrevsystem ?: "MissingCode",
            spraak = sprak ?: emptyList(),
            brevsystem = when (brevsystem) {
                "DOKSYS" -> BrevSystem.DOKSYS
                "GAMMEL" -> BrevSystem.EXTREAM
                // TODO handle state or throw something else.
                else -> throw IllegalStateException("Malformed metadata. Must be doksys or extream.")
            },
            isVedtaksbrev = this.brevkategori == BrevdataDto.BrevkategoriCode.VEDTAK,
            isEblankett = this.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT,
        )


    suspend fun getEblanketter(): List<LetterMetadata> {
        return httpClient.get("/api/brevdata/allBrev?includeXsd=false") {
                contentType(ContentType.Application.Json)
            }.body<List<BrevdataDto>>()
                .filter { it.redigerbart }
                .filter { it.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT }
            .map { it.mapToMetadata() }
    }
}

data class BrevdataDto(
    val redigerbart: Boolean,
    val dekode: String?,
    val brevkategori: BrevkategoriCode?,
    val dokType: String?,
    val sprak: List<SpraakKode>?,
    val visIPselv: Boolean?,
    val utland: String?,
    val brevregeltype: String?,
    val brevkravtype: String?,
    val brevkontekst: String?,
    val dokumentkategori: DokumentkategoriCode?,
    val synligForVeileder: Boolean?,
    val prioritet: Int?,
    val brevkodeIBrevsystem: String?,
    val brevsystem: String?,
) {
    enum class DokumentkategoriCode { B, EP, ES, E_BLANKETT, F, IB, IS, KD, KM, KS, SED, TS, VB }
    enum class BrevkategoriCode { BREV_MED_SKJEMA, INFORMASJON, INNHENTE_OPPL, NOTAT, OVRIG, VARSEL, VEDTAK }
}


enum class SpraakKode {
    EN, // Engelsk
    NB, // Bokmaal
    NN, // Nynorsk
    FR, // Fransk
    SE, // Nord-samisk
}


data class LetterCategory(
    val name: String,
    val templates: List<LetterMetadata>,
)

enum class BrevSystem { EXTREAM, DOKSYS, BREVBAKER }

data class LetterMetadata(
    val name: String,
    val id: String,
    val brevsystem: BrevSystem,
    val spraak: List<SpraakKode>,
    val isVedtaksbrev: Boolean,
    val isEblankett: Boolean,
)

