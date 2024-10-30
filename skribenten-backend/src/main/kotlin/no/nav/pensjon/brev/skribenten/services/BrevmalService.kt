package no.nav.pensjon.brev.skribenten.services

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.AFP
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.AFP_PRIVAT
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.ALDER
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.BARNEP
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.FAM_PL
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.GAM_YRK
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.GENRL
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.GJENLEV
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.GRBL
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.KRIGSP
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.OMSORG
import no.nav.pensjon.brev.skribenten.model.Pen.SakType.UFOREP
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode.BREV_MED_SKJEMA
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode.INFORMASJON
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode.INNHENTE_OPPL
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode.NOTAT
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode.OVRIG
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode.VARSEL
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkategoriCode.VEDTAK
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkontekstCode
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevregeltypeCode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.slf4j.LoggerFactory
import no.nav.pensjon.brevbaker.api.model.LetterMetadata as BrevbakerLetterMetadata

class BrevmalService(
    private val penService: PenService,
    private val brevmetadataService: BrevmetadataService,
    private val brevbakerService: BrevbakerService,
) {
    private val logger = LoggerFactory.getLogger(BrevmalService::class.java)

    suspend fun hentBrevmalerForSak(
        sakType: Pen.SakType,
        includeEblanketter: Boolean,
    ): List<LetterMetadata> = coroutineScope {
        val brevbakerMaler = async { hentBrevakerMaler() }
        val brevmetadata = brevmetadataService.hentMaler(sakType, includeEblanketter)
        val relevanteMaler = brevmetadata.maler.filter { filterForSaksKontekst(it) }
        mapToRelevantMetadata(relevanteMaler, brevmetadata.eblanketter, brevbakerMaler.await())
    }

    suspend fun hentBrevmalerForVedtak(
        sakType: Pen.SakType,
        includeEblanketter: Boolean,
        vedtaksId: String,
    ): List<LetterMetadata> = coroutineScope {
        val brevmetadataAsync = async { brevmetadataService.hentMaler(sakType, includeEblanketter) }
        val brevbakerMaler = async { hentBrevakerMaler() }
        val erKravPaaGammeltRegelverk =
            if (sakType == ALDER) {
                penService.hentIsKravPaaGammeltRegelverk(vedtaksId)
                    .catch { message, httpStatusCode ->
                        logger.error("Feil ved henting av felt \"erKravPaaGammeltRegelverk\" fra vedtak. Status: $httpStatusCode, message: $message")
                        false
                    }
            } else false

        val brevmetadata = brevmetadataAsync.await()
        val relevanteMaler = brevmetadata.maler
            .filter { filterForVedtaksKontekst(it) }
            .filter { erRelevantRegelverkstypeForSaktype(sakType, it.brevregeltype, erKravPaaGammeltRegelverk) }
        return@coroutineScope mapToRelevantMetadata(relevanteMaler, brevmetadata.eblanketter, brevbakerMaler.await())
    }

    private fun mapToRelevantMetadata(
        relevanteMaler: List<BrevdataDto>,
        eblanketter: List<BrevdataDto>,
        brevbakerMaler: List<LetterMetadata>
    ) = relevanteMaler
        .asSequence()
        .filter { it.redigerbart }
        .plus(eblanketter)
        .filter { !ekskluderteBrev.contains(it.brevkodeIBrevsystem) }
        .map { it.mapToMetadata() }
        .plus(brevbakerMaler)
        .toList()

    private fun filterForVedtaksKontekst(brevmetadata: BrevdataDto): Boolean =
        when (brevmetadata.brevkontekst) {
            BrevkontekstCode.ALLTID, BrevkontekstCode.VEDTAK -> true
            BrevkontekstCode.SAK -> false
            null -> false
        }

    private fun filterForSaksKontekst(brevmetadata: BrevdataDto): Boolean =
        when (brevmetadata.brevkontekst) {
            BrevkontekstCode.ALLTID, BrevkontekstCode.SAK -> true
            BrevkontekstCode.VEDTAK -> false
            null -> false
        }

    private fun erRelevantRegelverkstypeForSaktype(
        sakstype: Pen.SakType,
        brevregeltype: BrevregeltypeCode?,
        erKravPaaGammeltRegelverk: Boolean,
    ): Boolean =
        if (brevregeltype != null) {
            when (sakstype) {
                ALDER -> if (erKravPaaGammeltRegelverk) {
                    brevregeltype.gjelderGammeltRegelverk()
                } else {
                    brevregeltype.gjelderNyttRegelverk()
                }

                UFOREP -> brevregeltype.gjelderGammeltRegelverk()
                BARNEP, AFP, AFP_PRIVAT, FAM_PL, GAM_YRK, GENRL, GJENLEV, GRBL, KRIGSP, OMSORG -> true
            }
        } else {
            true
        }

    // TODO: Filtrere brevmaler som er relevante
    private suspend fun hentBrevakerMaler(): List<LetterMetadata> =
        if (Features.brevbakerbrev.isEnabled()) {
            brevbakerService.getTemplates()
                .map { result ->
                    result.filter { brev ->
                        when {
                            brev.erMalMedFritekst() -> Features.brevMedFritekst.isEnabled()
                            brev.hasEmptyBrevData() -> Features.brevutendata.isEnabled()
                            else -> true
                        }
                    }
                }
                .map { result -> result.map { it.toMetadata() } }
                .catch { message, statusCode ->
                    logger.error("Kunne ikke hente brevmaler fra brevbaker: $message - $statusCode")
                    emptyList()
                }
        } else emptyList()

    private fun TemplateDescription.erMalMedFritekst() = name in setOf(
        Brevkode.Redigerbar.PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ALDER,
        Brevkode.Redigerbar.PE_FORESPOERSELOMDOKUMENTASJONAVBOTIDINORGE_ETTERLATTE
    ).map { it.name }

    private fun TemplateDescription.hasEmptyBrevData() = letterDataClass in setOf(
        EmptyRedigerbarBrevdata::class.java.name,
        EmptyBrevdata::class.java.name
    )

    private fun TemplateDescription.toMetadata(): LetterMetadata =
        LetterMetadata(
            name = metadata.displayTitle,
            id = name,
            brevsystem = BrevSystem.BREVBAKER,
            spraak = this.languages.map { it.toSpraakKode() },
            brevkategori = kategori?.toKategoriTekst() ?: "Øvrig",
            dokumentkategoriCode = metadata.brevtype.toDokumentkategoriCode(),
            redigerbart = true,
            redigerbarBrevtittel = false,
        )

    private fun BrevdataDto.mapToMetadata() =
        LetterMetadata(
            name = dekode,
            id = brevkodeIBrevsystem,
            spraak = sprak ?: emptyList(),
            brevsystem = when (brevsystem) {
                BrevdataDto.BrevSystem.DOKSYS -> BrevSystem.DOKSYS
                BrevdataDto.BrevSystem.GAMMEL -> BrevSystem.EXSTREAM
            },
            brevkategori = kategoriOverrides[brevkodeIBrevsystem]?.toKategoriTekst() ?: this.brevkategori?.toKategoriTekst(),
            dokumentkategoriCode = this.dokumentkategori,
            redigerbart = redigerbart,
            redigerbarBrevtittel = isRedigerbarBrevtittel(),
        )

    private fun BrevdataDto.BrevkategoriCode.toKategoriTekst() =
        when (this) {
            BREV_MED_SKJEMA -> "Brev med skjema"
            INFORMASJON -> "Informasjon"
            INNHENTE_OPPL -> "Innhente opplysninger"
            NOTAT -> "Notat"
            OVRIG -> "Øvrig"
            VARSEL -> "Varsel"
            VEDTAK -> "Vedtak"
        }

    private fun Brevkategori.toKategoriTekst() =
        when (this) {
            Brevkategori.ETTEROPPGJOER -> "Etteroppgjør"
            Brevkategori.FOERSTEGANGSBEHANDLING -> "Førstegangsbehandling"
            Brevkategori.VEDTAK_ENDRING_OG_REVURDERING -> "Vedtak - endring og revurdering"
            Brevkategori.VEDTAK_FLYTTE_MELLOM_LAND -> "Vedtak - flytte mellom land"
            Brevkategori.SLUTTBEHANDLING -> "Sluttbehandling"
            Brevkategori.INFORMASJONSBREV -> "Informasjonsbrev"
            Brevkategori.VARSEL -> "Varsel"
            Brevkategori.VEDTAK_EKSPORT -> "Vedtak - eksport"
            Brevkategori.OMSORGSOPPTJENING -> "Omsorgsopptjening"
            Brevkategori.UFOEREPENSJON -> "Uførepensjon"
            Brevkategori.INNHENTE_OPPLYSNINGER -> "Innhente opplysninger"
            Brevkategori.LEVEATTEST -> "Leveattest"
            Brevkategori.FEILUTBETALING -> "Feilutbetaling"
            Brevkategori.KLAGE_OG_ANKE -> "Klage og anke"
            Brevkategori.POSTERINGSGRUNNLAG -> "Posteringsgrunnlag"
            Brevkategori.FRITEKSTBREV -> "Fritekstbrev"
        }

    private fun BrevbakerLetterMetadata.Brevtype.toDokumentkategoriCode(): BrevdataDto.DokumentkategoriCode =
        when (this) {
            BrevbakerLetterMetadata.Brevtype.VEDTAKSBREV -> BrevdataDto.DokumentkategoriCode.VB
            BrevbakerLetterMetadata.Brevtype.INFORMASJONSBREV -> BrevdataDto.DokumentkategoriCode.IB
        }

    private fun LanguageCode.toSpraakKode(): SpraakKode =
        when (this) {
            LanguageCode.BOKMAL -> SpraakKode.NB
            LanguageCode.NYNORSK -> SpraakKode.NN
            LanguageCode.ENGLISH -> SpraakKode.EN
        }

}

