package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import javax.xml.namespace.QName


/**
 * Soap client for PsakSamhandler service.
 * Benyttes for å finne samhandler og tilhørende tssId, kontakt og adresse-informajson
 *
 */
class PsakSamhandlerClient(
    config: Config,
    securityHandler: STSSercuritySOAPHandler,
    callIdSoapHandler: TjenestebussService.CallIdSoapHandler
) {
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
        handlers = listOf(securityHandler, callIdSoapHandler)
        features = listOf(WSAddressingFeature())
    }

    fun client(): PSAKSamhandler = jaxWsProxyFactoryBean.create() as PSAKSamhandler
}

