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
}