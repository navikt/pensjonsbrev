package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto

data class FinnSamhandlerResponseDto(val samhandlere: List<Samhandler>, failureType:) {
    data class Samhandler(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String
    )
    enum class FailureType{

    }
}