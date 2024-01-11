package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto


class RedigerDoksysDokumentRequestDto(
    val journalpostId: String,
    val dokumentId: String,
)

class RedigerExtreamDokumentRequestDto(
    val dokumentId: String,
)

sealed class RedigerExtreamDokumentResponseDto {
    data class Success(val url: String) : RedigerExtreamDokumentResponseDto()
    data class Failure(val message: String?) : RedigerExtreamDokumentResponseDto()
}


sealed class RedigerDoksysBrevResponse {
    data class Success(val url: String) : RedigerDoksysBrevResponse()
    data class Failure(val message: String?,val type: String?) : RedigerDoksysBrevResponse()
}
