package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import no.nav.pensjon.brev.maler.EksempelBrev
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.template.LetterTemplate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createInstance

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

    @Test
    fun `all names returned by getTemplates can be fetched with getTemplate`() {
        val templateNames = TemplateResource.getTemplates().toSet()
        val templates = templateNames
            .map { TemplateResource.getTemplate(it) }
            .filterNotNull()
            .map { it.name }
            .toSet()

        assertEquals(templateNames, templates)
    }

    @Test
    fun `all templates have letterDataType which are data class`() {
        val templatesWithoutDataClass: Map<String, LetterTemplate<*, *>> = TemplateResource.getTemplates()
            .associateWith { TemplateResource.getTemplate(it)!! }
            .filterValues { !it.letterDataType.isData }

        assertEquals(emptySet<String>(), templatesWithoutDataClass.keys)
    }

    @Test
    fun `all templates have letterDataType with no-arg constructor`() {
        val templatesWithoutNoArgConstructor = TemplateResource.getTemplates()
            .associateWith { TemplateResource.getTemplate(it)!! }
            .mapValues {
                try {
                    it.value.letterDataType.createInstance()
                } catch (e: Exception) {
                    null
                }
            }.filterValues { it == null }
            .keys

        assertEquals(emptySet<String>(), templatesWithoutNoArgConstructor, "letterDataType classes should have an internal no-arg constructor with valid test data.")
    }

}