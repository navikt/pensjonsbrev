package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.PesysBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.VedtakFeilutbetalingUforeDto.Saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate
import java.time.Month

data class VedtakFeilutbetalingUforeIngenTilbakekrevingDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: EmptySaksbehandlerValg,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, PesysData> {}

data class VedtakFeilutbetalingUforeDto(
    override val pesysData: PesysData, override val saksbehandlerValg: Saksbehandlervalg
) : RedigerbarBrevdata<Saksbehandlervalg, PesysData> {

    data class Saksbehandlervalg(
        @DisplayText("Endret sivilstand")
        val sivilstandEndret: Boolean,
        @DisplayText("Reduksjon foreldelse")
        val reduksjonForeldelse: Boolean,

        ) : SaksbehandlerValgBrevdata
}

data class PesysData(
    val feilutbetaltTotalBelop: Int,
    val resultatAvVurderingenForTotalBelop: TilbakekrevingResultat,
    val sluttPeriodeForTilbakekreving: LocalDate,
    val startPeriodeForTilbakekreving: LocalDate,
    val sumTilInnkrevingTotalBelop: Int,
    val dineRettigheterOgMulighetTilAKlageDto: DineRettigheterOgMulighetTilAKlageDto,
    val oversiktOverFeilutbetalingPEDto: OversiktOverFeilutbetalingPEDto,
) : PesysBrevdata

data class OversiktOverFeilutbetalingPEDto(
    val bruttoTilbakekrevdTotalbelop: Int,
    val nettoUtenRenterTilbakekrevdTotalbelop: Int,
    val rentetilleggSomInnkrevesTotalbelop: Int,
    val resultatAvVurderingenForTotalbelop: TilbakekrevingResultat,
    val skattefradragSomInnkrevesTotalbelop: Int,
    val tilbakekrevingPerManed: List<Tilbakekreving>,
    val feilutbetalingPerArListe: List<FeilutbetalingPerAr>? = null,
) : VedleggData {
    data class Tilbakekreving(
        val manedOgAr: LocalDate,
        val bruttobelopTilbakekrevd: Int,
        val feilutbetaltBelop: Int,
        val nettobelopUtenRenterTilbakekrevd: Int,
        val resultatAvVurderingen: TilbakekrevingResultat,
        val skattefradragSomInnkreves: Int,
        val ytelsenMedFeilutbetaling: KonteringType,
    )
}

data class FeilutbetalingPerAr(
    val ar: Int,
    val feilutbetalingManed: List<FeilutbetalingManed>
)

data class FeilutbetalingManed(
    val maned: Month,
    val feilutbetaltBelop: Int,
    val resultat: TilbakekrevingResultat,
    val bruttoBelop: Int,
    val skatt: Int,
    val nettobelopBelop: Int,
)

data class DineRettigheterOgMulighetTilAKlageDto(
    val sakstype: Sakstype,
    val brukerUnder18Ar: Boolean?,
)

enum class TilbakekrevingResultat {
    DELVIS_TILBAKEKREV,
    FEILREGISTRERT,
    FORELDET,
    FULL_TILBAKEKREV,
    INGEN_TILBAKEKREV,
}

enum class KonteringType {
    AAP,
    AFP_KOMP_TILLEGG,
    AFP_KRONETILLEGG,
    AFP_LIVSVARIG,
    AFP_T,
    ANNET,
    AP_GJT,
    AP_GJT_KAP19,
    BARNEPENSJON,
    BARNETILSYN,
    BT,
    ET,
    FAM_T,
    FAST_UTGIFT_T,
    FEILKONTO,
    GAP,
    GARANTITILLEGG,
    GAT,
    GP,
    HJELP_BIDRAG,
    HJELP_I_HUS,
    IP,
    JUSTERINGSKONTO,
    KRIG_GY,
    MENDEL,
    MIN_NIVA_TILL_INDV,
    MIN_NIVA_TILL_PPAR,
    MISK,
    PT,
    P_8_5_1_T,
    SISK,
    SKATT,
    SKATT_F_GP,
    SKATT_F_T,
    SKATT_F_UT_ORDINER,
    SKATT_PALEGG,
    SKJERMT,
    SP,
    ST,
    TFB,
    TILSKOTT_FLYTTEUTG,
    TJENESTEPENSJON,
    TP,
    TREKK,
    TSB,
    UFORETILLEGG_AP,
    UTD_STONAD,
    UT_AAP,
    UT_ET,
    UT_FAST_UTGIFT_T,
    UT_GJT,
    UT_GT_NORDISK,
    UT_ORDINER,
    UT_SP,
    UT_TFB,
    UT_TSB,
    VT
}