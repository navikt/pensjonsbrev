package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.FinnSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenFinnSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenHentSamhandlerRequest
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.HentSamhandlerRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.slf4j.LoggerFactory

class SamhandlerTjenestebussService(config: Config, securityHandler: STSSercuritySOAPHandler): TjenestebussService() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val psakSamhandlerClient = PsakSamhandlerClient(config, securityHandler, callIdHandler).client()

    fun hentSamhandler(requestDto: HentSamhandlerRequestDto): HentSamhandlerResponseDto {
        try {
            val response =  psakSamhandlerClient.hentSamhandler(ASBOPenHentSamhandlerRequest().apply {
                idTSSEkstern = requestDto.idTSSEkstern
                hentDetaljert = requestDto.hentDetaljert
            })
            logger.info("Henter samhandler for TSS id: ${requestDto.idTSSEkstern}")
            return  HentSamhandlerResponseDto.Success(
                navn = response.navn,
                samhandlerType = response.samhandlerType,
                offentligId = response.offentligId,
                idType = response.idType
            )
        } catch (ex: HentSamhandlerFaultPenGeneriskMsg) {
            logger.error("En feil oppstod under henting av samhandler med TSS id: ${requestDto.idTSSEkstern}")
            return HentSamhandlerResponseDto.Failure(HentSamhandlerResponseDto.Failure.FailureType.GENERISK, ex.faultInfo)
        } catch (ex: HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg) {
            logger.error("Kunne ikke finne samhandler med  TSS id: ${requestDto.idTSSEkstern}")
            return HentSamhandlerResponseDto.Failure(HentSamhandlerResponseDto.Failure.FailureType.IKKE_FUNNET, ex.faultInfo)
        }
    }

    fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto {
        try {
            logger.info("Finn samhandler med type: ${requestDto.samhandlerType}")

            val samhandlerResponse = psakSamhandlerClient.finnSamhandler(ASBOPenFinnSamhandlerRequest().apply {
                navn = requestDto.navn
                samhandlerType = requestDto.samhandlerType.name
            })
            return FinnSamhandlerResponseDto.Success(samhandlere = samhandlerResponse.samhandlere.map { FinnSamhandlerResponseDto.Success.Samhandler(
                it.navn,
                it.samhandlerType,
                it.offentligId,
                it.idType) }
            )
        } catch (ex: FinnSamhandlerFaultPenGeneriskMsg) {
            logger.error("En feil oppstod under kall til finnSamhandler med navn: ${requestDto.navn} , samhandlerType: ${requestDto.samhandlerType}, cause: ${ex.faultInfo.rootCause} ")
            return FinnSamhandlerResponseDto.Failure(ex.faultInfo.errorMessage, ex.faultInfo.errorType)
        }
    }
}



