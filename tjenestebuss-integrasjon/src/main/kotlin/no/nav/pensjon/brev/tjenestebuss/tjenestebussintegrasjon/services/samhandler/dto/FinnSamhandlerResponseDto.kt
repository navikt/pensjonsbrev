package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto

data class FinnSamhandlerResponseDto(val samhandlere: List<Samhandler>, val failureType: String?) {
    constructor(samhandlere: List<Samhandler>) : this(samhandlere, null)
    constructor(failure: String) : this(emptyList(), failure)

    data class Samhandler(
        val navn: String,
        val samhandlerType: String,
        val offentligId: String,
        val idType: String,
        val idTSSEkstern: String,
        )
}