package no.nav.pensjon.brev.skribenten.brevredigering.domain

import java.time.LocalDate

data class VedleggSnapshot(
    val valgteVedlegg: List<String>,
    val redigerteVedlegg: List<RedigertVedleggHash>,
    val leggVedFoersteside: LeggVedFoerstesideHash?,
) {
    data class RedigertVedleggHash(val vedleggId: String, val hash: String)
    data class LeggVedFoerstesideHash(val leggVedFoersteside: Boolean, val dato: LocalDate)
}
