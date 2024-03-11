package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.typesafe.config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkontekstCode.*
import org.slf4j.LoggerFactory

class BrevmetadataService(config: Config, clientEngine: HttpClientEngine = CIO.create()) : ServiceStatus {
    private val brevmetadataUrl = config.getString("url")
    private val logger = LoggerFactory.getLogger(BrevmetadataService::class.java)
    private val httpClient = HttpClient(clientEngine) {
        defaultRequest {
            url(brevmetadataUrl)
        }
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            }
        }
    }

    suspend fun getRedigerbareBrev(sakstype: PenService.SakType, isVedtaksKontekst: Boolean): List<LetterMetadata> {
        val httpResponse = httpClient.get("/api/brevdata/brevdataForSaktype/$sakstype?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }

        if (httpResponse.status.isSuccess()) {
            return httpResponse.body<List<BrevdataDto>>()
                .filter { filterForKontekst(it, isVedtaksKontekst) }
                .filter { it.redigerbart } // TODO ikke filtrer ut og legg til støtte for auto-brev. Krever design endringer.
                .map { it.mapToMetadata() }
        } else {
            logger.error("Feil ved henting av brevmetadata. Status: ${httpResponse.status} Message: ${httpResponse.bodyAsText()}")
            return emptyList()
        }
    }

    private fun filterForKontekst(brevmetadata: BrevdataDto, isVedtaksKontekst: Boolean): Boolean =
        when (brevmetadata.brevkontekst) {
            ALLTID -> true
            SAK -> !isVedtaksKontekst
            VEDTAK -> isVedtaksKontekst
            null -> false
        }

    private fun BrevdataDto.mapToMetadata() =
        LetterMetadata(
            name = dekode,
            id = brevkodeIBrevsystem,
            spraak = sprak ?: emptyList(),
            brevsystem = when (brevsystem) {
                BrevdataDto.BrevSystem.DOKSYS -> BrevSystem.DOKSYS
                BrevdataDto.BrevSystem.GAMMEL -> BrevSystem.EXSTREAM
            },
            brevkategori = kategoriOverrides[brevkodeIBrevsystem] ?: this.brevkategori?.toKategoriTekst(),
            dokumentkategoriCode = this.dokumentkategori,
            redigerbart = redigerbart,
            redigerbarBrevtittel = isRedigerbarBrevtittel(),
        )

    suspend fun getEblanketter(): List<LetterMetadata> {
        return httpClient.get("/api/brevdata/allBrev?includeXsd=false") {
            contentType(ContentType.Application.Json)
        }.body<List<BrevdataDto>>()
            .filter { it.dokumentkategori == BrevdataDto.DokumentkategoriCode.E_BLANKETT }
            .map { it.mapToMetadata() }
    }

    suspend fun getMal(brevkode: String): BrevdataDto {
        return httpClient.get("/api/brevdata/brevForBrevkode/${brevkode}") {
            contentType(ContentType.Application.Json)
        }.body<BrevdataDto>()
    }

    override val name = "Brevmetadata"
    override suspend fun ping(call: ApplicationCall): ServiceResult<Boolean> =
        httpClient.get("/api/internal/isReady").toServiceResult<String>().map { true }
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
    val brevkontekst: BrevkontekstCode?,
    val dokumentkategori: DokumentkategoriCode,
    val synligForVeileder: Boolean?,
    val prioritet: Int?,
    val brevkodeIBrevsystem: String,
    val brevsystem: BrevSystem,
    val brevgruppe: String?,
) {
    @Suppress("unused")
    enum class DokumentkategoriCode { B, E_BLANKETT, IB, SED, VB }
    @Suppress("unused")
    enum class BrevkategoriCode { BREV_MED_SKJEMA, INFORMASJON, INNHENTE_OPPL, NOTAT, OVRIG, VARSEL, VEDTAK }
    enum class BrevSystem { DOKSYS, GAMMEL /*EXSTREAM*/, }
    enum class BrevkontekstCode { ALLTID, SAK, VEDTAK }

    @Suppress("unused")
    enum class DokumentType {
        I, //Inngende dokument
        N, //Notat
        U, //Utgende dokument
    }

    fun isRedigerbarBrevtittel(): Boolean =
        brevkodeIBrevsystem == Brevkoder.FRITEKSTBREV_KODE
                || (dokType == DokumentType.N && brevkodeIBrevsystem !in Brevkoder.ikkeRedigerbarBrevtittel)
}


