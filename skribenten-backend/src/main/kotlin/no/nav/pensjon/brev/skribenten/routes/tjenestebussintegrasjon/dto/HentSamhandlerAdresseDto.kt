package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

data class HentSamhandlerAdresseResponseDto(
    val adresse: SamhandlerPostadresse?,
    val failureType: FailureType?,
) {
    data class SamhandlerPostadresse(
        val navn: String,
        val linje1: String?,
        val linje2: String?,
        val linje3: String?,
        val postnr: String?,
        val poststed: String?,
        val land: String?,
    )

    enum class FailureType {
        NOT_FOUND,
        GENERISK,
    }
}

data class HentSamhandlerAdresseRequestDto(
    val idTSSEkstern: String,
)
