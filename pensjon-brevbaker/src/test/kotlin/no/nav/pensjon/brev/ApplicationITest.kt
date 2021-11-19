package no.nav.pensjon.brev

import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.PDF_BYGGER)
class ApplicationITest {
    @Test
    fun `ping brevbaker container`() {
        runBlocking {
            httpClient.get<String>("$BREVBAKER_URL/isAlive")
        }
    }

}