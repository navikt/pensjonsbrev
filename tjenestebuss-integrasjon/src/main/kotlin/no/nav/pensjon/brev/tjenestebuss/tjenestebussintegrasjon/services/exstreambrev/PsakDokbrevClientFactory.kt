package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.exstreambrev

import com.typesafe.config.Config
import no.nav.inf.psak.dokbrev.PSAKDokBrev
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.ClientFactory
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import org.apache.cxf.feature.Feature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import javax.xml.namespace.QName
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.soap.SOAPMessageContext


/**
 * PsakDokbrevClient er en soap klient for PSAK Dokbrev.
 * Benyttes for Exstream brev
 */
class PsakDokbrevClientFactory(
    config: Config,
    private val securityHandler: STSSercuritySOAPHandler,
) : ClientFactory<PSAKDokBrev> {
    private val tjenestebussUrl = config.getString("tjenestebuss.url")

    override fun create(handlers: List<Handler<SOAPMessageContext>>, features: List<Feature>): PSAKDokBrev = JaxWsProxyFactoryBean().apply {
        val name = "PSAKDokBrevSCAEXP_PSAKDokBrevHttpService"
        val portName = "PSAKDokBrevSCAEXP_PSAKDokBrevHttpPort"
        val namespace = "http://nav-cons-pen-psak-dokbrev/no/nav/inf/PSAKDokBrev/Binding"
        address = "${tjenestebussUrl}nav-cons-pen-psak-dokbrevWeb/sca/PSAKDokBrevSCAEXP"
        wsdlURL = "wsdl/PSAKDokBrevSCAEXP_PSAKDokBrevHttp_Service.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = PSAKDokBrev::class.java
        this.handlers = handlers + securityHandler
        this.features = features
    }.create(PSAKDokBrev::class.java)
}