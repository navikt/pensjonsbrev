package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.beregningsgrunnlagavdodordiner.BeregningsgrunnlagAvdodOrdiner
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class GjenlevendetilleggInformasjon (
    val uforetidspunkt: LocalDate?,
    val anvendttrygdetid: Int?,
    val minsteytelsebenyttetungufor: Boolean?,
    val beregningsgrunnlagavdodordiner: BeregningsgrunnlagAvdodOrdiner?,
    val yrkesskadegrad: Int?,
    val beregningsgrunnlagavdodyrkesskadearsbelop: Kroner?,
    val inntektvedskadetidspunktet: Kroner?,
)