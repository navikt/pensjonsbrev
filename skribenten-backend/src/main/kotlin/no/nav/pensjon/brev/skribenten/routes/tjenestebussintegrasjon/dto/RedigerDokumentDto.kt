package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto


class RedigerDoksysDokumentRequestDto(
    val journalpostId: String,
    val dokumentId: String,
)

sealed class RedigerDoksysDokumentResponseDto {
    data class Success(val url: String) : RedigerDoksysDokumentResponseDto()
    data class Failure(val message: String?,val type: FailureType?) : RedigerDoksysDokumentResponseDto()

    enum class FailureType {
        LASING,
        IKKE_TILLATT,
        VALIDERING_FEILET,
        IKKE_FUNNET,
        IKKE_TILGANG,
        LUKKET
    }
}

data class RedigerExtreamDokumentRequestDto(
    val journalpostId: String,
)

data class RedigerExtreamDokumentResponseDto(val url: String?, val failure: String?)
