package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap

import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.*
import javax.xml.soap.SOAPException
import javax.xml.soap.SOAPFactory
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.MessageContext
import javax.xml.ws.handler.soap.SOAPMessageContext

abstract class TjenestebussService {
    protected val callIdHandler = CallIdSoapHandler()

    suspend fun <T> withCallId(callId: String?, block: suspend CoroutineScope.() -> T): T =
        withContext(callIdHandler.callId.asContextElement(callId), block)

    class CallIdSoapHandler : Handler<SOAPMessageContext> {
        private val logger = LoggerFactory.getLogger(this::class.java)

        val callId = ThreadLocal<String?>()
        override fun handleMessage(context: SOAPMessageContext?): Boolean {
            try {
                val sFactory = SOAPFactory.newInstance()
                context?.message?.soapPart?.envelope?.let {
                    it.header ?: it.addHeader()
                }?.addChildElement(
                    sFactory.createElement(
                        sFactory.createName(
                            "callId",
                            "",
                            "uri:no.nav.applikasjonsrammeverk"
                        )
                    ).apply { addTextNode(callId.get() ?: UUID.randomUUID().toString()) }
                )
                return true
            } catch (e: SOAPException) {
                logger.error("Error with the SOAP envelope/header!", e)
                return false
            } catch (e: Exception) {
                logger.error("Error when creating CallId-header", e)
                return false
            }
        }

        override fun handleFault(context: SOAPMessageContext?): Boolean = true

        override fun close(context: MessageContext?) {}
    }
}

suspend fun <T : TjenestebussService, R> PipelineContext<Unit, ApplicationCall>.withCallId(
    service: T,
    block: suspend T.() -> R
): R =
    service.withCallId(call.callId) {
        service.block()
    }