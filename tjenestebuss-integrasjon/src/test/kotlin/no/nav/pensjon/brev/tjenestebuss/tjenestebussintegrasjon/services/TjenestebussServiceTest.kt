package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.hasElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.ClientFactory
import no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.soap.TjenestebussService
import org.apache.cxf.feature.Feature
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean
import org.apache.cxf.jaxws.JaxWsServerFactoryBean
import java.util.*
import javax.jws.WebService
import javax.xml.ws.handler.Handler
import javax.xml.ws.handler.MessageContext
import javax.xml.ws.handler.soap.SOAPMessageContext
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class TjenestebussServiceTest {
    private val callIdReceiver = CallIdReceiver()

    private val server =
        JaxWsServerFactoryBean().apply {
            serviceClass = HelloWorld::class.java
            address = "http://localhost:9999/helloWorld"
            serviceBean = HelloWorldProvider()
            handlers = listOf(callIdReceiver)
        }.create()

    private val service = HelloWorldService()

    @AfterTest
    fun stopServer() {
        server.stop()
    }

    @Test
    fun `callId header is added`() {
        service.sayHi()
        assertThat(callIdReceiver.callIds.last(), has(String::length, greaterThan(10)))
    }

    @Test
    fun `callId header is added with provided value`() {
        val callId = "Our provided call id"
        runBlocking {
            service.withCallId(callId) {
                service.sayHi()
            }
        }
        assertThat(callIdReceiver.callIds.last(), equalTo(callId))
    }

    @Test
    fun `ping works`() {
        assertTrue { runBlocking { service.ping()!! } }
    }

    @Test
    fun `first ping results in a sendPing`() {
        val callId = UUID.randomUUID().toString()
        val service = HelloWorldService()

        assertTrue {
            runBlocking {
                service.withCallId(callId) {
                    service.ping()!!
                }
            }
        }

        assertThat(callIdReceiver.callIds, hasElement(callId))
    }

    @Test
    fun `second ping does not sendPing`() {
        val service = HelloWorldService()

        assertTrue { runBlocking { service.ping()!! } }

        val callId = UUID.randomUUID().toString()
        assertTrue {
            runBlocking {
                service.withCallId(callId) {
                    service.ping()!!
                }
            }
        }

        assertThat(callIdReceiver.callIds, !hasElement(callId))
    }

    @Test
    fun `ping results in sendPing if expired`() {
        val service = HelloWorldService(pingExpiration = 1.milliseconds)

        assertTrue { runBlocking { service.ping()!! } }

        val callId = UUID.randomUUID().toString()
        assertTrue {
            runBlocking {
                delay(2)
                service.withCallId(callId) {
                    service.ping()!!
                }
            }
        }

        assertThat(callIdReceiver.callIds, hasElement(callId))
    }
}

@WebService
interface HelloWorld {
    fun sayHi(): String
}

@WebService(
    endpointInterface = "no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.services.HelloWorld",
    serviceName = "HelloWorld",
)
private class HelloWorldProvider : HelloWorld {
    override fun sayHi(): String {
        return "Hello World"
    }
}

private class HelloWorldClientFactory : ClientFactory<HelloWorld> {
    override fun create(
        handlers: List<Handler<SOAPMessageContext>>,
        features: List<Feature>,
    ): HelloWorld =
        JaxWsProxyFactoryBean().apply {
            serviceClass = HelloWorld::class.java
            address = "http://localhost:9999/helloWorld"
            this.handlers = handlers
        }.create(HelloWorld::class.java)
}

private class HelloWorldService(pingExpiration: Duration = 5.minutes) : TjenestebussService<HelloWorld>(HelloWorldClientFactory(), pingExpiration) {
    override fun sendPing(): Boolean = client.sayHi().let { true }

    override val name = "testeson"

    fun sayHi(): String = client.sayHi()
}

private class CallIdReceiver : Handler<SOAPMessageContext> {
    private val _receivedCallIds = mutableListOf<String>()
    val callIds: List<String>
        get() = _receivedCallIds

    override fun handleMessage(context: SOAPMessageContext?): Boolean {
        if (context?.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY) == false) {
            context.message?.soapPart?.envelope?.header?.childElements
                ?.asSequence()
                ?.firstOrNull { it.localName == "callId" }
                ?.firstChild?.textContent
                ?.also { _receivedCallIds.add(it) }
        }
        return true
    }

    override fun handleFault(context: SOAPMessageContext?): Boolean = true

    override fun close(context: MessageContext?) {}
}
