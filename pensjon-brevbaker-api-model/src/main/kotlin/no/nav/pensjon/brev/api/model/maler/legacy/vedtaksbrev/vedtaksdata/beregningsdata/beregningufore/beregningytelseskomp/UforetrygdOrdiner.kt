package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.Minsteytelse
import no.nav.pensjon.brevbaker.api.model.Kroner

data class UforetrygdOrdiner(
    val AvkortningsInformasjon: AvkortningsInformasjon?,
    val Fradrag: Kroner?,
    val Netto: Kroner?,
    val NettoAkk: Kroner?,
    val Minsteytelse: Minsteytelse?,
    val AvkortingsbelopPerAr: Kroner?,
    val Brutto: Kroner?,
    val NettoRestAr: Kroner?,
    val Ytelsesgrunnlag: Ytelsesgrunnlag?,
)