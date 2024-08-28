package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.Minsteytelse
import no.nav.pensjon.brevbaker.api.model.Kroner

data class UforetrygdOrdiner(
    val avkortningsinformasjon: AvkortningsInformasjon?,
    val fradrag: Kroner?,
    val netto: Kroner?,
    val nettoakk: Kroner?,
    val minsteytelse: Minsteytelse?,
    val avkortingsbelopperar: Kroner?,
    val brutto: Kroner?,
    val nettorestar: Kroner?,
    val ytelsesgrunnlag: Ytelsesgrunnlag?,
)