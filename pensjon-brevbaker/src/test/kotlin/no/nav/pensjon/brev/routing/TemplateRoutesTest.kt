package no.nav.pensjon.brev.routing

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import no.nav.pensjon.brev.api.description
import no.nav.pensjon.brev.api.prodAutobrevTemplates
import no.nav.pensjon.brev.api.prodRedigerbareTemplates
import no.nav.pensjon.brev.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdAuto
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.TemplateModelSpecification
import no.nav.pensjon.brev.template.render.TemplateDocumentation
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.TemplateDescription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateRoutesTest {

    @Test
    fun isAlive() = testBrevbakerApp { client ->
        val response = client.get("/isAlive")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Alive!", response.bodyAsText())
    }

    @Test
    fun `can get names of all autobrev`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/autobrev")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(prodAutobrevTemplates.map { it.kode.name }.toSet(), response.body<Set<String>>())
    }

    @Test
    fun `can get names of all redigerbar`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/redigerbar")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(prodRedigerbareTemplates.map { it.kode.name }.toSet(), response.body<Set<String>>())
    }

    @Test
    fun `can get description of autobrev`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/autobrev/${OmsorgEgenAuto.kode.name}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(OmsorgEgenAuto.template.description(), response.body<TemplateDescription>())
    }

    @Test
    fun `can get description of redigerbar`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/redigerbar/${InformasjonOmSaksbehandlingstid.kode.name}")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(InformasjonOmSaksbehandlingstid.template.description(), response.body<TemplateDescription>())
    }

    @Test
    fun `can get modelSpecification of autobrev`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/autobrev/${OmsorgEgenAuto.kode.name}/modelSpecification")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(OmsorgEgenAuto.template.modelSpecification, response.body<TemplateModelSpecification>())
    }

    @Test
    fun `can get modelSpecification of redigerbar`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/redigerbar/${InformasjonOmSaksbehandlingstid.kode.name}/modelSpecification")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(InformasjonOmSaksbehandlingstid.template.modelSpecification, response.body<TemplateModelSpecification>())
    }

    @Test
    fun `can get template documentation of autobrev`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/autobrev/${ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.kode.name}/doc/BOKMAL")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(TemplateDocumentationRenderer.render(ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template, Language.Bokmal), response.body<TemplateDocumentation>())
    }

    @Test
    fun `can get template documentation of redigerbar`() = testBrevbakerApp { client ->
        val response = client.get("/v2/templates/redigerbar/${InformasjonOmSaksbehandlingstid.kode.name}/doc/BOKMAL")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(TemplateDocumentationRenderer.render(InformasjonOmSaksbehandlingstid.template, Language.Bokmal), response.body<TemplateDocumentation>())
    }

}