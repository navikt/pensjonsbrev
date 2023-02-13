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

    private val templateResource = TemplateResource(prodVedtaksbrevTemplates)

    @Test
    fun `getVedtaksbrev fetches template`() {
        assertEquals(OmsorgEgenAuto.template, templateResource.getVedtaksbrev(OmsorgEgenAuto.kode))
    }

    @Test
    fun `getRedigerbartBrev fetches template`() {
        assertEquals(InformasjonOmSaksbehandlingstid.template, templateResource.getRedigerbartBrev(InformasjonOmSaksbehandlingstid.kode))
    }

    @Test
    fun `getVedtaksbrev returns list of template names`() {
        assertThat(
            templateResource.getVedtaksbrev(),
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
    fun `all names returned by getVedtaksbrev can be fetched with getVedtaksbrev`() {
        val templateNames = templateResource.getVedtaksbrev().toSet()
        val templates = templateNames.mapNotNull { templateResource.getVedtaksbrev(it) }
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
    fun `all vedtaksbrev templates have letterDataType which are data class`() {
        val templatesWithoutDataClass: Map<Brevkode.AutoBrev, LetterTemplate<*, *>> = templateResource.getVedtaksbrev()
            .associateWith { templateResource.getVedtaksbrev(it)!! }
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
    fun `all vedtaksbrev templates have letterDataType that can be created`() {
        val templatesWithoutSampleData = templateResource.getVedtaksbrev()
            .associateWith { templateResource.getVedtaksbrev(it)!! }
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
    fun `all vedtaksbrev template letterDataType can be serialized and deserialized`() {
        val jackson = jacksonObjectMapper()
        templateResource.getVedtaksbrev()
            .map { templateResource.getVedtaksbrev(it)!! }
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