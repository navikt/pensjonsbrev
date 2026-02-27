package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


data class Gjenlevendetillegg(
    val gtinnvilget: Boolean?,
    val gtnetto: Kroner?,
    val nyttgjenlevendetillegg: Boolean?,
    val gjenlevendetillegginformasjon: GjenlevendetilleggInformasjon?
)