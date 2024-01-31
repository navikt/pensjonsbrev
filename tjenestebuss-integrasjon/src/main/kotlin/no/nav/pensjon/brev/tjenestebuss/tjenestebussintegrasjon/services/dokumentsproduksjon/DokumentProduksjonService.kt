package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.RedigerDoksysDokumentRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.RedigerDoksysDokumentResponseDto.FailureType.*
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.*
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.meldinger.WSRedigerDokumentRequest
import org.slf4j.LoggerFactory

class DokumentproduksjonService(config: Config, securityHandler: STSSercuritySOAPHandler) : TjenestebussService() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val dokumentProduksjonClient = DokumentProduksjonClient(config, securityHandler, callIdHandler).client()


    /**
     * Henter metaforce uri som benyttes for å redigere et DOKSYS brev.
     *
     * @param requestDto en request som inneholder journalpostId og dokumentId
     * @return en response som enten er Success med metaforceURI eller Failure med feiltype og eventuell årsak
     */
    fun redigerDokument(requestDto: RedigerDoksysDokumentRequestDto): RedigerDoksysDokumentResponseDto {
        try {
            val response = dokumentProduksjonClient.redigerDokument(WSRedigerDokumentRequest().apply {
                journalpostId = requestDto.journalpostId
                dokumentId = requestDto.dokumentId
            })
            logger.info("Henter metaforce URI for dokumentId: ${requestDto.journalpostId} i journalpostId: ${requestDto.dokumentId}")
            return RedigerDoksysDokumentResponseDto(response.metaforceURI)
        } catch (ex: Exception) {
            logger.error("En feil oppstod under henting av metaforce URI for dokumentId: ${requestDto.dokumentId} i journalpostId: ${requestDto.journalpostId}, ErrorMessage: ${ex.message}")
            return RedigerDoksysDokumentResponseDto(
                when (ex) {
                    is RedigerDokumentPessimistiskLaasing -> UNDER_REDIGERING
                    is RedigerDokumentRedigeringIkkeTillatt -> IKKE_REDIGERBART
                    is RedigerDokumentInputValideringFeilet -> VALIDERING_FEILET
                    is RedigerDokumentDokumentIkkeFunnet -> IKKE_FUNNET
                    is RedigerDokumentSikkerhetsbegrensning -> IKKE_TILGANG
                    is RedigerDokumentMetaforceInstanceClosed -> LUKKET
                    else -> UFORVENTET
                }
            )
        }
    }
}

data class RedigerDoksysDokumentResponseDto(val metaforceURI: String?, val failure: FailureType?) {
    constructor(metaforceURI: String) : this(metaforceURI = metaforceURI, failure = null)
    constructor(failure: FailureType) : this(metaforceURI = null, failure = failure)

    enum class FailureType {
        UNDER_REDIGERING,
        IKKE_REDIGERBART,
        VALIDERING_FEILET,
        IKKE_FUNNET,
        IKKE_TILGANG,
        LUKKET,
        UFORVENTET
    }
}



