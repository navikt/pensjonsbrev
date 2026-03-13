package no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


data class Gjenlevendetillegg(
    val gtinnvilget: Boolean?,
    val gtbrutto: Kroner?,
    val gtnetto: Kroner?,
    val nyttgjenlevendetillegg: Boolean?,
    val gjenlevendetillegginformasjon: GjenlevendetilleggInformasjon?
)