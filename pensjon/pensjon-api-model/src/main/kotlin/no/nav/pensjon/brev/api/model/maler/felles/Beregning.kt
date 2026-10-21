package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class Beregning(
    val virkFom: LocalDate,//denne var nullable, men er ikke egentlig det  vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom
    val uforegrad: Int,//vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
    val uforetidspunkt: LocalDate,
    val utbetalingsgrad: Int,//vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad

    //beløp
    val totalNetto: Kroner,//vedtaksdata_beregningsdata_beregningufore_totalnetto
    val belopHarOkt: Boolean, //tidligere nullable, null blir nå false  vedtaksdata_beregningsdata_beregningufore_belopokt
    val beregningsresultatTilRevurderingTotalNetto: Kroner?,

    val bunnfradrag: Kroner,//vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradrag
    val bunnfradragNesteAr: Kroner,//tror denne er nullable og det ikke alltid er satt fra preg  vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_bunnfradragnestear
    val reduksjonsprosent: Double,//vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_kompensasjonsgrad

    val yrkesskadegrad: Int?,// vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad
    val yrkesskadeBest: Boolean,//vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagyrkesskadebest
    val inntektSkadetidspunkt: Kroner?,//vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_inntektvedskadetidspunktet

    val grunnbelop: Kroner,//vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop
)