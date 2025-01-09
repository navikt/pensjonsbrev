package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap

import io.ktor.server.plugins.callid.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.withContext
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.ServiceStatus
import org.apache.cxf.feature.Feature
import org.apache.cxf.ws.addressing.WSAddressingFeature
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import jakarta.xml.soap.SOAPException
import jakarta.xml.soap.SOAPFactory
import jakarta.xml.ws.handler.Handler
import jakarta.xml.ws.handler.MessageContext
import jakarta.xml.ws.handler.soap.SOAPMessageContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

interface ClientFactory<Client> {
    fun create(handlers: List<Handler<SOAPMessageContext>>, features: List<Feature>): Client
}

abstract class TjenestebussService<Client>(factory: ClientFactory<Client>, pingExpiration: Duration = 10.minutes) : ServiceStatus {
    private val callIdHandler = CallIdSoapHandler()
    private val successfulResponseHandler = SuccessfulResponseHandler()
    private val pingExpiration = pingExpiration.toJavaDuration()

    protected val client: Client = factory.create(
        handlers = listOf(callIdHandler, successfulResponseHandler),
        features = listOf(WSAddressingFeature())
    )

    abstract fun sendPing(): Boolean?

    override suspend fun ping(): Boolean? =
        if (successfulResponseHandler.successfulResponseAt?.isAfter(Instant.now() - pingExpiration) == true) {
            true
        } else sendPing()

    suspend fun <T> withCallId(callId: String?, block: suspend CoroutineScope.() -> T): T =
        withContext(callIdHandler.callId.asContextElement(callId), block)

    class CallIdSoapHandler : Handler<SOAPMessageContext> {
        private val logger = LoggerFactory.getLogger(this::class.java)

        val callId = ThreadLocal<String?>()
        override fun handleMessage(context: SOAPMessageContext?): Boolean {
            if (context?.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) == true) {
                try {
                    val sFactory = SOAPFactory.newInstance()
                    context.message?.soapPart?.envelope?.let {
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
            return true
        }

        override fun handleFault(context: SOAPMessageContext?): Boolean = true

        override fun close(context: MessageContext?) {}
    }

    class SuccessfulResponseHandler : Handler<SOAPMessageContext> {
        private var _successfulResponseAt: Instant? = null
        val successfulResponseAt: Instant?
            get() = _successfulResponseAt

        override fun handleMessage(context: SOAPMessageContext?): Boolean {
            if (context?.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) == false) {
                _successfulResponseAt = Instant.now()
            }
            return true
        }

        override fun handleFault(context: SOAPMessageContext?): Boolean = true
        override fun close(context: MessageContext?) {}
    }
}

suspend fun <T : TjenestebussService<*>, R> RoutingContext.withCallId(
    service: T,
    block: suspend T.() -> R
): R =
    service.withCallId(call.callId) {
        service.block()
    }