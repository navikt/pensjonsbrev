package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.slf4j.LoggerFactory

class BrevmetadataService(config: Config) {
    private val brevmetadataUrl = config.getString("url")
    private val logger = LoggerFactory.getLogger(BrevmetadataService::class.java)
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

    suspend fun getRedigerbareBrev(sakstype: String): List<LetterMetadata> {
        val httpResponse = httpClient.get("/api/brevdata/brevdataForSaktype/$sakstype?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }

        if (httpResponse.status.isSuccess()) {
            return httpResponse.body<List<BrevdataDto>>()
                .filter { it.redigerbart }
                .filter {it.brevkategori != BrevdataDto.BrevkategoriCode.VEDTAK}
                .map{ it.mapToMetadata() }
        } else {
            logger.error("Feil ved henting av brevmetadata. Status: ${httpResponse.status} Message: ${httpResponse.bodyAsText()}")
            return emptyList()
        }
    }

    private fun BrevdataDto.mapToMetadata() =
        LetterMetadata(
            name = dekode,     // TODO handle missing fields in front-end instead.
            id = brevkodeIBrevsystem ?: "MissingCode",
            spraak = sprak ?: emptyList(),
            brevsystem = when (brevsystem) {
                BrevdataDto.BrevSystem.DOKSYS -> BrevSystem.DOKSYS
                BrevdataDto.BrevSystem.GAMMEL -> BrevSystem.EXTREAM
            },
            brevkategoriCode = this.brevkategori
        )


    suspend fun getEblanketter(): List<LetterMetadata> {
        return httpClient.get("/api/brevdata/allBrev?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }.body<List<BrevdataDto>>()
            .filter { it.redigerbart }
            .filter { it.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT }
            .map { it.mapToMetadata() }
    }

    suspend fun getMal(brevkode: String): BrevdataDto {
        return httpClient.get("/api/brevdata/brevForBrevkode/${brevkode}") {
            contentType(ContentType.Application.Json)
        }.body<BrevdataDto>()
    }
}

data class BrevdataDto(
    val redigerbart: Boolean,
    val dekode: String,
    val brevkategori: BrevkategoriCode?,
    val dokType: DokumentType,
    val sprak: List<SpraakKode>?,
    val visIPselv: Boolean?,
    val utland: String?,
    val brevregeltype: String?,
    val brevkravtype: String?,
    val brevkontekst: String?,
    val dokumentkategori: DokumentkategoriCode,
    val synligForVeileder: Boolean?,
    val prioritet: Int?,
    val brevkodeIBrevsystem: String?,
    val brevsystem: BrevSystem,
    val brevgruppe: String?,
) {
    enum class DokumentkategoriCode { B, E_BLANKETT, IB, SED, VB }
    enum class BrevkategoriCode { BREV_MED_SKJEMA, INFORMASJON, INNHENTE_OPPL, NOTAT, OVRIG, VARSEL, VEDTAK }
    enum class BrevSystem {
        DOKSYS,
        GAMMEL,     //EXTREAM
    }

    enum class DokumentType {
        I, //Inngende dokument
        N, //Notat
        U, //Utgende dokument
    }
}


enum class SpraakKode {
    EN, // Engelsk
    NB, // Bokmaal
    NN, // Nynorsk
    FR, // Fransk
    SE, // Nord-samisk
}

enum class BrevSystem { EXTREAM, DOKSYS, BREVBAKER }

data class LetterMetadata(
    val name: String,
    val id: String,
    val brevsystem: BrevSystem,
    val spraak: List<SpraakKode>, // Enkelte brev er egentlig bare bokmål, men har null i metadata.
    val brevkategoriCode: BrevdataDto.BrevkategoriCode?
)

