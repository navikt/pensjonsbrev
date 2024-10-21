package no.nav.pensjon.brev.skribenten.context

import io.ktor.callid.*
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class CallIdFromContextTest {

    private val clientWithCallIdlugin = HttpClient(MockEngine { respond("CallId: ${it.headers[HttpHeaders.XRequestId]}") }) { install(CallIdFromContext) }

    @Test
    fun `feiler om callId ikke er i context`(): Unit = runBlocking {
        assertThrows<CoroutineContextValueException> {
            clientWithCallIdlugin.get("/something")
        }
    }

    @Test
    fun `sender callId som XRequestId`(): Unit = runBlocking {
        withCallId("et veldig kult kall") {
            assertEquals("CallId: et veldig kult kall", clientWithCallIdlugin.get("/something").bodyAsText())
        }
    }
}