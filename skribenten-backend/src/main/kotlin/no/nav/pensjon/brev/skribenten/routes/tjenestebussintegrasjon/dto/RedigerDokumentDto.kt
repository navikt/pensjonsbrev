package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

class RedigerDoksysDokumentRequestDto(
    val journalpostId: String,
    val dokumentId: String,
)

data class RedigerDoksysDokumentResponseDto(val metaforceURI: String?, val failure: FailureType?) {
    enum class FailureType {
        UNDER_REDIGERING,
        IKKE_REDIGERBART,
        VALIDERING_FEILET,
        IKKE_FUNNET,
        IKKE_TILGANG,
        LUKKET,
        UFORVENTET,
        ENHETSID_MANGLER,
    }
}

data class RedigerExstreamDokumentRequestDto(val journalpostId: String)

data class RedigerExstreamDokumentResponseDto(val url: String?, val failure: String?)
