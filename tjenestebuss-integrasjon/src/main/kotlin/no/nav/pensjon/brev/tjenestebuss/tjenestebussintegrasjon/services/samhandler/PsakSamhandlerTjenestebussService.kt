package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.FinnSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenFinnSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenHentSamhandlerRequest
import no.nav.lib.pen.psakpselv.fault.FaultPenBase
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.HentSamhandlerRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.slf4j.LoggerFactory

class PsakSamhandlerTjenestebussService(config: Config, securityHandler: STSSercuritySOAPHandler) : TjenestebussService() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val psakSamhandlerClient = PsakSamhandlerClient(config, securityHandler, callIdHandler).client()

    fun hentSamhandler(requestDto: HentSamhandlerRequestDto): HentSamhandlerResponseDto {
        try {
            val response = psakSamhandlerClient.hentSamhandler(ASBOPenHentSamhandlerRequest().apply {
                idTSSEkstern = requestDto.idTSSEkstern
                hentDetaljert = requestDto.hentDetaljert
            })
            logger.info("Henter samhandler for TSS id: ${requestDto.idTSSEkstern}")
            return HentSamhandlerResponseDto(
                HentSamhandlerResponseDto.Success(
                    navn = response.navn,
                    samhandlerType = response.samhandlerType,
                    offentligId = response.offentligId,
                    idType = response.idType
                )
            )
        } catch (ex: HentSamhandlerFaultPenGeneriskMsg) {
            logger.error("En feil oppstod under henting av samhandler med TSS id: ${requestDto.idTSSEkstern} ", ex.faultInfo.prettyPrint())
            return HentSamhandlerResponseDto(HentSamhandlerResponseDto.FailureType.GENERISK)
        } catch (ex: HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg) {
            ex.faultInfo.prettyPrint()
            logger.error("Kunne ikke finne samhandler med TSS id: ${requestDto.idTSSEkstern}", ex.faultInfo.prettyPrint())
            return HentSamhandlerResponseDto(HentSamhandlerResponseDto.FailureType.IKKE_FUNNET)
        }
    }

    fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto {
        try {
            logger.info("Finn samhandler med type: ${requestDto.samhandlerType}")

            val samhandlerResponse = psakSamhandlerClient.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                navn = requestDto.navn
                samhandlerType = requestDto.samhandlerType.name
            })
            return FinnSamhandlerResponseDto(samhandlere = samhandlerResponse.samhandlere.flatMap { samhandler ->
                samhandler.avdelinger.map { avdeling ->
                    FinnSamhandlerResponseDto.Samhandler(
                        navn = avdeling.avdelingNavn.takeIf { !it.isNullOrBlank() } ?: samhandler.navn,
                        samhandlerType = samhandler.samhandlerType,
                        offentligId = samhandler.offentligId,
                        idType = samhandler.idType,
                        idTSSEkstern = avdeling.idTSSEkstern,
                    )
                }
            }.distinctBy { it.idTSSEkstern })
        } catch (ex: FinnSamhandlerFaultPenGeneriskMsg) {
            logger.error(
                "En feil oppstod under kall til finnSamhandler med navn: ${requestDto.navn} , samhandlerType: ${requestDto.samhandlerType}",
                ex.faultInfo.prettyPrint()
            )
            return FinnSamhandlerResponseDto("Feil ved henting av samhandler")
        }
    }
}

private fun FaultPenBase.prettyPrint() =
    """
    | errorSource: $errorSource
    | errorType: $errorType
    | rootCause: $rootCause
    | errorMessage: $errorMessage
    """.trimMargin()


