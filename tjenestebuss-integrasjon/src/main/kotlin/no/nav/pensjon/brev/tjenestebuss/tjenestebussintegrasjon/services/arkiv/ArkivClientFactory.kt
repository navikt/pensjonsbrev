package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.arkiv

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.ClientFactory
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.virksomhet.tjenester.arkiv.v1.Arkiv
import org.apache.cxf.feature.Feature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import javax.xml.namespace.QName
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.soap.SOAPMessageContext

class ArkivClientFactory(config: Config, private val securityHandler: STSSercuritySOAPHandler): ClientFactory<Arkiv> {

    private val tjenestebussUrl = config.getString("url")

    override fun create(handlers: List<Handler<SOAPMessageContext>>, features: List<Feature>): Arkiv = JaxWsProxyFactoryBean().apply {
        val name = "ArkivWSEXP_ArkivHttpService"
        val portName = "ArkivWSEXP_ArkivHttpPort"
        val namespace = "http://nav.no/virksomhet/tjenester/arkiv/v1/Binding"
        address = "${tjenestebussUrl}nav-tjeneste-arkiv_v1Web/sca/ArkivWSEXP"
        wsdlURL = "wsdl/nav-tjeneste-arkiv_ArkivWSEXP.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = Arkiv::class.java
        this.handlers = handlers + securityHandler
        this.features = listOf(WSAddressingFeature())
    }.create(Arkiv::class.java)
}