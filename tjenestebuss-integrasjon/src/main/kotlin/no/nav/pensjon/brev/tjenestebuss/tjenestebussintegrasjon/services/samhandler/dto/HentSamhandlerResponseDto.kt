package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto

data class HentSamhandlerResponseDto(val success: Success?, val failure: FailureType?) {
    constructor(failure: FailureType) : this(null, failure)
    constructor(success: Success) : this(success, null)

    data class Success(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String,
    )

    enum class FailureType {
        GENERISK,
    }

}