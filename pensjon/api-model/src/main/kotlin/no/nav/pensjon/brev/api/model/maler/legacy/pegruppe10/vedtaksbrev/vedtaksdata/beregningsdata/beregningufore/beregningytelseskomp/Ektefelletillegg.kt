package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


data class Ektefelletillegg(
    val etinnvilget: Boolean?,
    val etnetto: Kroner?,
)