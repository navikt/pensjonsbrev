package no.nav.pensjon.brev.routing

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.zip.GZIPInputStream
import kotlinx.coroutines.runBlocking
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
import no.nav.pensjon.brev.template.render.TemplateTextExtractor
import no.nav.pensjon.brev.template.render.TemplateDocumentation
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brev.template.toCode
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Isolated

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
    fun `batch doc endpoint returns one entry per autobrev template per supported language`() =
        testBrevbakerApp(isIntegrationTest = false) { client ->
            val response = client.get("/templates/autobrev/all")
            assertEquals(HttpStatusCode.OK, response.status)
            val body = response.body<List<SearchableContent>>()

            val expected = alleAutobrevmaler.flatMap { mal ->
                mal.template.language.all().map { mal.kode.kode() to it.toCode() }
            }.toSet()
            assertEquals(expected, body.map { it.brevkode to it.language }.toSet())

            // Lines match the server-side extraction of the per-template documentation.
            val sample = body.first { it.brevkode == ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.kode.name && it.language == LanguageCode.BOKMAL }
            assertEquals(
                TemplateTextExtractor.extract(
                    TemplateDocumentationRenderer.render(
                        ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template,
                        Language.Bokmal,
                        TemplateModelSpecification(types = emptyMap(), letterModelTypeName = null),
                    ),
                ),
                sample.lines,
            )
            // Every segment is a text or var segment (no empty lines).
            assertTrue(sample.lines.all { it.segments.isNotEmpty() })
        }

    @Test
    fun `batch doc endpoint serves a stable ETag and answers 304 to a matching If-None-Match`() =
        testBrevbakerApp(isIntegrationTest = false) { client ->
            val first = client.get("/templates/autobrev/all")
            assertEquals(HttpStatusCode.OK, first.status)
            val etag = first.headers[HttpHeaders.ETag]
            assertTrue(!etag.isNullOrBlank())

            // Content is static at runtime, so the ETag is stable across calls.
            val second = client.get("/templates/autobrev/all")
            assertEquals(etag, second.headers[HttpHeaders.ETag])

            // A matching If-None-Match revalidates cheaply with 304 and no body.
            val notModified = client.get("/templates/autobrev/all") {
                header(HttpHeaders.IfNoneMatch, etag)
            }
            assertEquals(HttpStatusCode.NotModified, notModified.status)
            assertTrue(notModified.bodyAsText().isEmpty())

            // A stale ETag still gets the full payload.
            val stale = client.get("/templates/autobrev/all") {
                header(HttpHeaders.IfNoneMatch, "\"not-the-current-etag\"")
            }
            assertEquals(HttpStatusCode.OK, stale.status)
        }

    @Test
    fun `batch doc endpoint gzip-encodes the body when the client accepts gzip`() =
        testBrevbakerApp(isIntegrationTest = false) { client ->
            val plain = client.get("/templates/autobrev/all")
            val expected = plain.body<List<SearchableContent>>()

            val gzipResponse = client.get("/templates/autobrev/all") {
                header(HttpHeaders.AcceptEncoding, "gzip")
            }
            assertEquals(HttpStatusCode.OK, gzipResponse.status)
            assertEquals("gzip", gzipResponse.headers[HttpHeaders.ContentEncoding])

            // The Ktor test client doesn't auto-decompress, so we gunzip ourselves
            // and verify the payload round-trips to the same content as the plain body.
            val decompressed = GZIPInputStream(gzipResponse.readRawBytes().inputStream()).use { it.readBytes() }
            val actual = brevbakerJacksonObjectMapper().readValue<List<SearchableContent>>(decompressed)
            assertEquals(expected.toSet(), actual.toSet())
        }
}