package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.Kroner


data class Gjenlevendetillegg(
    val gtinnvilget: Boolean?,
    val gtbrutto: Kroner?,
    val gtnetto: Kroner?,
    val nyttgjenlevendetillegg: Boolean?,
    val gjenlevendetillegginformasjon: GjenlevendetilleggInformasjon?
)