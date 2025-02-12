package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKomp
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.uforetrygdberegning.Uforetrygdberegning
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class BeregningUfore(
    val beregningytelseskomp: BeregningYtelsesKomp?,
    val belopredusert: Boolean?,
    val totalnetto: Kroner?,
    val total: Kroner?,
    val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
    val belopsendring: Belopsendring?,
    val uforetrygdberegning: Uforetrygdberegning?,
    val beregningsivilstandanvendt: String?,
    val belopokt: Boolean?,
    val beregningvirkningdatofom: LocalDate?,
    val beregningbrukersivilstand: String?,
)
