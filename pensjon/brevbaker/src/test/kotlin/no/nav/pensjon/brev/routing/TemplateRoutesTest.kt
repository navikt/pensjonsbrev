package no.nav.pensjon.brev.routing

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.pensjon.brev.alleAutobrevmaler
import no.nav.pensjon.brev.alleRedigerbareMaler
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdAuto
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.maler.example.OverstyrtModelSpecificationTemplate
import no.nav.pensjon.brev.maler.redigerbar.BrukerTestBrev
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.brevbakerJacksonObjectMapper
import no.nav.pensjon.brev.template.render.TemplateDocumentation
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brev.template.render.TemplateTextExtractor
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Isolated
import java.util.zip.GZIPInputStream

class TemplateRoutesTest {

    @Test
    fun isAlive() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.get("/isAlive")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Alive!", response.bodyAsText())
    }

    @Test
    fun `can get names of all autobrev`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.get("/templates/autobrev")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(alleAutobrevmaler.map { it.kode.kode() }.toSet(), response.body<Set<String>>())
    }

    @Test
    fun `brevkoder of all redigerbare brev are unique`() =
        assertEquals(
            alleRedigerbareMaler
                .map { it.kode.kode() }.distinct().toSet().size, alleRedigerbareMaler.size, "Alle redigerbare maler skal ha unike brevkoder"
        )

    @Test
    fun `brevkoder of all autobrev are unique`() =
        assertEquals(
            alleAutobrevmaler
                .map { it.kode.kode() }.distinct().toSet().size, alleAutobrevmaler.size, "Alle autobrev maler skal ha unike brevkoder"
        )


    @Test
    fun `can get description of all autobrev`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.get("/templates/autobrev?includeMetadata=true")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(alleAutobrevmaler.map { it.description() }, response.body<List<TemplateDescription.Autobrev>>())
    }

    // Disse testene må kjøre for seg sjøl for å ikke bli forvirra av parallellisering
    // Burde vel egentlig kunne løses med resourcelock
    @Isolated
    class Redigerbar {
        @Test
        fun `can get description of all redigerbar`() = testBrevbakerApp(enableAllToggles = true, isIntegrationTest = false) { client ->
            val response = client.get("/templates/redigerbar?includeMetadata=true")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(
            alleRedigerbareMaler.map { it.description() }, response.body<List<TemplateDescription.Redigerbar>>()
            )
        }

        @Test
        fun `can get names of all redigerbar`() = testBrevbakerApp(enableAllToggles = true, isIntegrationTest = false) { client ->
            val response = client.get("/templates/redigerbar")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(
                alleRedigerbareMaler
                    .map { it.kode.kode() }.toSet(), response.body<Set<String>>()
            )
        }

        @Test
        fun `can get description of redigerbar`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val response = client.get("/templates/redigerbar/${InformasjonOmSaksbehandlingstid.kode.name}")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(InformasjonOmSaksbehandlingstid.description(), response.body<TemplateDescription.Redigerbar>())
        }

        @Test
        fun `har-redigerbare-vedlegg er true for mal med redigerbart vedlegg`() = testBrevbakerApp(enableAllToggles = true, isIntegrationTest = false) { client ->
            val response = client.get("/templates/redigerbar/${BrukerTestBrev.kode.name}/har-redigerbare-vedlegg")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(true, response.body<Boolean>())
        }

        @Test
        fun `har-redigerbare-vedlegg er false for mal uten redigerbart vedlegg`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val response = client.get("/templates/redigerbar/${InformasjonOmSaksbehandlingstid.kode.name}/har-redigerbare-vedlegg")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(false, response.body<Boolean>())
        }
    }

    @Test
    fun `can get description of autobrev`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.get("/templates/autobrev/${OmsorgEgenAuto.kode.name}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(OmsorgEgenAuto.description(), response.body<TemplateDescription.Autobrev>())
    }

    @Test
    fun `can get modelSpecification of autobrev`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.get("/templates/autobrev/${OmsorgEgenAuto.kode.name}/modelSpecification")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(OmsorgEgenAuto.template.modelSpecification(), response.body<TemplateModelSpecification>())
    }

    @Test
    fun `can get modelSpecification of redigerbar`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response =
            client.get("/templates/redigerbar/${InformasjonOmSaksbehandlingstid.kode.name}/modelSpecification")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            InformasjonOmSaksbehandlingstid.template.modelSpecification(),
            response.body<TemplateModelSpecification>()
        )
    }

    @Test
    fun `can get overriden modelSpecification of redigerbar`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response =
            client.get("/templates/redigerbar/${OverstyrtModelSpecificationTemplate.kode.name}/modelSpecification")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            OverstyrtModelSpecificationTemplate.modelSpecification,
            response.body<TemplateModelSpecification>()
        )
    }

    @Test
    fun `can get template documentation of autobrev`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response =
            client.get("/templates/autobrev/${ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.kode.name}/doc/BOKMAL")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            TemplateDocumentationRenderer.render(
                ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
                Language.Bokmal,
                ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template.modelSpecification()
            ), response.body<TemplateDocumentation>()
        )
    }

    @Test
    fun `can get template documentation of redigerbar`() = testBrevbakerApp(isIntegrationTest = false) { client ->
        val response = client.get("/templates/redigerbar/${InformasjonOmSaksbehandlingstid.kode.name}/doc/BOKMAL")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            TemplateDocumentationRenderer.render(
                InformasjonOmSaksbehandlingstid.template,
                Language.Bokmal,
                InformasjonOmSaksbehandlingstid.template.modelSpecification()
            ), response.body<TemplateDocumentation>()
        )
    }

    @Test
    fun `filtrerer bort deaktiverte maler`() = testBrevbakerApp(enableAllToggles = false, isIntegrationTest = false) { client ->
        val response = client.get("/templates/redigerbar?includeMetadata=true")
        assertEquals(HttpStatusCode.OK, response.status)
        val navn = response.body<List<LinkedHashMap<*, *>>>().map { it["name"] }
        assertNull(navn.firstOrNull { it == "PE_OVERSETTELSE_AV_DOKUMENTER" })
        assertNull(navn.firstOrNull { it == "UT_AVSLAG_UFOERETRYGD" })
    }

    /** Dokumentasjon for alle autobrevmaler på alle språk, i ett kall. */
    @Nested
    inner class BatchDoc {

        @Test
        fun `har en oppfoering per autobrevmal per spraak malen stoetter`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val body = client.get("/templates/autobrev/all").body<List<SearchableContent>>()

            val forventet = alleAutobrevmaler.flatMap { mal ->
                mal.template.language.all().map { mal.kode.kode() to it.toCode() }
            }.toSet()
            assertEquals(forventet, body.map { it.brevkode to it.language }.toSet())
        }

        @Test
        fun `linjene er de samme som dokumentasjonsendepunktet for malen gir`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val body = client.get("/templates/autobrev/all").body<List<SearchableContent>>()

            val mal = body.first { it.brevkode == ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.kode.name && it.language == LanguageCode.BOKMAL }
            assertEquals(
                TemplateTextExtractor.extract(
                    TemplateDocumentationRenderer.render(
                        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
                        Language.Bokmal,
                        TemplateModelSpecification(types = emptyMap(), letterModelTypeName = null),
                    ),
                ),
                mal.lines,
            )
        }

        @Test
        fun `alle linjer har minst ett segment`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val body = client.get("/templates/autobrev/all").body<List<SearchableContent>>()

            assertTrue(body.all { innhold -> innhold.lines.all { it.segments.isNotEmpty() } })
        }

        @Test
        fun `svarer med en ETag`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val response = client.get("/templates/autobrev/all")

            assertTrue(!response.headers[HttpHeaders.ETag].isNullOrBlank())
        }

        @Test
        fun `ETag er stabil mellom kall, siden innholdet ikke endrer seg`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val foerste = client.get("/templates/autobrev/all")
            val andre = client.get("/templates/autobrev/all")

            assertEquals(foerste.headers[HttpHeaders.ETag], andre.headers[HttpHeaders.ETag])
        }

        @Test
        fun `matchende If-None-Match gir 304 uten body`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val etag = client.get("/templates/autobrev/all").headers[HttpHeaders.ETag]

            val response = client.get("/templates/autobrev/all") { header(HttpHeaders.IfNoneMatch, etag) }

            assertEquals(HttpStatusCode.NotModified, response.status)
        }

        @Test
        fun `utdatert If-None-Match gir hele svaret`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val response = client.get("/templates/autobrev/all") {
                header(HttpHeaders.IfNoneMatch, "\"not-the-current-etag\"")
            }

            assertEquals(HttpStatusCode.OK, response.status)
        }

        @Test
        fun `svarer med Content-Encoding gzip naar klienten godtar gzip`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val response = client.get("/templates/autobrev/all") { header(HttpHeaders.AcceptEncoding, "gzip") }

            assertEquals("gzip", response.headers[HttpHeaders.ContentEncoding])
        }

        @Test
        fun `gzippet body har samme innhold som det ukomprimerte svaret`() = testBrevbakerApp(isIntegrationTest = false) { client ->
            val forventet = client.get("/templates/autobrev/all").body<List<SearchableContent>>()

            val gzippet = client.get("/templates/autobrev/all") { header(HttpHeaders.AcceptEncoding, "gzip") }

            // Ktor sin testklient pakker ikke ut selv, så vi gunzipper og sjekker at innholdet er det samme.
            val utpakket = GZIPInputStream(gzippet.readRawBytes().inputStream()).use { it.readBytes() }
            assertEquals(forventet.toSet(), brevbakerJacksonObjectMapper().readValue<List<SearchableContent>>(utpakket).toSet())
        }
    }
}
