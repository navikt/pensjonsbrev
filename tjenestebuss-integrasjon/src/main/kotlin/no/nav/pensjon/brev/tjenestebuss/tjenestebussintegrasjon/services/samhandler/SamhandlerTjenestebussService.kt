package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.FinnSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenGeneriskMsg
import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg
import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenFinnSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenHentSamhandlerRequest
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.HentSamhandlerRequestDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler.dto.HentSamhandlerResponseDto
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import org.slf4j.LoggerFactory
import javax.xml.namespace.QName

class SamhandlerTjenestebussService(config: Config, securityHandler: STSSercuritySOAPHandler) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val tjenestebussUrl = config.getString("url")
    private val jaxWsProxyFactoryBean = JaxWsProxyFactoryBean().apply {
        val name = "PSAKSamhandlerWSEXP_PSAKSamhandlerHttpService"
        val portName = "PSAKSamhandlerWSEXP_PSAKSamhandlerHttpPort"
        val namespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf/Binding"
        address = "${tjenestebussUrl}nav-cons-pen-psak-samhandlerWeb/sca/PSAKSamhandlerWSEXP"
        wsdlURL = "wsdl/PSAKSamhandlerWSEXP_PSAKSamhandlerHttp_Service.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = PSAKSamhandler::class.java
        handlers = listOf(securityHandler)
        features = listOf(WSAddressingFeature())        //TODO add Logging feature?
    }

    fun hentSamhandler(requestDto: HentSamhandlerRequestDto): HentSamhandlerResponseDto {
        // TODO do we need to create a new bean every time to get refreshed auth?
        val psakSamhandlerClient: PSAKSamhandler = jaxWsProxyFactoryBean.create() as PSAKSamhandler

        try {
            val response =  psakSamhandlerClient.hentSamhandler(ASBOPenHentSamhandlerRequest().apply {
                idTSSEkstern = requestDto.idTSSEkstern
                hentDetaljert = requestDto.hentDetaljert
            })
            logger.info("Henter samhandler for TSS id: ${requestDto.idTSSEkstern}")
            return  HentSamhandlerResponseDto.Samhandler(
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
        val psakSamhandlerClient: PSAKSamhandler = jaxWsProxyFactoryBean.create() as PSAKSamhandler
        try {
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
            logger.error("En feil oppstod under kall til finnSamhandler med navn: ${requestDto.navn} , samhandlerType: ${requestDto.samhandlerType}")
            return FinnSamhandlerResponseDto.Failure(ex.faultInfo)
        }
    }
}



