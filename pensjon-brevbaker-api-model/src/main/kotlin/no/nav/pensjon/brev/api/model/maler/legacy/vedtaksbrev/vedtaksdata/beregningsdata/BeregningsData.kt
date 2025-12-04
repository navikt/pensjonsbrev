package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUfore
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregninguforeperiode.BeregningUforePeriode
import no.nav.pensjon.brevbaker.api.model.Kroner

data class BeregningsData(
    val beregningufore: BeregningUfore?,
    val beregningantallperioder: Int?,
    val beregninguforeperiode: List<BeregningUforePeriode>,
    val beregningsresultattilrevurderingtotalnetto: Kroner? = null,
)