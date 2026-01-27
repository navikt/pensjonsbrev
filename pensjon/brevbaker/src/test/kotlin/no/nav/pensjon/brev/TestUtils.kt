package no.nav.pensjon.brev

import com.fasterxml.jackson.databind.SerializerProvider
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import no.nav.brev.brevbaker.PDFByggerTestContainer
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.template.brevbakerConfig
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer

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
                registerModule(SakstypeModule)
            }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
    block(client)
}
private object SakstypeModule : SimpleModule() {
    private fun readResolve(): Any = SakstypeModule

    init {
        addDeserializer(ISakstype::class.java, SakstypeDeserializer)
    }

    private object SakstypeDeserializer : JsonDeserializer<ISakstype>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): ISakstype =
            TestSakstype(ctxt.readValue(parser, String::class.java))
    }
}

internal class TestSakstype(val name: String) : ISakstype {
    override val kode = name
    override fun equals(other: Any?): Boolean {
        if (other !is ISakstype) return false
        return name == other.kode
    }
    override fun hashCode() = name.hashCode()
    override fun toString() = name
}