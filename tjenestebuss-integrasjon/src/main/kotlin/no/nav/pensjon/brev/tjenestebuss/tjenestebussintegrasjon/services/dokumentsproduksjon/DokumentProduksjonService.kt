package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.RedigerDoksysDokumentRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon.RedigerDoksysDokumentResponseDto.Failure
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.RedigerDokumentDokumentIkkeFunnet
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.RedigerDokumentInputValideringFeilet
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.RedigerDokumentMetaforceInstanceClosed
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.RedigerDokumentPessimistiskLaasing
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.RedigerDokumentRedigeringIkkeTillatt
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.RedigerDokumentSikkerhetsbegrensning
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
            return RedigerDoksysDokumentResponseDto.Success(response.metaforceURI)
        } catch (ex: RedigerDokumentPessimistiskLaasing) {
            logError(requestDto)
            return Failure(Failure.FailureType.LASING, ex)
        } catch (ex: RedigerDokumentRedigeringIkkeTillatt) {
            logError(requestDto)
            return Failure(Failure.FailureType.IKKE_TILLATT, ex)
        } catch (ex: RedigerDokumentInputValideringFeilet) {
            logError(requestDto)
            return Failure(Failure.FailureType.VALIDERING_FEILET, ex)
        } catch (ex: RedigerDokumentDokumentIkkeFunnet) {
            logError(requestDto)
            return Failure(Failure.FailureType.IKKE_FUNNET, ex)
        } catch (ex: RedigerDokumentSikkerhetsbegrensning) {
            logError(requestDto)
            return Failure(Failure.FailureType.IKKE_TILGANG, ex)
        } catch (ex: RedigerDokumentMetaforceInstanceClosed) {
            logError(requestDto)
            return Failure(Failure.FailureType.LUKKET, ex)
        }
    }

    private fun logError(requestDto: RedigerDoksysDokumentRequestDto) {
        logger.error("En feil oppstod under henting av metaforce URI for dokumentId: ${requestDto.dokumentId} i journalpostId: ${requestDto.journalpostId}")
    }
}

sealed class RedigerDoksysDokumentResponseDto {
    data class Success(
        val metaforceURI: String,
    ) : RedigerDoksysDokumentResponseDto()

    data class Failure(
        val failureType: FailureType,
        val message: String?,
        val cause: String?,
    ) : RedigerDoksysDokumentResponseDto() {
        constructor(failureType: FailureType, ex: Exception) : this(
            failureType,
            ex.message,
            ex.cause?.message,
        )

        enum class FailureType {
            LASING,
            IKKE_TILLATT,
            VALIDERING_FEILET,
            IKKE_FUNNET,
            IKKE_TILGANG,
            LUKKET
        }
    }
}



