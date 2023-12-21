package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto


class RedigerDokumentRequestDto(
    val journalpostId: String,
    val dokumentId: String,
)

sealed class RedigerDokumentResponseDto {
    data class Success(
        val metaforceURI: String,
    ) : RedigerDokumentResponseDto()

    data class Failure(
        val message: String?,
        val type: String?,
    ) : RedigerDokumentResponseDto()
}
