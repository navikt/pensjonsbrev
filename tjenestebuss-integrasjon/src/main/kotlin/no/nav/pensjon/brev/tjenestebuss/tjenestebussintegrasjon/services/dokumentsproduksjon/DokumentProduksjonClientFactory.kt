package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.ClientFactory
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3
import org.apache.cxf.feature.Feature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import javax.xml.namespace.QName
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.soap.SOAPMessageContext

class DokumentProduksjonClientFactory(config: Config, private val securityHandler: STSSercuritySOAPHandler) : ClientFactory<DokumentproduksjonV3> {

    private val dokprodUrl = config.getString("url")

    override fun create(handlers: List<Handler<SOAPMessageContext>>, features: List<Feature>): DokumentproduksjonV3 = JaxWsProxyFactoryBean().apply {
        val name = "Dokumentproduksjon_v3"
        val portName = "Dokumentproduksjon_v3Port"
        val namespace = "http://nav.no/tjeneste/virksomhet/dokumentproduksjon/v3/Binding"
        address = "${dokprodUrl}dokprod/ws/dokumentproduksjon/v3"
        wsdlURL = "wsdl/no/nav/tjeneste/virksomhet/dokumentproduksjon/v3/Binding.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = DokumentproduksjonV3::class.java
        this.handlers = handlers + securityHandler
        this.features = features
    }.create(DokumentproduksjonV3::class.java)
}