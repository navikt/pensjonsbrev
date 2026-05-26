package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Vedtak — innvilgelse av AFP i privat sektor (autobrev).
 *
 * Ported from the Exstream brevkode `PE_AF_04_115`.
 */
data class InnvilgelseAvAfpAutoDto(
    // PE_Vedtaksdata_Kravhode_KravMottatdato
    // (rtv-brev brev Vedtaksdata Kravhode KravMottatdato)
    val kravMottattDato: LocalDate,

    // PE_Vedtaksdata_VirkningFOM
    // (rtv-brev brev Vedtaksdata VirkningFOM)
    val virkningFom: LocalDate,

    // Avledet fra PE_Vedtaksdata_BrukerAlder
    // (rtv-brev brev Vedtaksdata BrukerAlder)
    // Brevet bruker bare predikatet `BrukerAlder < 70`; modellert som scalar
    val brukerUnder70Aar: Boolean,

    // FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_Trygdeavtaler_Bostedsland, 1) == "nor"
    // (rtv-brev brev Grunnlag Persongrunnlagsliste Trygdeavtaler Bostedsland[1])
    // The letter only branches on bostedsland = "nor" / != "nor"; modelled as Boolean
    // per convert-exstream-letter skill Step 3 exception (literal index 1 -> scalar).
    val bosattINorge: Boolean,

    val afpBeregning: AfpBeregning,
) : AutobrevData {

    data class AfpBeregning(
        // PE_Vedtaksdata_BeregningsData_Beregning_TotalPensjon
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning TotalPensjon)
        val totalPensjon: Kroner,

        // Per AFP-komponent: brutto månedsbeløp, eller `null` når komponenten ikke
        // er innvilget. Nullability erstatter den tidligere `innvilget: Boolean`-flagget
        // (umulig tilstand "innvilget = false med brutto = X kr" er nå urepresenterbar).
        //
        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPLivsvarig_AFPLivsvarBrutto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningYtelsesKomp AFPLivsvarig AFPLivsvarBrutto)
        // Tilstede ⇔ PE_..._AFPLivsvarig_AFPLivsvarInnvilget = true
        val livsvarigBrutto: Kroner?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPKronetillegg_AFPKroneBrutto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningYtelsesKomp AFPKronetillegg AFPKroneBrutto)
        // Tilstede ⇔ PE_..._AFPKronetillegg_AFPKroneInnvilget = true
        val kronetilleggBrutto: Kroner?,

        // PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_AFPKompensasjonstillegg_AFPKompBrutto
        // (rtv-brev brev Vedtaksdata BeregningsData Beregning BeregningYtelsesKomp AFPKompensasjonstillegg AFPKompBrutto)
        // Tilstede ⇔ PE_..._AFPKompensasjonstillegg_AFPKompInnvilget = true
        val kompensasjonstilleggBrutto: Kroner?,
    )
}