enum class SpraakKode {
    EN, // Engelsk
    NB, // Bokmaal
    NN, // Nynorsk
    FR, // Fransk
    SE, // Nord-samisk
}

enum class BrevSystem { EXSTREAM, DOKSYS, BREVBAKER }

data class LetterMetadata(
    val name: String,
    val id: String,
    val brevsystem: BrevSystem,
    val spraak: List<SpraakKode>, // Enkelte brev er egentlig bare bokmål, men har null i metadata.
    val brevkategori: String?,
    val dokumentkategoriCode: BrevdataDto.DokumentkategoriCode?,
    val redigerbart: Boolean,
    val redigerbarBrevtittel: Boolean,
)

object Brevkoder {
    const val FRITEKSTBREV_KODE = "PE_IY_05_300"
    const val POSTERINGSGRUNNLAG_KODE = "PE_OK_06_100"
    const val POSTERINGSGRUNNLAG_VIRK0101_KODE = "PE_OK_06_101"
    const val POSTERINGSGRUNNLAG_VIRK0102_KODE = "PE_OK_06_102"

    val ikkeRedigerbarBrevtittel = setOf(POSTERINGSGRUNNLAG_KODE, POSTERINGSGRUNNLAG_VIRK0101_KODE, POSTERINGSGRUNNLAG_VIRK0102_KODE)
}

