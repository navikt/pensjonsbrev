package no.nav.pensjon.brev

import com.fasterxml.jackson.databind.module.SimpleModule
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.converters.addAbstractTypeMapping
import no.nav.pensjon.brev.template.brevbakerConfig

fun testBrevbakerApp(
    enableAllToggles: Boolean = false,
    isIntegrationTest: Boolean = true,
    block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit,
): Unit = testApplication {
    environment {
        val conf = if (isIntegrationTest) "application-integrationtests.conf" else "application.conf"
        config = ApplicationConfig(conf).mergeWith(
            MapApplicationConfig(
                "brevbaker.unleash.fakeUnleashEnableAll" to "$enableAllToggles",
                // else-en her blir aldri brukt, men må være her for å ikke gi oss kompileringsfeil
                "brevbaker.pdfByggerUrl" to if (isIntegrationTest) PDFByggerTestContainer.mappedUrl() else "denne blir ikke brukt",
            )
        )
    }
    val client = createClient {
        install(ContentNegotiation) {
            jackson {
                brevbakerConfig()
                registerModule(BrevkategoriModule)
            }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
    block(client)
}

private object BrevkategoriModule : SimpleModule() {
    private fun readResolve(): Any = BrevkategoriModule

    init {
        addAbstractTypeMapping<TemplateDescription.IBrevkategori, Brevkategori>()
    }
}

@JvmInline
value class Brevkategori(val kategori: String) : TemplateDescription.IBrevkategori {
    override fun kode(): String = kategori
}