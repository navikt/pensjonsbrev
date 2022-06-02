package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createInstance

class TemplateResourceTest {

    val templateResource = TemplateResource(productionTemplates + LetterExample)

    @Test
    fun `getTemplate fetches template`() {
        assertEquals(LetterExample.template, templateResource.getTemplate(LetterExample.template.name))
    }

    @Test
    fun `getTemplate returns null for non-existing template`() {
        assertNull(templateResource.getTemplate("non_existing"))
    }

    @Test
    fun `getTemplates returns list of template names`() {
        assertThat(
            templateResource.getTemplates(),
            hasElement(LetterExample.template.name) and hasElement(LetterExample.template.name)
        )
    }

    @Test
    fun `all names returned by getTemplates can be fetched with getTemplate`() {
        val templateNames = templateResource.getTemplates().toSet()
        val templates = templateNames
            .map { templateResource.getTemplate(it) }
            .filterNotNull()
            .map { it.name }
            .toSet()

        assertEquals(templateNames, templates)
    }

    @Test
    fun `all templates have letterDataType which are data class`() {
        val templatesWithoutDataClass: Map<String, LetterTemplate<*, *>> = templateResource.getTemplates()
            .associateWith { templateResource.getTemplate(it)!! }
            .filterValues { !it.letterDataType.isData }

        assertEquals(emptySet<String>(), templatesWithoutDataClass.keys)
    }

    @Test
    fun `all templates have letterDataType with no-arg constructor`() {
        val templatesWithoutNoArgConstructor = templateResource.getTemplates()
            .associateWith { templateResource.getTemplate(it)!! }
            .mapValues {
                try {
                    it.value.letterDataType.createInstance()
                } catch (e: Exception) {
                    null
                }
            }.filterValues { it == null }
            .keys

        assertEquals(
            emptySet<String>(),
            templatesWithoutNoArgConstructor,
            "letterDataType classes should have an internal no-arg constructor with valid test data."
        )
    }

    @Test
    fun `all template letterDataType can be serialized and deserialized`() {
        val jackson = jacksonObjectMapper()
        templateResource.getTemplates()
            .map { templateResource.getTemplate(it)!! }
            .forEach {
                val data = it.letterDataType.createInstance()
                val json = jackson.writeValueAsString(data)
                val deserialized = jackson.readValue(json, it.letterDataType.java)

                println(json)
                assertEquals(data, deserialized)
            }
    }

}