package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Vedlegg «Oversikt over pensjonens størrelse» for AFP i privat sektor
 * (PE_AF_oversikt_over_pensjonen_RTF).
 *
 * Inkluderes kun når vedtaket har flere beregningsperioder
 * (PE_Vedtaksdata_BeregningsData_BeregningAntallPerioder > 1).
 *
 * Hver [Periode] viser AFP-en for en avgrenset periode (FOM-TOM), og
 * [sistePeriode] viser den endelige/siste beregningen (kun FOM).
 *
 * Forskjellig fra [OversiktOverPensjonenAfpDto] (MR71, offentlig sektor)
 * som bryter ned pensjon i grunnpensjon/tilleggspensjon/særtillegg osv.
 * RTF-varianten bryter ned i AFP-komponenter (livsvarig/kronetillegg/
 * kompensasjonstillegg).
 */
data class OversiktOverPensjonenAfpPrivatDto(
    // PE_Vedtaksdata_VirkningFOM
    val virkningFom: LocalDate,
    val perioder: List<Periode>,
    val sistePeriode: SistePeriode,
) : VedleggData {
    data class Periode(
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_VirkDatoFOM
        val virkDatoFom: LocalDate,
        // PE_Vedtaksdata_BeregningsData_BeregningPeriode_VirkDatoTOM
        val virkDatoTom: LocalDate,
        val beregning: AfpBeregning,
    )

    data class SistePeriode(
        // PE_Vedtaksdata_BeregningsData_Beregning_VirkDatoFOM
        val virkDatoFom: LocalDate,
        val beregning: AfpBeregning,
    )

    data class AfpBeregning(
        // PE_…_TotalPensjon
        val totalPensjon: Kroner,
        // PE_…_AFPLivsvarig_AFPLivsvarBrutto. Tilstede ⇔ AFPLivsvarInnvilget = true
        val livsvarigBrutto: Kroner?,
        // PE_…_AFPKronetillegg_AFPKroneBrutto. Tilstede ⇔ AFPKroneInnvilget = true
        val kronetilleggBrutto: Kroner?,
        // PE_…_AFPKompensasjonstillegg_AFPKompBrutto. Tilstede ⇔ AFPKompInnvilget = true
        val kompensasjonstilleggBrutto: Kroner?,
    )
}
