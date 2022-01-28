package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class CollectionTest {

    val emptyScope: ExpressionScope<Unit, Language.Bokmal> = ExpressionScope(Unit, Fixtures.felles, Language.Bokmal)

    @Test
    fun `containsAll returns true when input has all items`() {
        assertTrue((1..5).toList().expr().containsAll(2, 3).eval(emptyScope))
    }

    @Test
    fun `containsAll returns false when input doesnt have all items`() {
        assertFalse((1..5).toList().expr().containsAll(2, 3, 10).eval(emptyScope))
    }

    @Test
    fun `containsAll ignores order`() {
        assertTrue(listOf(3, 2, 1).expr().containsAll(1, 2).eval(emptyScope))
    }

    @Test
    fun `isEmpty checks that collection is empty`() {
        assertTrue(emptyList<Int>().expr().isEmpty().eval(emptyScope))
        assertFalse(listOf(1).expr().isEmpty().eval(emptyScope))
    }

    @Test
    fun `containsOnly returns true when input has items`() {
        assertTrue(listOf(1, 2).expr().containsOnly(1, 2).eval(emptyScope))
    }

    @Test
    fun `containsOnly returns false when input has more than expected items`() {
        assertFalse(listOf(1, 2, 3).expr().containsOnly(1, 2).eval(emptyScope))
    }

    @Test
    fun `containsOnly ignores order`() {
        assertTrue(listOf(3, 2, 1).expr().containsOnly(1, 2, 3).eval(emptyScope))
    }

    @Test
    fun `containsAny returns true when input contains at least one of the listed items`() {
        assertTrue(listOf(1, 2, 3).expr().containsAny(1, 5).eval(emptyScope))
    }

    @Test
    fun `containsAny returns false when input doesnt contain any of the listed items`() {
        assertFalse(listOf(1, 2, 3).expr().containsAny(4, 5).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsAny returns true when input has the listed items`() {
        assertTrue(listOf(1, 2, 3).expr().exclusivelyContainsAny(3, 2, 1).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsAny returns false when input has element not listed`() {
        assertFalse(listOf(1, 2, 3, 4).expr().exclusivelyContainsAny(3, 2, 1).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsAny returns true when input doesnt have all listed items`() {
        assertTrue(listOf(1, 2).expr().exclusivelyContainsAny(3, 2, 1).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsAny returns false when input is empty`() {
        assertFalse(emptyList<Int>().expr().exclusivelyContainsAny(3, 2, 1).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsRequiredAndAnyOf returns true when input has all required and all anyOf`() {
        assertTrue(listOf(1, 2, 3, 4).expr().exclusivelyContainsRequiredAndAnyOf(setOf(1, 2), setOf(3, 4)).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsRequiredAndAnyOf returns false when input has all required and all anyOf but also additional items`() {
        assertFalse(listOf(1, 2, 3, 4, 5).expr().exclusivelyContainsRequiredAndAnyOf(setOf(1, 2), setOf(3, 4)).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsRequiredAndAnyOf returns true when input has all required and some anyOf`() {
        assertTrue(listOf(1, 2, 3).expr().exclusivelyContainsRequiredAndAnyOf(setOf(1, 2), setOf(3, 4)).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsRequiredAndAnyOf returns false when input has all required and none anyOf`() {
        assertFalse(listOf(1, 2).expr().exclusivelyContainsRequiredAndAnyOf(setOf(1, 2), setOf(3, 4)).eval(emptyScope))
    }

    @Test
    fun `exclusivelyContainsRequiredAndAnyOf returns false when input has some required and some anyOf`() {
        assertFalse(listOf(1, 2, 4).expr().exclusivelyContainsRequiredAndAnyOf(setOf(1, 2, 3), setOf(4, 5)).eval(emptyScope))
    }

}