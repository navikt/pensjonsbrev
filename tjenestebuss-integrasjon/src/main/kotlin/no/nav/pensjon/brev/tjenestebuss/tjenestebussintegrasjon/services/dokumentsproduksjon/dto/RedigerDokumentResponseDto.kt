package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.dto

sealed class RedigerDokumentResponseDto {
    data class Success(
        val metaforceURI: String,
        ) : RedigerDokumentResponseDto()
    data class Failure(
        val failureType: FailureType,
        val message: String?,
        val cause: String?,
    ) : RedigerDokumentResponseDto() {
        constructor(failureType: FailureType, ex: Exception): this(
            failureType,
            ex.message,
            ex.cause?.message,
        )
        enum class FailureType {
            LASING,
            IKKE_TILLATT,
            VALIDERING_FEILET   ,
            IKKE_FUNNET,
            IKKE_TILGANG,
            LUKKET
        }
    }
}