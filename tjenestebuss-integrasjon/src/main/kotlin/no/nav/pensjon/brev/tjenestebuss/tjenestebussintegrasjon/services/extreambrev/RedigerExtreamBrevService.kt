package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.extreambrev

import com.typesafe.config.Config
import no.nav.lib.pen.psakpselv.asbo.brev.ASBOPenHentBrevklientURLRequest
import no.nav.lib.pen.psakpselv.asbo.brev.ASBOPenHentBrevklientURLResponse
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.RedigerExtreamDokumentRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.slf4j.LoggerFactory
import java.net.URI

const val TOKEN_QUERY_KEY = "token="
const val BREVKLIENT_WIDTH = "300"
const val BREVKLIENT_HEIGHT = "30"

class RedigerExtreamBrevService(config: Config, securityHandler: STSSercuritySOAPHandler) : TjenestebussService() {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val psakDokbrevClient = PsakDokbrevClient(config, securityHandler, callIdHandler).client()
    private val brevklientSystemId = config.getString("brevklient.systemid")
    private val brevklientPassword = config.getString("brevklient.password")
    private val brevklientRootUrl = config.getString("brevklient.rootUrl")
    private val brevklientChromeRootUrl = config.getString("brevklient.chromeRootUrl")
        .replace(":", "%3A")
        .replace("/", "%2F")
        .replace("?", "%3F")
        .replace("=", "%3D")
        .replace("&", "%26")
        .replace("#", "%23")

    /**
     * Henter URL som benyttes for å redigere et Extream dokument.
     *
     * @param requestDto en request som inneholder journalpostId
     * @return en response som enten er Success med extreamBrevUrl eller Failure med eventuell årsak
     */
    fun hentExtreamBrevUrl(requestDto: RedigerExtreamDokumentRequestDto): RedigerExtreamDokumentResponseDto {
        try {
            val response: ASBOPenHentBrevklientURLResponse =
                psakDokbrevClient.hentBrevklientURL(ASBOPenHentBrevklientURLRequest().apply {
                    dokumentId = requestDto.journalpostId
                    systemId = brevklientSystemId
                    passord = brevklientPassword
                    rootURL = brevklientRootUrl
                    bredde = BREVKLIENT_WIDTH
                    hoyde = BREVKLIENT_HEIGHT
                })
            logger.info("Henter Extream brev URL for dokumentId: ${requestDto.journalpostId}")

            val token = URI(response.brevklientURL).query.split("&").filter { it.startsWith(TOKEN_QUERY_KEY) }
                .map { it.removePrefix(TOKEN_QUERY_KEY) }
                .firstOrNull()
                ?: return RedigerExtreamDokumentResponseDto.failure("Could not find token in redigerExtreamBrev response")

            val redirectChromeUrl =
                "mbdok://$brevklientSystemId@brevklient/dokument/${requestDto.journalpostId}?token=$token&server=$brevklientChromeRootUrl"

            return RedigerExtreamDokumentResponseDto(url = redirectChromeUrl, failure = null)
        } catch (ex: Exception) {
            val message =
                """En feil oppstod under henting av Extream brev URL for dokumentId: ${requestDto.journalpostId}:
                    |${ex.message}""".trimMargin()
            logger.error(message)
            return RedigerExtreamDokumentResponseDto.failure(message)
        }
    }
}

data class RedigerExtreamDokumentResponseDto(val url: String?, val failure: String?) {
    companion object {
        fun success(url: String) = RedigerExtreamDokumentResponseDto(url = url, failure = null)
        fun failure(message: String) = RedigerExtreamDokumentResponseDto(url = null, failure = message)
    }
}
