package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class EndringIOpptjeningAutoDto(
    val endringIOpptjening: EndringIOpptjening,
    val fellesbarnTillegg: FellesbarnTillegg?,
    val saerkullsbarnTillegg: SaerkullsbarnTillegg?,
    val sivilstand: Sivilstand,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto,
    val opplysningerBruktIBeregningUT: OpplysningerBruktIBeregningUTDto,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) {
    data class EndringIOpptjening(
        val beloepsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense
        val brukerBorInorge: Boolean,
        val forventetInntekt: Kroner,  //  Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
        val grunnbeloep: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
        val harBeloepOekt: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt
        val harBeloepRedusert: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert
        val harDelvisUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad > 0 < 100
        val harEktefelletilleggInnvilget: Boolean?,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Ektefelletillegg
        val harFullUfoeregrad: Boolean,  // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad = 100
        val harFullUtbetalingsgrad: Boolean,
        val harGjenlevendetilleggInnvilget: Boolean?,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg
        val harInntektEtterUfoere: Boolean,  // IEUInntekt > 0 / Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val harUtbetalingsgrad:Boolean,
        val harYrkesskadeGradUtbetaling: Boolean?,
        val inntektsgrense: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektsgrenseNesteAar: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
        val inntektstak: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
        val kompensasjonsgrad: Double,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad
        val nettoAkkumulerteBeloepUtbetalt: Kroner,  // <NettoAkk> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
        val nettoAkkumulertePlussNettoRestAar: Kroner,  // <NettoAkk> + <NettoRestAr> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
        val nettoTilUtbetalingRestenAvAaret: Kroner,  // Kan bli et negativt tall <NettoRestAr> edtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
        val nettoUfoeretrygdUtbetaltPerMaaned: Kroner,  // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto
        val oppjustertInntektEtterUfoere: Kroner,  // <Oieu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu
        val oppjustertInntektFoerUfoere80prosent: Kroner,  // <Oifu * 0.8) Beregning i Exstream! Tror verdien finnes i Pesys
        val oppjustertInntektFoerUfoere: Kroner,  // <Oifu> Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu
        val ufoeregrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegningu_ufoeregrad
        val ufoertrygdUtbetalt: Kroner,
        val utbetalingsgrad: Int, // Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_utbetalingsgrad
        val utbetaltPerMaaned: Kroner,
        val virkningsDato: LocalDate,
    )

    data class FellesbarnTillegg(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFellesbarnInnvilget: Boolean,
        val harFradrag: Boolean,
        val harFratrukketBeloepFraAnnenForelder: Boolean,
        val harJusteringsbeloep: Boolean,
        val inntektAnnenForelder: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
        )

    data class SaerkullsbarnTillegg(
        val beloepBrutto: Kroner,
        val beloepNetto: Kroner,
        val fribeloep: Kroner,
        val gjelderFlereBarn: Boolean,
        val harFradrag: Boolean,
        val harJusteringsbeloep: Boolean,
        val harSaerkullsbarnInnvilget: Boolean,
        val inntektBruktIAvkortning: Kroner,
        val inntektstak: Kroner,
        )

}