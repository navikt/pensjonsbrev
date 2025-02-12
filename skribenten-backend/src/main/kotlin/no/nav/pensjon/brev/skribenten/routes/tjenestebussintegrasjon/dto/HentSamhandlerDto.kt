package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

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
    val idTSSEkstern: String,
    val hentDetaljert: Boolean,
)
