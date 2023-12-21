package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

sealed class HentSamhandlerResponseDto {
    data class Success(val samhandler: Samhandler) {
        data class Samhandler(
            val navn: String,
            val samhandlerType: String,
            val offentligId: String,
            val idType: String
        ) : HentSamhandlerResponseDto()
    }

    data class Failure(
        val message: String,
        val type: String,
    ) : HentSamhandlerResponseDto()
}

class HentSamhandlerRequestDto(
    val idTSSEkstern: String,
    val hentDetaljert: Boolean,
)