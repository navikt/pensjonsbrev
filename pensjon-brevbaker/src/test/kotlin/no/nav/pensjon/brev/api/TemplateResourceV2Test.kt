package no.nav.pensjon.brev.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.latex.LaTeXCompilerService
import no.nav.pensjon.brev.template.brevbakerConfig
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.INTEGRATION_TEST)
class TemplateResourceV2Test {

    @Test
    fun isAlive() = testBrevbakerApp {
        val response = client.get("/isAlive")
        
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `render pdf responds according to content-type`() = testBrevbakerApp {
        val response = it.post("/v2/templates/autobrev/OMSORG_EGEN_AUTO/letter/html") {
            accept(ContentType.Application.Json)
            setBody(BestillBrevRequest(
                letterData = Fixtures.create<OmsorgEgenAutoDto>(),
                felles = Fixtures.fellesAuto,
                language = LanguageCode.BOKMAL,
            ))
        }
        val message = response.bodyAsText()
        println(message)
        assertEquals(response.body<LetterResponse.V2>().contentType, ContentType.Text.Html.withCharset(Charsets.UTF_8).toString())
    }

    private fun testBrevbakerApp(block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit): Unit = testApplication {
        environment {
            config = MapApplicationConfig(
                "brevbaker.pdfByggerUrl" to "http://localhost:8081",
            )
        }

        application {
            install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                jackson { brevbakerConfig() }
            }
            routing {
                val latexCompilerService = LaTeXCompilerService(
                    pdfByggerUrl = "http://localhost:8081",
                    maxRetries = 30,
                )

                templateRoutes(TemplateResourceV2("autobrev", prodAutobrevTemplates, latexCompilerService))
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                jackson {
                    brevbakerConfig()
                }
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }

        block(client)
    }
}