private val kategoriOverrides: Map<String, String> = mapOf(
    "Etteroppgjør" to listOf("PE_AF_03_101","PE_AF_04_100","PE_AF_04_101","PE_AF_04_102","PE_AF_04_103","PE_AF_04_104","PE_AF_04_105","PE_AF_04_106","PE_AF_04_108","PE_AF_04_109","PE_UT_04_400","PE_UT_04_401","PE_UT_04_402"),
    "Førstegangsbehandling" to listOf("PE_AF_04_001","PE_AF_04_010","PE_AF_04_111","PE_AF_04_112","AP_AVSL_TIDLUTTAK","AP_AVSL_UTTAK","AP_INNV_MAN","AP_INNV_AVT_MAN","PE_AP_04_001","PE_AP_04_010","PE_AP_04_202","PE_AP_04_203","PE_AP_04_211","PE_AP_04_212","PE_AP_04_901","PE_AP_04_902","PE_AP_04_910","PE_AP_04_913","PE_AP_04_914","BP_AVSL_MAN","PE_BP_04_001","PE_BP_04_002","PE_BP_04_010","PE_BP_04_011","PE_GP_04_001","PE_GP_04_010","PE_GP_04_023","PE_GP_04_024","PE_GP_04_026","PE_GP_04_031","PE_GP_04_033","PE_UT_04_101","PE_UT_04_117","PE_UT_04_118"),
    "Vedtak - endring og revurdering" to listOf("PE_AF_04_020","PE_AF_04_114","AP_AVSL_ENDR","AP_ENDR_GRAD_MAN","AP_ENDR_STANS_MAN","AP_ENDR_OPPTJ_MAN","AP_ENDR_FT_MAN","AP_ENDR_EPS_MAN","AP_ENDR_INST_MAN","AP_ENDR_GJRETT_MAN","AP_OPPH_FT_MAN","AP_AVSL_GJRETT_MAN","AP_AVSL_FT_MAN","PE_AP_04_020","PE_AP_04_214","PE_AP_04_215","PE_AP_04_216","PE_AP_04_220","PE_AP_04_920","PE_FT_01_006","PE_FT_01_007","BP_OPPH_MAN","PE_BP_04_020","PE_BP_04_021","PE_BP_04_031","PE_GP_04_020","PE_GP_04_028","PE_GP_04_029","PE_GP_04_030","PE_UT_06_300","PE_UT_04_100","PE_UT_04_102","PE_UT_04_109","PE_UT_04_114","PE_UT_07_100"),
    "Vedtak - flytte mellom land" to listOf("AP_ENDR_FLYTT_MAN","AP_STANS_FLYTT_MAN","PE_AP_04_223","PE_AP_04_224","PE_AP_04_225","PE_IY_04_125","PE_IY_04_126","PE_IY_04_127"),
    "Sluttbehandling" to listOf("PE_AP_04_903","PE_AP_04_904","PE_AP_04_912","PE_GP_04_025","PE_GP_04_032","PE_UT_04_106","PE_UT_04_107","INFO_P1"),
    "Informasjonsbrev" to listOf("PE_IY_03_175","AP_INFO_STID_MAN","PE_IY_03_174","PE_BP_01_001","PE_BP_01_002","PE_IY_03_169","PE_UP_07_105","PE_UT_04_001","PE_UT_04_004","PE_IY_05_041","PE_IY_03_163","PE_IY_03_164","PE_IY_03_180","PE_IY_03_170","PE_IY_03_172", "DOD_INFO_RETT_MAN", "PE_IY_03_173", "PE_IY_03_171", "PE_AP_04_922", "PE_AP_04_922"),
    "Varsel" to listOf("VARSEL_REVURD","PE_IY_03_051","PE_IY_03_179"),
    "Vedtak - eksport" to listOf("PE_GP_04_022","PE_UT_04_103","PE_UT_04_115"),
    "Omsorgsopptjening" to listOf("OMSORG_EGEN_MAN","PE_IY_04_010"),
    "Uførepensjon" to listOf("PE_UP_04_020","PE_UP_04_022","PE_UP_04_023","PE_UP_04_024","PE_UP_04_025","PE_UP_04_026","PE_UP_04_027","PE_UP_04_028","PE_UP_04_029","PE_UP_04_030","PE_UP_07_010","PE_UT_04_300"),
    "Innhente opplysninger" to listOf("PE_UP_07_100","PE_UT_04_003","HENT_INFO_MAN","PE_UT_04_002","PE_IY_03_167","PE_IY_03_047","PE_IY_03_048","PE_IY_03_168","PE_IY_03_178","PE_IY_05_006","PE_IY_05_007","PE_IY_05_008","PE_IY_03_049","PE_GP_01_010"),
    "Leveattest" to listOf("PE_IY_03_176","PE_IY_03_177","PE_IY_05_301","PE_IY_05_411","PE_IY_05_510","PE_IY_05_410","PE_IY_05_511"),
    "Feilutbetaling" to listOf("VARSEL_TILBAKEBET","VEDTAK_TILBAKEKREV","VEDTAK_TILBAKEKREV_MIDL","PE_IY_04_060","PE_IY_04_061","PE_IY_05_027"),
    "Klage og anke" to listOf("PE_IY_03_151","PE_IY_03_152","PE_IY_03_158","PE_IY_03_159","PE_IY_03_160","PE_IY_04_050","PE_IY_04_051","PE_IY_03_150","PE_IY_03_153","PE_IY_03_154","PE_IY_03_157","PE_IY_03_161","PE_IY_03_162"),
    "Posteringsgrunnlag" to listOf("PE_OK_06_100","PE_OK_06_101", "PE_OK_06_102"),
    "Fritekstbrev" to listOf("PE_IY_03_156","PE_IY_05_300"),
).flatMap { kategori -> kategori.value.map { it to kategori.key } }.toMap()

private fun BrevkategoriCode.toKategoriTekst() =
    when (this) {
        BrevkategoriCode.BREV_MED_SKJEMA -> "Brev med skjema"
        BrevkategoriCode.INFORMASJON -> "Informasjon"
        BrevkategoriCode.INNHENTE_OPPL -> "Innhente opplysninger"
        BrevkategoriCode.NOTAT -> "Notat"
        BrevkategoriCode.OVRIG -> "Øvrig"
        BrevkategoriCode.VARSEL -> "Varsel"
        BrevkategoriCode.VEDTAK -> "Vedtak"
    }