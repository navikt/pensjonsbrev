package no.nav.pensjon.brev.skribenten.brevredigering.domain

data class VedleggSnapshot(
    val valgteVedlegg: List<String>,
    val redigerteVedlegg: List<RedigertVedleggHash>,
) {
    data class RedigertVedleggHash(val vedleggId: String, val hash: String)
}
