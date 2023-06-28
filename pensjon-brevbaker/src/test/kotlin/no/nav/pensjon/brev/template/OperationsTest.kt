package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.UnaryOperation.SizeOf.format
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Navn
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class OperationsTest {

    @Test
    fun `an operation without fields are considered equal based on class`() {
        assertEquals(UnaryOperation.ToString<Int>(), UnaryOperation.ToString<Int>())
    }

    @Test
    fun `different operations are not considered equal`() {
        assertNotEquals(BinaryOperation.Equal<Int>(), UnaryOperation.ToString<String>())
    }

    @Test
    fun `LocalizedCollectionFormat uses comma and localized separator before last item`() {
        val list = listOf("Alexander", "Jeremy", "Håkon", "Agne")

        assertEquals("Alexander, Jeremy, Håkon og Agne", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Alexander, Jeremy, Håkon og Agne", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Alexander, Jeremy, Håkon and Agne", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.English))
    }

    @Test
    fun `LocalizedCollectionFormat has no separator for only one item`() {
        val list = listOf("Agne")

        assertEquals("Agne", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Agne", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Agne", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.English))
    }

    @Test
    fun `LocalizedCollectionFormat has no comma for only two items`() {
        val list = listOf("Agne", "Jeremy")

        assertEquals("Agne og Jeremy", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.Bokmal))
        assertEquals("Agne og Jeremy", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.Nynorsk))
        assertEquals("Agne and Jeremy", BinaryOperation.LocalizedCollectionFormat.apply(list, Language.English))
    }


    @Nested
    @DisplayName("Absolute value operators")
    inner class AbsoluteValue{
        val scope = ExpressionScope(2, Fixtures.felles, Language.Bokmal)

        @Test
        fun `absoluteKronerValue returns positive value if negative value`(){
            val expr = Kroner(-123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }

        @Test
        fun `absoluteKronerValue returns positive value if positive value`(){
            val expr = Kroner(123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }

        @Test
        fun `absoluteValue returns positive value if negative value`(){
            val expr = (-123).expr().absoluteValue()
            assertEquals(expr.eval(scope), 123)
        }

        @Test
        fun `absoluteValue returns positive value if positive value`(){
            val expr = Kroner(123).expr().absoluteValue()
            assertEquals(expr.eval(scope), Kroner(123))
        }

        @Test
        fun `formatNavn returns givenname lastname if no middle name`() {
            val navn = Navn(fornavn = "Navn", etternavn = "Navnesen").expr()
            assertEquals(navn.format().eval(scope), "Navn Navnesen")
        }

        @Test
        fun `formatNavn returns givenname middle name lastname if middle name`() {
            val navn = Navn(fornavn = "Navn", mellomnavn = "Navnish", etternavn = "Navnesen").expr()
            assertEquals(navn.format().eval(scope), "Navn Navnish Navnesen")
        }
    }
}