enum class BrevSystem { EXSTREAM, DOKSYS, BREVBAKER }
enum class SpraakKode {
    EN, // Engelsk
    NB, // Bokmaal
    NN, // Nynorsk
    FR, // Fransk
    SE, // Nord-samisk
}

data class LetterMetadata(
    val name: String,
    val id: String,
    val brevsystem: BrevSystem,
    val spraak: List<SpraakKode>,
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

private val ekskluderteBrev =
    hashSetOf("PE_IY_05_301", "PE_BA_01_108", "PE_GP_01_010", "PE_AP_04_922", "PE_IY_03_169", "PE_IY_03_171", "PE_IY_03_172", "PE_IY_03_173")

private val kategoriOverrides: Map<String, Brevkategori> = mapOf(
    Brevkategori.ETTEROPPGJOER to listOf(
        "PE_AF_03_101",
        "PE_AF_04_100",
        "PE_AF_04_101",
        "PE_AF_04_102",
        "PE_AF_04_103",
        "PE_AF_04_104",
        "PE_AF_04_105",
        "PE_AF_04_106",
        "PE_AF_04_108",
        "PE_AF_04_109",
        "PE_UT_04_400",
        "PE_UT_04_401",
        "PE_UT_04_402"
    ),
    Brevkategori.FOERSTEGANGSBEHANDLING to listOf(
        "AP_AVSL_TIDLUTTAK",
        "AP_AVSL_UTTAK",
        "AP_INNV_AVT_MAN",
        "AP_INNV_MAN",
        "BP_AVSL_MAN",
        "PE_AF_04_001",
        "PE_AF_04_010",
        "PE_AF_04_111",
        "PE_AF_04_112",
        "PE_AP_04_001",
        "PE_AP_04_010",
        "PE_AP_04_202",
        "PE_AP_04_203",
        "PE_AP_04_211",
        "PE_AP_04_212",
        "PE_AP_04_901",
        "PE_AP_04_902",
        "PE_AP_04_910",
        "PE_AP_04_911",
        "PE_AP_04_913",
        "PE_AP_04_914",
        "PE_BP_04_001",
        "PE_BP_04_002",
        "PE_BP_04_010",
        "PE_BP_04_011",
        "PE_GP_04_001",
        "PE_GP_04_010",
        "PE_GP_04_023",
        "PE_GP_04_024",
        "PE_GP_04_026",
        "PE_GP_04_031",
        "PE_GP_04_033",
        "PE_UT_04_101",
        "PE_UT_04_104",
        "PE_UT_04_117",
        "PE_UT_04_118"
    ),
    Brevkategori.VEDTAK_ENDRING_OG_REVURDERING to listOf(
        "AP_AVSL_ENDR",
        "AP_AVSL_FT_MAN",
        "AP_AVSL_GJRETT_MAN",
        "AP_ENDR_EPS_MAN",
        "AP_ENDR_FT_MAN",
        "AP_ENDR_GJRETT_MAN",
        "AP_ENDR_GRAD_MAN",
        "AP_ENDR_INST_MAN",
        "AP_ENDR_OPPTJ_MAN",
        "AP_ENDR_STANS_MAN",
        "AP_OPPH_FT_MAN",
        "BP_OPPH_MAN",
        "PE_AF_04_020",
        "PE_AF_04_114",
        "PE_AP_04_020",
        "PE_AP_04_214",
        "PE_AP_04_215",
        "PE_AP_04_216",
        "PE_AP_04_220",
        "PE_AP_04_920",
        "PE_BP_04_020",
        "PE_BP_04_021",
        "PE_BP_04_031",
        "PE_FT_01_002",
        "PE_FT_01_003",
        "PE_FT_01_006",
        "PE_FT_01_007",
        "PE_GP_04_020",
        "PE_GP_04_028",
        "PE_GP_04_029",
        "PE_GP_04_030",
        "PE_UT_04_100",
        "PE_UT_04_102",
        "PE_UT_04_109",
        "PE_UT_04_114",
        "PE_UT_06_300",
        "PE_UT_07_100"
    ),
    Brevkategori.VEDTAK_FLYTTE_MELLOM_LAND to listOf(
        "AP_ENDR_FLYTT_MAN",
        "AP_STANS_FLYTT_MAN",
        "PE_AP_04_223",
        "PE_AP_04_224",
        "PE_AP_04_225",
        "PE_IY_04_125",
        "PE_IY_04_126",
        "PE_IY_04_127"
    ),
    Brevkategori.SLUTTBEHANDLING to listOf(
        "INFO_P1",
        "PE_AP_04_903",
        "PE_AP_04_904",
        "PE_AP_04_912",
        "PE_GP_04_025",
        "PE_GP_04_032",
        "PE_UT_04_106",
        "PE_UT_04_107"
    ),
    Brevkategori.INFORMASJONSBREV to listOf(
        "AP_INFO_STID_MAN",
        "DOD_INFO_RETT_MAN",
        "PE_AP_04_922",
        "PE_BP_01_001",
        "PE_BP_01_002",
        "PE_IY_03_163",
        "PE_IY_03_164",
        "PE_IY_03_169",
        "PE_IY_03_170",
        "PE_IY_03_171",
        "PE_IY_03_172",
        "PE_IY_03_173",
        "PE_IY_03_174",
        "PE_IY_03_175",
        "PE_IY_03_180",
        "PE_IY_05_041",
        "PE_UP_07_105",
        "PE_UT_04_001",
        "PE_UT_04_004"
    ),
    Brevkategori.VARSEL to listOf("VARSEL_REVURD", "PE_IY_03_051", "PE_IY_03_179"),
    Brevkategori.VEDTAK_EKSPORT to listOf("PE_GP_04_022", "PE_UT_04_103", "PE_UT_04_115"),
    Brevkategori.OMSORGSOPPTJENING to listOf("OMSORG_EGEN_MAN", "PE_IY_04_010"),
    Brevkategori.UFOEREPENSJON to listOf(
        "PE_UP_04_001",
        "PE_UP_04_020",
        "PE_UP_04_010",
        "PE_UP_04_021",
        "PE_UP_04_022",
        "PE_UP_04_023",
        "PE_UP_04_024",
        "PE_UP_04_025",
        "PE_UP_04_026",
        "PE_UP_04_027",
        "PE_UP_04_028",
        "PE_UP_04_029",
        "PE_UP_04_030",
        "PE_UP_07_010",
        "PE_UT_04_300"
    ),
    Brevkategori.INNHENTE_OPPLYSNINGER to listOf(
        "PE_UP_07_100",
        "PE_UT_04_003",
        "HENT_INFO_MAN",
        "PE_UT_04_002",
        "PE_IY_03_167",
        "PE_IY_03_047",
        "PE_IY_03_048",
        "PE_IY_03_168",
        "PE_IY_03_178",
        "PE_IY_05_006",
        "PE_IY_05_007",
        "PE_IY_05_008",
        "PE_IY_03_049",
        "PE_GP_01_010"
    ),
    Brevkategori.LEVEATTEST to listOf("PE_IY_03_176", "PE_IY_03_177", "PE_IY_05_411", "PE_IY_05_510", "PE_IY_05_410", "PE_IY_05_511"),
    Brevkategori.FEILUTBETALING to listOf(
        "VARSEL_TILBAKEBET",
        "VEDTAK_TILBAKEKREV",
        "VEDTAK_TILBAKEKREV_MIDL",
        "PE_IY_04_060",
        "PE_IY_04_061",
        "PE_IY_05_027"
    ),
    Brevkategori.KLAGE_OG_ANKE to listOf(
        "PE_IY_03_151",
        "PE_IY_03_152",
        "PE_IY_03_158",
        "PE_IY_03_159",
        "PE_IY_03_160",
        "PE_IY_04_050",
        "PE_IY_04_051",
        "PE_IY_03_150",
        "PE_IY_03_153",
        "PE_IY_03_154",
        "PE_IY_03_157",
        "PE_IY_03_161",
        "PE_IY_03_162"
    ),
    Brevkategori.POSTERINGSGRUNNLAG to listOf("PE_OK_06_100", "PE_OK_06_101", "PE_OK_06_102"),
    Brevkategori.FRITEKSTBREV to listOf("PE_IY_03_156", "PE_IY_05_300"),
).flatMap { kategori -> kategori.value.map { it to kategori.key } }.toMap()