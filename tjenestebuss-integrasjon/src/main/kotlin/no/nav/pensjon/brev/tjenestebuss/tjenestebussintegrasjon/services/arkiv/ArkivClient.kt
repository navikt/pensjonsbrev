package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.STSSercuritySOAPHandler
import no.nav.virksomhet.tjenester.arkiv.v1.Arkiv
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import javax.xml.namespace.QName

class ArkivClient(config: Config, securityHandler: STSSercuritySOAPHandler) {

    private val tjenestebussUrl = config.getString("url")
    private val jaxWsProxyFactoryBean = JaxWsProxyFactoryBean().apply {
        val name = "ArkivWSEXP_ArkivHttpService"
        val portName = "ArkivWSEXP_ArkivHttpPort"
        val namespace = "http://nav.no/virksomhet/tjenester/arkiv/v1/Binding"
        address = "${tjenestebussUrl}nav-tjeneste-arkiv_v1Web/sca/ArkivWSEXP"
        wsdlURL = "wsdl/nav-tjeneste-arkiv_ArkivWSEXP.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = Arkiv::class.java
        handlers = listOf(securityHandler)
        features = listOf(WSAddressingFeature())        //TODO add Logging feature?
    }
    fun arkivClient(): Arkiv = jaxWsProxyFactoryBean.create() as Arkiv
}