package no.nav.pensjon.brev.api

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.hasElement
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.*
import no.nav.pensjon.brev.maler.redigerbar.InformasjonOmSaksbehandlingstid
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateResourceTest {

    private val templateResource = TemplateResource(prodAutobrevTemplates)

    @Test
    fun `getAutoBrev fetches template`() {
        assertEquals(OmsorgEgenAuto.template, templateResource.getAutoBrev(OmsorgEgenAuto.kode))
    }

    @Test
    fun `getRedigerbartBrev fetches template`() {
        assertEquals(InformasjonOmSaksbehandlingstid.template, templateResource.getRedigerbartBrev(InformasjonOmSaksbehandlingstid.kode))
    }

    @Test
    fun `getAutoBrev returns list of template names`() {
        assertThat(
            templateResource.getAutoBrev(),
            hasElement(OmsorgEgenAuto.kode) and hasElement(UngUfoerAuto.kode)
        )
    }

    @Test
    fun `getRedigerbareBrev returns list of template names`() {
        assertThat(
            templateResource.getRedigerbareBrev(),
            hasElement(InformasjonOmSaksbehandlingstid.kode)
        )
    }

    @Test
    fun `all names returned by getAutoBrev can be fetched with getAutoBrev`() {
        val templateNames = templateResource.getAutoBrev().toSet()
        val templates = templateNames.mapNotNull { templateResource.getAutoBrev(it) }
            .map { Brevkode.AutoBrev.valueOf(it.name) }
            .toSet()

        assertEquals(templateNames, templates)
    }

    @Test
    fun `all names returned by getRedigerbareBrev can be fetched with getRedigerbartBrev`() {
        val templateNames = templateResource.getRedigerbareBrev().toSet()
        val templates = templateNames.mapNotNull { templateResource.getRedigerbartBrev(it) }
            .map { Brevkode.Redigerbar.valueOf(it.name) }
            .toSet()

        assertEquals(templateNames, templates)
    }

    @Test
    fun `all autobrev templates have letterDataType which are data class`() {
        val templatesWithoutDataClass: Map<Brevkode.AutoBrev, LetterTemplate<*, *>> = templateResource.getAutoBrev()
            .associateWith { templateResource.getAutoBrev(it)!! }
            .filterValues { !it.letterDataType.isData }

        assertEquals(emptySet<Brevkode.AutoBrev>(), templatesWithoutDataClass.keys)
    }

    @Test
    fun `all redigerbare templates have letterDataType which are data class`() {
        val templatesWithoutDataClass: Map<Brevkode.Redigerbar, LetterTemplate<*, *>> = templateResource.getRedigerbareBrev()
            .associateWith { templateResource.getRedigerbartBrev(it)!! }
            .filterValues { !it.letterDataType.isData }

        assertEquals(emptySet<Brevkode.Redigerbar>(), templatesWithoutDataClass.keys)
    }

    @Test
    fun `all autobrev templates have letterDataType that can be created`() {
        val templatesWithoutSampleData = templateResource.getAutoBrev()
            .associateWith { templateResource.getAutoBrev(it)!! }
            .mapValues {
                try {
                    Fixtures.create(it.value.letterDataType)
                    null
                } catch (e: IllegalArgumentException) {
                    e.message
                }
            }.filterValues { it != null }

        assertEquals(
            emptyMap<Brevkode.AutoBrev, String>(),
            templatesWithoutSampleData,
            "letterDataType classes must be constructable by Fixtures.create."
        )
    }

    @Test
    fun `all redigerbare templates have letterDataType that can be created`() {
        val templatesWithoutSampleData = templateResource.getRedigerbareBrev()
            .associateWith { templateResource.getRedigerbartBrev(it)!! }
            .mapValues {
                try {
                    Fixtures.create(it.value.letterDataType)
                    null
                } catch (e: IllegalArgumentException) {
                    e.message
                }
            }.filterValues { it != null }

        assertEquals(
            emptyMap<Brevkode.Redigerbar, String>(),
            templatesWithoutSampleData,
            "letterDataType classes must be constructable by Fixtures.create."
        )
    }

    @Test
    fun `all autobrev template letterDataType can be serialized and deserialized`() {
        val jackson = jacksonObjectMapper()
        templateResource.getAutoBrev()
            .map { templateResource.getAutoBrev(it)!! }
            .forEach {
                val data = Fixtures.create(it.letterDataType)
                val json = jackson.writeValueAsString(data)
                val deserialized = jackson.readValue(json, it.letterDataType.java)

                assertEquals(data, deserialized)
            }
    }

    @Test
    fun `all redigerbar template letterDataType can be serialized and deserialized`() {
        val jackson = jacksonObjectMapper()
        templateResource.getRedigerbareBrev()
            .map { templateResource.getRedigerbartBrev(it)!! }
            .forEach {
                val data = Fixtures.create(it.letterDataType)
                val json = jackson.writeValueAsString(data)
                val deserialized = jackson.readValue(json, it.letterDataType.java)

                assertEquals(data, deserialized)
            }
    }

}