package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.ClientFactory
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import org.apache.cxf.feature.Feature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import javax.xml.namespace.QName
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.soap.SOAPMessageContext


/**
 * Soap client for PsakSamhandler service.
 * Benyttes for å finne samhandler og tilhørende tssId, kontakt og adresse-informajson
 *
 */
class PsakSamhandlerClientFactory(
    config: Config,
    private val securityHandler: STSSercuritySOAPHandler,
): ClientFactory<PSAKSamhandler> {
    private val tjenestebussUrl = config.getString("url")

    override fun create(handlers: List<Handler<SOAPMessageContext>>, features: List<Feature>): PSAKSamhandler =
        JaxWsProxyFactoryBean().apply {
            val name = "PSAKSamhandlerWSEXP_PSAKSamhandlerHttpService"
            val portName = "PSAKSamhandlerWSEXP_PSAKSamhandlerHttpPort"
            val namespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf/Binding"
            address = "${tjenestebussUrl}nav-cons-pen-psak-samhandlerWeb/sca/PSAKSamhandlerWSEXP"
            wsdlURL = "wsdl/PSAKSamhandlerWSEXP_PSAKSamhandlerHttp_Service.wsdl"
            serviceName = QName(namespace, name)
            endpointName = QName(namespace, portName)
            serviceClass = PSAKSamhandler::class.java
            this.handlers = handlers + securityHandler
            this.features = features
        }.create(PSAKSamhandler::class.java)

}

