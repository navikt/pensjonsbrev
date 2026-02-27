package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.Minsteytelse
import no.nav.pensjon.brevbaker.api.model.Kroner

data class UforetrygdOrdiner(
    val avkortingsbelopperar: Kroner?,
    val avkortningsinformasjon: AvkortningsInformasjon?,
    val brutto: Kroner?,
    val fradrag: Kroner?,
    val minsteytelse: Minsteytelse?,
    val netto: Kroner?,
    val nettoakk: Kroner?,
    val nettoperar: Kroner?,
    val nettorestar: Kroner?,
    val ytelsesgrunnlag: Ytelsesgrunnlag?,
)