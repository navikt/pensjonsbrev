package no.nav.pensjon.brev.ufore.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class TilbakekrevingAvFeilutbetaltBeloepDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptyBrevdata
) : RedigerbarBrevdata<EmptyBrevdata, TilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {

    data class PesysData(
        val feilutbetaltTotalBeloep: Kroner,
        val resultatAvVurderingenForTotalBeloep: TilbakekrevingResultat,
        val sluttPeriodeForTilbakekreving: LocalDate,
        val startPeriodeForTilbakekreving: LocalDate,
        val sumTilInnkrevingTotalBeloep: Kroner,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
        val oversiktOverFeilutbetalingPEDto: OversiktOverFeilutbetalingPEDto,
    ) : BrevbakerBrevdata

    data class OversiktOverFeilutbetalingPEDto(
        val bruttoTilbakekrevdTotalbeloep: Kroner,
        val nettoUtenRenterTilbakekrevdTotalbeloep: Kroner,
        val rentetilleggSomInnkrevesTotalbeloep: Kroner,
        val resultatAvVurderingenForTotalbeloep: TilbakekrevingResultat,
        val skattefradragSomInnkrevesTotalbeloep: Kroner,
        val tilbakekrevingPerMaaned: List<Tilbakekreving>,
    ) : BrevbakerBrevdata {
        data class Tilbakekreving(
            val maanedOgAar: LocalDate,
            val bruttobeloepTilbakekrevd: Kroner,
            val feilutbetaltBeloep: Kroner,
            val nettobeloepUtenRenterTilbakekrevd: Kroner,
            val resultatAvVurderingen: TilbakekrevingResultat,
            val skattefradragSomInnkreves: Kroner,
            val ytelsenMedFeilutbetaling: KonteringType,
        )
    }

    data class DineRettigheterOgMulighetTilAaKlageDto(
        val sakstype: Sakstype,
        val brukerUnder18Aar: Boolean?,
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
}
