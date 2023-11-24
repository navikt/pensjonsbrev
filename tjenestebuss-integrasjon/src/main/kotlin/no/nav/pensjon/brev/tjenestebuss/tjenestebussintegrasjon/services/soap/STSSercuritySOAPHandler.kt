package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap

import kotlinx.coroutines.runBlocking
import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import java.util.Base64
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.soap.SOAPElement
import javax.xml.soap.SOAPFactory
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.MessageContext
import javax.xml.ws.handler.soap.SOAPMessageContext

class STSSercuritySOAPHandler(private val stsService: STSService) : Handler<SOAPMessageContext> {
    private val SECURITY_URL = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
    private val base64Decoder = Base64.getDecoder()

    override fun handleMessage(context: SOAPMessageContext?): Boolean {
        val token = runBlocking {
            stsService.getToken()
        }
        val decodedToken: String = base64Decoder.decode(token.access_token).decodeToString()

        return context?.message?.soapPart?.envelope?.apply {
            if (header == null) {
                addHeader()
            }
            header.addChildElement(generateSecurityHeader(decodedToken))
        } != null
    }

    private fun generateSecurityHeader(decodedToken: String): SOAPElement =
        SOAPFactory.newInstance().run {
            createElement(createName("Security", "wsse", SECURITY_URL)).apply {
                appendChild(ownerDocument.importNode(convertStringToDocument(decodedToken).firstChild, true))
                addAttribute(createName("mustUnderstand", "soapenv", "http://www.w3.org/2003/05/soap-envelope"), "1")
            }
        }

    override fun handleFault(context: SOAPMessageContext?): Boolean {
        return true
    }

    override fun close(context: MessageContext?) {
    }

    private fun convertStringToDocument(xmlStr: String): Document {
        val factory = DocumentBuilderFactory.newInstance()
        factory.isNamespaceAware = true
        try {
            StringReader(xmlStr).use { input ->
                val builder = factory.newDocumentBuilder()
                return builder.parse(InputSource(input))
            }
        } catch (e: ParserConfigurationException) {
            throw IllegalArgumentException("Error parsing SAML assertion", e)
        } catch (e: SAXException) {
            throw IllegalArgumentException("Error parsing SAML assertion", e)
        } catch (e: IOException) {
            throw IllegalArgumentException("Error parsing SAML assertion", e)
        }
    }
}