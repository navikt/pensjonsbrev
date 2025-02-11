package no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.vedtaksdata.beregningsdata.beregningufore.beregningytelseskomp

data class Gjenlevendetillegg(
    val gtinnvilget: Boolean?,
    val nyttgjenlevendetillegg: Boolean?,
    val gjenlevendetillegginformasjon: GjenlevendetilleggInformasjon?,
)
