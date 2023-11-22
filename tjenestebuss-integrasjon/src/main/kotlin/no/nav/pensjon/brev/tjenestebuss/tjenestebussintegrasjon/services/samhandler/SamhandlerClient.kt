package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.STSSercuritySOAPHandler
import no.nav.virksomhet.tjenester.arkiv.v1.Arkiv
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import javax.xml.namespace.QName

class SamhandlerClient(config: Config, securityHandler: STSSercuritySOAPHandler) {
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
    fun client(): PSAKSamhandler = jaxWsProxyFactoryBean.create() as PSAKSamhandler
}

