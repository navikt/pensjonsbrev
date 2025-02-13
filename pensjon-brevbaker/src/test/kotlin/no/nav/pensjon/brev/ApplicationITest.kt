package no.nav.pensjon.brev

import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.BREVBAKER_URL
import org.junit.jupiter.api.Test

class ApplicationITest {
    @Test
    fun `ping running brevbaker`() {
        runBlocking {
            try {
                testBrevbakerApp {
                    client.get("isAlive")
                }
            } catch (e: Exception) {
                throw Exception("Failed to ping brevbaker at: $BREVBAKER_URL", e)
            }
        }
    }

}