package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.dokumentsproduksjon

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v3.DokumentproduksjonV3
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import javax.xml.namespace.QName

class DokumentProduksjonClient(config: Config, securityHandler: STSSercuritySOAPHandler, callIdSoapHandler: TjenestebussService.CallIdSoapHandler) {

    private val dokprodUrl = config.getString("url")
    private val jaxWsProxyFactoryBean = JaxWsProxyFactoryBean().apply {
        val name = "Dokumentproduksjon_v3"
        val portName = "Dokumentproduksjon_v3Port"
        val namespace = "http://nav.no/tjeneste/virksomhet/dokumentproduksjon/v3/Binding"
        address = "${dokprodUrl}dokprod/ws/dokumentproduksjon/v3"
        wsdlURL = "wsdl/no/nav/tjeneste/virksomhet/dokumentproduksjon/v3/Binding.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = DokumentproduksjonV3::class.java
        handlers = listOf(securityHandler, callIdSoapHandler)
        features = listOf(WSAddressingFeature())
    }
    fun client(): DokumentproduksjonV3 = jaxWsProxyFactoryBean.create() as DokumentproduksjonV3
}