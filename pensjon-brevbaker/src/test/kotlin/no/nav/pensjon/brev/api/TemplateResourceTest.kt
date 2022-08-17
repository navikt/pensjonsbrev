package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateResourceTest {

    private val templateResource = TemplateResource(productionTemplates)

    @Test
    fun `getTemplate fetches template`() {
        assertEquals(OmsorgEgenAuto.template, templateResource.getTemplate(OmsorgEgenAuto.kode))
    }

    @Test
    fun `getTemplates returns list of template names`() {
        assertThat(
            templateResource.getTemplates(),
            hasElement(OmsorgEgenAuto.kode) and hasElement(UngUfoerAuto.kode)
        )
    }

    @Test
    fun `all names returned by getTemplates can be fetched with getTemplate`() {
        val templateNames = templateResource.getTemplates().toSet()
        val templates = templateNames
            .map { templateResource.getTemplate(it) }
            .filterNotNull()
            .map { Brevkode.Vedtak.valueOf(it.name) }
            .toSet()

        assertEquals(templateNames, templates)
    }

    @Test
    fun `all templates have letterDataType which are data class`() {
        val templatesWithoutDataClass: Map<Brevkode.Vedtak, LetterTemplate<*, *>> = templateResource.getTemplates()
            .associateWith { templateResource.getTemplate(it)!! }
            .filterValues { !it.letterDataType.isData }

        assertEquals(emptySet<Brevkode.Vedtak>(), templatesWithoutDataClass.keys)
    }

    @Test
    fun `all templates have letterDataType that can be created`() {
        val templatesWithoutSampleData = templateResource.getTemplates()
            .associateWith { templateResource.getTemplate(it)!! }
            .mapValues {
                try {
                    Fixtures.create(it.value.letterDataType)
                    null
                } catch (e: IllegalArgumentException) {
                    e.message
                }
            }.filterValues { it != null }

        assertEquals(
            emptyMap<Brevkode.Vedtak, String>(),
            templatesWithoutSampleData,
            "letterDataType classes must be constructable by Fixtures.create."
        )
    }

    @Test
    fun `all template letterDataType can be serialized and deserialized`() {
        val jackson = jacksonObjectMapper()
        templateResource.getTemplates()
            .map { templateResource.getTemplate(it)!! }
            .forEach {
                val data = Fixtures.create(it.letterDataType)
                val json = jackson.writeValueAsString(data)
                val deserialized = jackson.readValue(json, it.letterDataType.java)

                assertEquals(data, deserialized)
            }
    }

}