package no.nav.pensjon.brev.api

import no.nav.pensjon.brev.maler.Alderspensjon
import no.nav.pensjon.brev.maler.EksempelBrev
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TemplateResourceTest {

    @Test
    fun `getTemplate fetches template`() {
        assertEquals(EksempelBrev.template, TemplateResource.getTemplate(EksempelBrev.template.name))
    }

    @Test
    fun `getTemplate returns null for non-existing template`() {
        assertNull(TemplateResource.getTemplate("non_existing"))
    }

    @Test
    fun `getTemplates returns list of template names`() {
        val expected = listOf(
            EksempelBrev, Alderspensjon
        ).map { it.template.name }.toSet()

        assertTrue(TemplateResource.getTemplates().containsAll(expected))
    }

}