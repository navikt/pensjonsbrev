package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.extreambrev

import com.typesafe.config.Config
import no.nav.lib.pen.psakpselv.asbo.brev.ASBOPenHentBrevklientURLRequest
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.RedigerExtreamDokumentRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.slf4j.LoggerFactory

class ExtreamBrevService(private val config: Config, securityHandler: STSSercuritySOAPHandler) : TjenestebussService() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val psakDokbrevClient = PsakDokbrevClient(config, securityHandler, callIdHandler).client()


    /**
     * Henter URL som benyttes for å redigere et Extream dokument.
     *
     * @param requestDto en request som inneholder journalpostId
     * @return en response som enten er Success med extreamBrevUrl eller Failure med eventuell årsak
     */
    fun hentExtreamBrevUrl(requestDto: RedigerExtreamDokumentRequestDto): RedigerExtreamDokumentResponseDto {
        try {
            val response = psakDokbrevClient.hentBrevklientURL(ASBOPenHentBrevklientURLRequest().apply {
                dokumentId = requestDto.dokumentId
                systemId = config.getString("brevklient.systemid")
                passord = config.getString("brevklient.password")
                rootURL = config.getString("brevklient.rooturl")
                bredde = requestDto.bredde
                hoyde = requestDto.hoyde
            })
            logger.info("Henter Extream brev URL for dokumentId: ${requestDto.dokumentId}")
            return RedigerExtreamDokumentResponseDto.Success(response.brevklientURL)
        } catch (ex: Exception) {
            logger.error("En feil oppstod under henting av Extream brev URL for dokumentId: ${requestDto.dokumentId}")
            return RedigerExtreamDokumentResponseDto.Failure(ex.message)
        }
    }
}

sealed class RedigerExtreamDokumentResponseDto {
    data class Success(val url: String) : RedigerExtreamDokumentResponseDto()
    data class Failure(val message: String?) : RedigerExtreamDokumentResponseDto()
}