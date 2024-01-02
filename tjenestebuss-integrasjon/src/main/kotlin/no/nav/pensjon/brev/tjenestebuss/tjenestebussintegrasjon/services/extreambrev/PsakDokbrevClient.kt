package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.extreambrev

import com.typesafe.config.Config
import no.nav.inf.psak.dokbrev.PSAKDokBrev
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.STSSercuritySOAPHandler
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.ws.addressing.WSAddressingFeature
import javax.xml.namespace.QName


/**
 * PsakDokbrevClient er en soap klient for PSAK Dokbrev.
 * Benyttes for Extream brev
 */
class PsakDokbrevClient(
    config: Config,
    securityHandler: STSSercuritySOAPHandler,
    callIdSoapHandler: TjenestebussService.CallIdSoapHandler
) {
    private val tjenestebussUrl = config.getString("url")
    private val jaxWsProxyFactoryBean = JaxWsProxyFactoryBean().apply {
        val name = "PSAKDokBrevSCAEXP_PSAKDokBrevHttpService"
        val portName = "PSAKDokBrevSCAEXP_PSAKDokBrevHttpPort"
        val namespace = "http://nav-cons-pen-psak-dokbrev/no/nav/inf/PSAKDokBrev/Binding"
        address = "${tjenestebussUrl}nav-cons-pen-psak-dokbrevWeb/sca/PSAKDokBrevSCAEXP"
        wsdlURL = "wsdl/PSAKDokBrevSCAEXP_PSAKDokBrevHttp_Service.wsdl"
        serviceName = QName(namespace, name)
        endpointName = QName(namespace, portName)
        serviceClass = PSAKDokBrev::class.java
        handlers = listOf(securityHandler, callIdSoapHandler)
        features = listOf(WSAddressingFeature())
    }

    fun client(): PSAKDokBrev = jaxWsProxyFactoryBean.create() as PSAKDokBrev
}