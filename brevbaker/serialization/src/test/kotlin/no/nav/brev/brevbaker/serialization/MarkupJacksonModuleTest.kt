package no.nav.brev.brevbaker.serialization

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.databind.SerializationFeature
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage
import no.nav.brev.brevbaker.pdfbygger.api.LetterPDFRequest
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class MarkupJacksonModuleTest {

    private val mapper = internalObjectMapper()
    private val prettyMapper = internalObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    @Test
    fun `alle Block-subtyper er registrert i mixin-en`() =
        assertAllSubclassesRegistered(Block::class, "BlockMixin")

    @Test
    fun `alle Text-subtyper er registrert i mixin-en`() =
        assertAllSubclassesRegistered(Text::class, "TextMixin")

    @Test
    fun `LetterPDFRequest roundtripper`() {
        val request = MarkupGoldenFixture.request()
        assertEquals(request, mapper.readValue(mapper.writeValueAsString(request), LetterPDFRequest::class.java))
    }

    @Test
    fun `LetterMarkupWithDataUsage roundtripper`() {
        val letter = MarkupGoldenFixture.withDataUsage()
        assertEquals(
            letter,
            mapper.readValue(mapper.writeValueAsString(letter), LetterMarkupWithDataUsage::class.java),
        )
    }

    @Test
    fun `LetterPDFRequest serialiseres likt som golden-filen`() =
        assertGolden("golden/letterPDFRequest.json", MarkupGoldenFixture.request())

    @Test
    fun `LetterMarkupWithDataUsage serialiseres likt som golden-filen`() =
        assertGolden("golden/letterMarkupWithDataUsage.json", MarkupGoldenFixture.withDataUsage())

    private fun assertGolden(resource: String, value: Any) {
        val actual = prettyMapper.writeValueAsString(value) + "\n"
        if (System.getenv("REGENERER_GOLDEN")?.toBoolean() == true) {
            java.io.File("src/test/resources/$resource").writeText(actual)
        }
        val expected = readResource(resource)
        assertEquals(
            expected,
            actual,
            "Wire-formatet mot pdf-bygger/skribenten er endret. Er endringen tilsiktet, oppdater $resource.",
        )
    }

    private fun readResource(name: String): String =
        checkNotNull(javaClass.classLoader.getResourceAsStream(name)) { "Fant ikke $name" }
            .use { it.readBytes().decodeToString() }

    /**
     * Vokter mot drift: legges det til en ny sealed subtype i markup uten at den registreres i
     * [MarkupJacksonModule], feiler denne testen i stedet for at deserialiseringen ryker i produksjon.
     */
    private fun assertAllSubclassesRegistered(base: KClass<*>, mixinSimpleName: String) {
        val mixin = MarkupJacksonModule::class.java.declaredClasses.single { it.simpleName == mixinSimpleName }
        val registered = mixin.getAnnotation(JsonSubTypes::class.java).value.map { it.value }.toSet()
        val expected = base.sealedSubclasses.filter { !it.isAbstract }.toSet()
        assertEquals(emptySet<KClass<*>>(), expected - registered, "Ikke registrert i $mixinSimpleName")
        assertEquals(emptySet<KClass<*>>(), registered - expected, "Registrert, men ikke lenger en subtype")
        assert(registered.all { it.isSubclassOf(base) })
    }
}
