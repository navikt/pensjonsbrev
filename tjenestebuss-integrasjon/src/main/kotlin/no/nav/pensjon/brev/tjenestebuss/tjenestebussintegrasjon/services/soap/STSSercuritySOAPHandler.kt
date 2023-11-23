package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap

import kotlinx.coroutines.runBlocking
import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import java.time.LocalDateTime
import java.util.*
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
        val testCorrelationId = UUID.randomUUID().toString()

        context?.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, testCorrelationId)

        return context?.message?.soapPart?.envelope?.apply {
            if (header == null) {
                addHeader()
            }
            header.addChildElement(generateSecurityHeader(decodedToken))
        } != null
    }

    private fun generateSecurityHeader(decodedToken: String): SOAPElement {
        val soapFactory = SOAPFactory.newInstance()
        val headerName = soapFactory.createName("Security", "wsse", SECURITY_URL)
        val securityHeader = soapFactory.createElement(headerName)
        val assertion: Document = convertStringToDocument(decodedToken)
        val securityDoc: Document = securityHeader.ownerDocument
        val samlNode = securityDoc.importNode(assertion.firstChild, true)
        securityHeader.appendChild(samlNode)
        val soapenvName = soapFactory.createName("mustUnderstand", "soapenv", "http://www.w3.org/2003/05/soap-envelope")
        securityHeader.addAttribute(soapenvName, "1")
        return securityHeader
    }

    override fun handleFault(context: SOAPMessageContext?): Boolean {
        println(LocalDateTime.now())
        println(context)
        return false
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