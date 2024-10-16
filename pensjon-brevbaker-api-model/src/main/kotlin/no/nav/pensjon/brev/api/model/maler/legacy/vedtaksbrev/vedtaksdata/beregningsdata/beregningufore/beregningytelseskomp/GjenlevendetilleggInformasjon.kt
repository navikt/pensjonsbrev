package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class GjenlevendetilleggInformasjon (
    val uforetidspunkt: LocalDate?,
    val anvendttrygdetid: Boolean?,
    val minsteytelsebenyttetungufor: Boolean?,
    val beregningsgrunnlagavdodordiner: Boolean?,
    val yrkesskadegrad: Int?,
    val beregningsgrunnlagavdodyrkesskadearsbelop: Kroner?,
    val inntektvedskadetidspunktet: Kroner?,
)