package no.nav.pensjon.brev.template.dsl.expression

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerSelectors.valueSelector
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CollectionTest {

    private val emptyScope: ExpressionScope<Unit> =
        ExpressionScope(Unit, Fixtures.felles, Language.Bokmal)

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
    fun `containsExclusively_required returns true when input has items`() {
        assertTrue(listOf(1, 2).expr().containsExclusively { required(1, 2) }.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_required returns false when input has more than expected items`() {
        assertFalse(listOf(1, 2, 3).expr().containsExclusively { required(1, 2) }.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_required ignores order`() {
        assertTrue(listOf(3, 2, 1).expr().containsExclusively { required(1, 2, 3) }.eval(emptyScope))
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
        assertTrue(listOf(1, 2, 3).expr().containsExclusively { anyOf(3, 2, 1) }.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf returns false when input has element not listed`() {
        assertFalse(listOf(1, 2, 3, 4).expr().containsExclusively { anyOf(3, 2, 1) }.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf returns true when input doesnt have all listed items`() {
        assertTrue(listOf(1, 2).expr().containsExclusively { anyOf(3, 2, 1) }.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf returns false when input is empty`() {
        assertFalse(emptyList<Int>().expr().containsExclusively { anyOf(3, 2, 1) }.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf_and_required returns true when input has all required and all anyOf`() {
        val containsExclusively = listOf(1, 2, 3, 4).expr().containsExclusively {
            required(1, 2)
            anyOf(3, 4)
        }
        assertTrue(containsExclusively.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf_and_required returns false when input has all required and all anyOf but also additional items`() {
        val containsExclusively = listOf(1, 2, 3, 4, 5).expr().containsExclusively {
            required(1, 2)
            anyOf(3, 4)
        }
        assertFalse(containsExclusively.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf_and_required returns true when input has all required and some anyOf`() {
        val containsExclusively = listOf(1, 2, 3).expr().containsExclusively {
            required(1, 2)
            anyOf(3, 4)
        }
        assertTrue(containsExclusively.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf_and_required returns false when input has all required and none anyOf`() {
        val containsExclusively = listOf(1, 2).expr().containsExclusively {
            required(1, 2)
            anyOf(3, 4)
        }
        assertFalse(containsExclusively.eval(emptyScope))
    }

    @Test
    fun `containsExclusively_anyOf_and_required returns false when input has some required and some anyOf`() {
        val containsExclusively = listOf(1, 2, 4).expr().containsExclusively {
            required(1, 2, 3)
            anyOf(4, 5)
        }
        assertFalse(containsExclusively.eval(emptyScope))
    }

    @Test
    fun `map transforms collection items`() {
        val fnrs = listOf(1, 5, 6, 2, 4, 7).map { Foedselsnummer(it.toString()) }

        assertEquals(fnrs.map { it.value }, fnrs.expr().map(UnaryOperation.Select(valueSelector)).eval(emptyScope))
        assertEquals(fnrs.map { it.value }, fnrs.expr().map(valueSelector).eval(emptyScope))
    }

    @Test
    fun `format transforms collection to a string`() {
        val fnrs = listOf(1, 5, 6, 2, 4, 7).map { Foedselsnummer(it.toString()) }

        listOf(Language.Bokmal, Language.Nynorsk, Language.English).forEach {
            val scope = ExpressionScope(Unit, Fixtures.felles, it)
            assertEquals(
                LocalizedFormatter.CollectionFormat.apply(fnrs.map { it.value }, it),
                fnrs.expr().map(valueSelector).format().eval(scope)
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