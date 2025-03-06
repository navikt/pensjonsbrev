package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CollectionTest {

    private val emptyScope: ExpressionScope<Unit> =
        ExpressionScope(Unit, Fixtures.felles, Language.Bokmal)

    private val selector = object : TemplateModelSelector<Foedselsnummer, String> {
        override val className = "FakeFoedselsnummerSelector"
        override val propertyName = "value"
        override val propertyType = "String"
        override val selector = Foedselsnummer::value
    }

    private data class FoedselsnummerImpl(override val value: String) : Foedselsnummer

    @Test
    fun `isEmpty checks that collection is empty`() {
        assertTrue(emptyList<Int>().expr().isEmpty().eval(emptyScope))
        assertFalse(listOf(1).expr().isEmpty().eval(emptyScope))
    }

    @Test
    fun `map transforms collection items`() {
        val fnrs = listOf(1, 5, 6, 2, 4, 7).map { FoedselsnummerImpl(it.toString()) }

        assertEquals(fnrs.map { it.value }, fnrs.expr().map(UnaryOperationImpl.Select(selector)).eval(emptyScope))
        assertEquals(fnrs.map { it.value }, fnrs.expr().map(selector).eval(emptyScope))
    }

    @Test
    fun `format transforms collection to a string`() {
        val fnrs = listOf(1, 5, 6, 2, 4, 7).map { FoedselsnummerImpl(it.toString()) }

        listOf(Language.Bokmal, Language.Nynorsk, Language.English).forEach {
            val scope = ExpressionScope(Unit, Fixtures.felles, it)
            assertEquals(
                LocalizedFormatter.CollectionFormat.apply(fnrs.map { it.value }, it),
                fnrs.expr().map(selector).format().eval(scope)
            )
        }
    }

    @Test
    fun `size operator returns correct size`() {
        val fnrs = listOf(1, 5, 6, 2, 4, 7)
        val scope = ExpressionScope(Unit, Fixtures.felles, Language.Bokmal)
        assertEquals(fnrs.size, fnrs.expr().size().eval(scope))
        assertEquals(0, emptyList<Int>().expr().size().eval(scope))
    }
}