package no.nav.pensjon.brev.skribenten.routes.samhandler.dto

import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId

data class HentSamhandlerResponseDto(val success: Success?, val failure: FailureType?) {
    data class Success(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String,
    )

    enum class FailureType {
        GENERISK,
        IKKE_FUNNET,
    }
}
class HentSamhandlerRequestDto(
    val idTSSEkstern: TssId,
    val hentDetaljert: Boolean,
)