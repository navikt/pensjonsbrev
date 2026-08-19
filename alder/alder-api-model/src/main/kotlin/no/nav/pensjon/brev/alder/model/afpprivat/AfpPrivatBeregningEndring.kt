package no.nav.pensjon.brev.alder.model.afpprivat

import no.nav.pensjon.brevbaker.api.model.BrevbakerType

/**
 * AFP-beregning i privat sektor for endringsvedtak.
 *
 * Modellerer feltene under PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp i
 * Exstream-malene `PE_AF_04_113` / `PE_AF_04_114`. Hvert tilleggsbeløp er nullable —
 * `null` betyr at tillegget ikke er innvilget (tilsvarer `AFP*Innvilget = false` i originalen).
 */
data class AfpPrivatBeregningEndring(
    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPLivsvarig_AFPLivsvarBrutto
    // (null hvis AFPLivsvarInnvilget = false)
    val livsvarig: BrevbakerType.Kroner?,
    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPKronetillegg_AFPKroneBrutto
    val kronetillegg: BrevbakerType.Kroner?,
    // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPKompensasjonstillegg_AFPKompBrutto
    val kompensasjonstillegg: BrevbakerType.Kroner?,
    // PE_Vedtaksdata_BeregningsData_Beregning_TotalPensjon
    val sumAfpFoerSkatt: BrevbakerType.Kroner,
)