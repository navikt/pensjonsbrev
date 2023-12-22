package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto

sealed class FinnSamhandlerResponseDto {
    data class Success(val samhandlere: List<Samhandler>) : FinnSamhandlerResponseDto() {
        data class Samhandler(
            val navn: String,
            val samhandlerType: String,
            val offentligId: String,
            val idType: String
        )
    }

    data class Failure(
        val message: String,
        val type: String,
    ) : FinnSamhandlerResponseDto()
}