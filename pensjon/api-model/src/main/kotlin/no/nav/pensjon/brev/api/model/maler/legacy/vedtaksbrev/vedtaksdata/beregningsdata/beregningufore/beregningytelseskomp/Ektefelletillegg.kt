package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner


data class Ektefelletillegg(
    val etinnvilget: Boolean?,
    val etnetto: Kroner?,
)