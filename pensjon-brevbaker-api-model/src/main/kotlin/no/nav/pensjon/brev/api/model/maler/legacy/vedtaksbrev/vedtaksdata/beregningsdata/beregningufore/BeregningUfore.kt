package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp.BeregningYtelsesKomp
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.uforetrygdberegning.Uforetrygdberegning
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class BeregningUfore(
    val BeregningYtelsesKomp: BeregningYtelsesKomp?,
    val BelopRedusert: Boolean?,
    val TotalNetto: Kroner?,
    val Total: Kroner?,
    val Reduksjonsgrunnlag: Reduksjonsgrunnlag?,
    val Belopsendring: Belopsendring?,
    val Uforetrygdberegning: Uforetrygdberegning?,
    val BeregningSivilstandAnvendt: String?,
    val BelopOkt: Kroner?,
    val BeregningVirkningDatoFom: LocalDate?
)