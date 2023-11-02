package no.nav.pensjon.brev.skribenten.services.buss

import com.typesafe.config.Config
import no.nav.inf.psak.samhandler.PSAKSamhandler
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import javax.xml.namespace.QName





private data class MissingWsdlResourceException(override val message: String): Exception(message)
class TjenestebussService(config: Config) {
    private val tjenestebussUrl = config.getConfig("url")
    private val wsdlLocation: String =
        this.javaClass.getResource("wsdl/PSAKSamhandlerWSEXP_PSAKSamhandlerHttp_Service.wsdl")?.toString()
            ?: throw MissingWsdlResourceException("Could not find resource PSAKSamhandlerWSEXP_PSAKSamhandlerHttp_Service.wsdl")


        //fun PersonV3(serviceUrl: String): PersonV3 {
//
    //    return createServicePort(
    //        serviceUrl,
    //        serviceClazz = PersonV3::class.java,
    //        wsdl = "wsdl/no/nav/tjeneste/virksomhet/person/v3/Binding.wsdl",
    //        namespace = "http://nav.no/tjeneste/virksomhet/person/v3/Binding",
    //        svcName = "Person_v3",
    //        portName = "Person_v3Port"
    //    )
    //}
//
    private fun <PORT_TYPE> createServicePort(
        serviceUrl: String,
        serviceClazz: Class<PORT_TYPE>,
        svcName: String,
        portName: String
    ): PORT_TYPE {
        val factory = JaxWsProxyFactoryBean().apply {
            val name = "PSAKSamhandlerWSEXP_PSAKSamhandlerHttpService"
            val namespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf/Binding"
            address = "${tjenestebussUrl}nav-cons-pen-psak-fullmaktWeb/sca/PSAKFullmaktWSEXP"
            wsdlLocation = wsdlURL
            serviceName = QName(namespace, name)
            endpointName = QName(namespace, portName)
            serviceClass = PSAKSamhandler::class.java
            //address = serviceUrl
            //wsdlURL = wsdl
            //serviceName = QName(namespace, svcName)
            //endpointName = QName(namespace, portName)
            //serviceClass = serviceClazz
            //features = listOf(WSAddressingFeature(), LoggingFeature(), /*MetricFeature()*/)
            //outInterceptors.add(CallIdInterceptor())
        }

        return factory.create(serviceClazz)
    }
}

//object TssService {
//    fun psakSamhandlerPort: JaxWsPortProxyFactoryBean(
//        endpoint: String,
//        handlerResolver: HandlerResolver){
//        port: JaxWsPortProxyFactoryBean = new JaxWsPortProxyFactoryBean();
//        port.setWsdlDocumentUrl(new ClassPathResource ("wsdl/PSAKSamhandlerWSEXP_PSAKSamhandlerHttp_Service.wsdl").getURL());
//        port.setNamespaceUri("http://nav-cons-pen-psak-samhandler/no/nav/inf/Binding");
//        port.setPortName("PSAKSamhandlerWSEXP_PSAKSamhandlerHttpPort");
//        port.setServiceName("PSAKSamhandlerWSEXP_PSAKSamhandlerHttpService");
//        port.setServiceInterface(PSAKSamhandler.class);
//        port.setEndpointAddress(endpoint);
//        port.setHandlerResolver(handlerResolver);
//        return port;
//    }
//}
//trenger:
// ASBOPenSamhandlerListe
// ASBOPenFinnSamhandlerRequest
//
// ASBOPenSamhandler
// ASBOPenHentSamhandlerRequest
//


// psak samhandler interface:
//     @WebMethod
//    @RequestWrapper(
//        localName = "finnSamhandler",
//        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
//        className = "no.nav.inf.psak.samhandler.FinnSamhandler"
//    )
//    @ResponseWrapper(
//        localName = "finnSamhandlerResponse",
//        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
//        className = "no.nav.inf.psak.samhandler.FinnSamhandlerResponse"
//    )
//    @WebResult(
//        name = "finnSamhandlerResponse",
//        targetNamespace = ""
//    )
//    ASBOPenSamhandlerListe finnSamhandler(@WebParam(name = "finnSamhandlerRequest",targetNamespace = "") ASBOPenFinnSamhandlerRequest var1) throws FinnSamhandlerFaultPenGeneriskMsg;

// psak samhandler port:
//     @Bean
//
//
//
//
//
//
//
//
//
//
//
//