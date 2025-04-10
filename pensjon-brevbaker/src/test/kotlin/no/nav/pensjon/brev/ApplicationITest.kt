package no.nav.pensjon.brev

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import no.nav.brev.brevbaker.BREVBAKER_URL
import no.nav.brev.brevbaker.Fixtures
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brevbaker.api.model.LanguageCode
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

    @Test
    fun `response includes deserialization error`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/pdf") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                {
                    "kode": "${LetterExample.kode.kode()}"
                }
            """.trimIndent()
            )
        }
        assertThat(response.bodyAsText(), containsSubstring("value failed for JSON property "))
    }

    @Test
    fun `response includes letterData deserialization error`() = testBrevbakerApp { client ->
        val response = client.post("/letter/autobrev/pdf") {
            contentType(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = LetterExample.kode,
                    letterData = EmptyBrevdata,
                    felles = Fixtures.fellesAuto,
                    language = LanguageCode.BOKMAL
                )
            )
        }
        assertThat(response.bodyAsText(), containsSubstring("Missing required creator property"))
    }
}