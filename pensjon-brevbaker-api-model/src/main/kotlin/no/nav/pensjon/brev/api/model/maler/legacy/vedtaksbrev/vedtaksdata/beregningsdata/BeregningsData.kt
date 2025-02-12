package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata

import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.BeregningUfore
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregninguforeperiode.BeregningUforePeriode

data class BeregningsData(
    val beregningufore: BeregningUfore?,
    val beregningantallperioder: Int?,
    val beregninguforeperiode: List<BeregningUforePeriode>,
)
