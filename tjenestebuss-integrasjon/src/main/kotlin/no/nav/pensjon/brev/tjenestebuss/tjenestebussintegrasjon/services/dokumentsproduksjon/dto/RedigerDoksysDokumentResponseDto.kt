package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.dto

sealed class RedigerDoksysDokumentResponseDto {
    data class Success(
        val metaforceURI: String,
        ) : RedigerDoksysDokumentResponseDto()
    data class Failure(
        val failureType: FailureType,
        val message: String?,
        val cause: String?,
    ) : RedigerDoksysDokumentResponseDto() {
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