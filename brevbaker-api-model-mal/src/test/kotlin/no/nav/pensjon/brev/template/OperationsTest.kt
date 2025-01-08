package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*


class OperationsTest {

    @Test
    fun `an operation without fields are considered equal based on class`() {
        assertEquals(UnaryOperation.ToString, UnaryOperation.ToString)
    }

    @Test
    fun `different operations are not considered equal`() {
        assertNotEquals(BinaryOperation.Equal<Int>(), UnaryOperation.ToString)
    }

    @Test
    fun `LocalizedCollectionFormat uses comma and localized separator before last item`() {
        val list = listOf("Alexander", "Jeremy", "Håkon", "Agne")

        assertEquals("Alexander, Jeremy, Håkon og Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Alexander, Jeremy, Håkon og Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Alexander, Jeremy, Håkon and Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.English))
    }

    @Test
    fun `LocalizedCollectionFormat has no separator for only one item`() {
        val list = listOf("Agne")

        assertEquals("Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Agne", LocalizedFormatter.CollectionFormat.apply(list, Language.English))
    }

    @Test
    fun `LocalizedCollectionFormat has no comma for only two items`() {
        val list = listOf("Agne", "Jeremy")

        assertEquals("Agne og Jeremy", LocalizedFormatter.CollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Agne og Jeremy", LocalizedFormatter.CollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Agne and Jeremy", LocalizedFormatter.CollectionFormat.apply(list, Language.English))
    }


    @Nested
    @DisplayName("Absolute value operators")
    inner class AbsoluteValue {
        private val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        @Test
        fun `absoluteKronerValue returns positive value if negative value`() {
            val expr = Kroner(-123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }

        @Test
        fun `absoluteKronerValue returns positive value if positive value`() {
            val expr = Kroner(123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }

        @Test
        fun `absoluteValue returns positive value if negative value`() {
            val expr = (-123).expr().absoluteValue()
            assertEquals(expr.eval(scope), 123)
        }

        @Test
        fun `absoluteValue returns positive value if positive value`() {
            val expr = Kroner(123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }
    }
}