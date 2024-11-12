package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.beregningsgrunnlagavdodordiner

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.OpptjeningUT
import no.nav.pensjon.brevbaker.api.model.Kroner

data class BeregningsgrunnlagAvdodOrdiner(
    val arsbelop: Kroner?,
    val opptjeningutliste: List<OpptjeningUT>?,
)
