package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import no.nav.pensjon.brev.maler.EksempelBrev
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
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
        assertThat(TemplateResource.getTemplates(), hasElement(EksempelBrev.template.name) and hasElement(OmsorgEgenAuto.template.name))
    }

    //TODO: Test at alle templates kan rendres (krever at alle templates har testdata)

}