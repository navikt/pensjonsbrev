package no.nav.pensjon.brev.template

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

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
}