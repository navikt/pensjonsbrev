package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.samhandler

import com.typesafe.config.Config
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.ClientFactory
import no.nav.virksomhet.tjenester.samhandler.v2.binding.Samhandler
import org.apache.cxf.feature.Feature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import java.util.*
import javax.xml.namespace.QName
import javax.xml.soap.SOAPElement
import javax.xml.soap.SOAPFactory
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.MessageContext
import javax.xml.ws.handler.soap.SOAPHandler
import javax.xml.ws.handler.soap.SOAPMessageContext

private const val PASSWORD_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText"
private const val SECURITY_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
private const val SOAP_ENC_URL = "http://schemas.xmlsoap.org/soap/encoding/"
private const val SOAP_ENV_URL = "http://schemas.xmlsoap.org/soap/envelope/"
private const val XML_SCHEMA_URL = "http://www.w3.org/2001/XMLSchema"
private const val XML_SCHEMA_INS_URL = "http://www.w3.org/2001/XMLSchema-instance"
private const val WSSE = "wsse"

class SamhandlerClientFactory(config: Config) : ClientFactory<Samhandler> {
    private val samhandlerClientUrl = config.getString("url")
    private val samhandlerUsername = config.getString("username")
    private val samhandlerPassword = config.getString("password")

    override fun create(
        handlers: List<Handler<SOAPMessageContext>>,
        features: List<Feature>,
    ): Samhandler =
        JaxWsProxyFactoryBean().apply {
            val name = "Samhandler"
            val portName = "SamhandlerPort"
            val namespace = "http://nav.no/virksomhet/tjenester/samhandler/v2/Binding/"
            address = "${samhandlerClientUrl}services/tss/hentSamhandler"
            wsdlURL = "wsdl/no/nav/virksomhet/tjenester/samhandler/v2/Binding/Binding.wsdl"
            serviceName = QName(namespace, name)
            endpointName = QName(namespace, portName)
            serviceClass = Samhandler::class.java
            this.handlers = handlers + (BasicAuthSoapSecurityHandler(samhandlerUsername, samhandlerPassword))
            this.features = features
        }.create(Samhandler::class.java)
}

class BasicAuthSoapSecurityHandler(private val username: String, private val password: String) : SOAPHandler<SOAPMessageContext> {
    override fun getHeaders(): Set<QName> {
        return PROCESSED_HEADERS_QNAME
    }

    override fun handleMessage(context: SOAPMessageContext): Boolean {
        return context.message.soapPart.envelope.apply {
            if (header == null) {
                addHeader()
            }
            header.addChildElement(createBasicAuth(username, password))
        } != null
    }

    private fun createBasicAuth(
        user: String?,
        password: String?,
    ): SOAPElement? {
        if (user == null || password == null) {
            return null
        }

        val sFactory = SOAPFactory.newInstance()

        val userElement =
            sFactory.createElement(sFactory.createName("Username", WSSE, SECURITY_URL))
                .apply { addTextNode(user) }
        val passwordElement =
            sFactory.createElement(sFactory.createName("Password", WSSE, SECURITY_URL))
                .apply {
                    addAttribute(sFactory.createName("Type"), PASSWORD_TYPE)
                    addTextNode(password)
                }
        val userToken =
            sFactory.createElement(sFactory.createName("UsernameToken", WSSE, SECURITY_URL))
                .apply {
                    addChildElement(userElement)
                    addChildElement(passwordElement)
                }

        val header =
            sFactory.createElement(sFactory.createName("Security", WSSE, SECURITY_URL)).apply {
                addNamespaceDeclaration("soapenc", SOAP_ENC_URL)
                addNamespaceDeclaration("xsd", XML_SCHEMA_URL)
                addNamespaceDeclaration("xsi", XML_SCHEMA_INS_URL)
                val mustName = sFactory.createName("mustUnderstand", "soapenv", SOAP_ENV_URL)
                addAttribute(mustName, "1")
                addChildElement(userToken)
            }
        return header
    }

    override fun handleFault(context: SOAPMessageContext?): Boolean = true

    override fun close(context: MessageContext) {}

    companion object {
        private val SECURITY_QNAME = QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security")
        private val PROCESSED_HEADERS_QNAME: Set<QName> = Collections.unmodifiableSet(setOf(SECURITY_QNAME))
    }
}
