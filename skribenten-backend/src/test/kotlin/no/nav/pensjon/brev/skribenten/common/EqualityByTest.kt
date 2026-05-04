package no.nav.pensjon.brev.skribenten.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EqualityByTest {

    class Clazz(@Suppress("unused") val exclude: String, val a: String?, val b: Int, val c: Long?, val d: Boolean): EqualityBy<Clazz>(Clazz::a, Clazz::b, Clazz::c, Clazz::d)

    @Test
    fun `equals only use declared equality properties`() {
        assertEquals(Clazz("obj1", "a", 1, 2L, true), Clazz("obj2", "a", 1, 2L, true))
        assertEquals(Clazz("obj1", "a", 1, 2L, true), Clazz("obj1", "a", 1, 2L, true))
        assertEquals(Clazz("obj1", null, 1, null, true), Clazz("obj2", null, 1, null, true))
        assertEquals(Clazz("obj1", "a", 1, null, true), Clazz("obj2", "a", 1, null, true))
        assertEquals(Clazz("obj1", "a", 1, null, false), Clazz("obj2", "a", 1, null, false))

        assertNotEquals(Clazz("obj1", "a", 1, 2L, true), Clazz("obj2", "b", 1, 2L, true))
        assertNotEquals(Clazz("obj1", "a", 1, 2L, true), Clazz("obj2", "a", 2, 2L, true))
        assertNotEquals(Clazz("obj1", null, 1, 2L, true), Clazz("obj2", null, 1, 3L, true))
        assertNotEquals(Clazz("obj1", "a", 1, null, true), Clazz("obj2", "a", 1, null, false))
        assertNotEquals(Clazz("obj1", "a", 1, 2L, false), Clazz("obj2", "a", 1, null, false))
    }

    @Test
    fun `hashcode only use declared equality properties`() {
        assertEquals(Clazz("obj1", "a", 1, 2L, true).hashCode(), Clazz("obj2", "a", 1, 2L, true).hashCode())
        assertEquals(Clazz("obj1", "a", 1, 2L, true).hashCode(), Clazz("obj1", "a", 1, 2L, true).hashCode())
        assertEquals(Clazz("obj1", null, 1, null, true).hashCode(), Clazz("obj2", null, 1, null, true).hashCode())
        assertEquals(Clazz("obj1", "a", 1, null, true).hashCode(), Clazz("obj2", "a", 1, null, true).hashCode())
        assertEquals(Clazz("obj1", "a", 1, null, false).hashCode(), Clazz("obj2", "a", 1, null, false).hashCode())

        assertNotEquals(Clazz("obj1", "a", 1, 2L, true).hashCode(), Clazz("obj2", "b", 1, 2L, true).hashCode())
        assertNotEquals(Clazz("obj1", "a", 1, 2L, true).hashCode(), Clazz("obj2", "a", 2, 2L, true).hashCode())
        assertNotEquals(Clazz("obj1", null, 1, 2L, true).hashCode(), Clazz("obj2", null, 1, 3L, true).hashCode())
        assertNotEquals(Clazz("obj1", "a", 1, null, true).hashCode(), Clazz("obj2", "a", 1, null, false).hashCode())
        assertNotEquals(Clazz("obj1", "a", 1, 2L, false).hashCode(), Clazz("obj2", "a", 1, null, false).hashCode())
    }

    interface NoPropEquality
    class NoPropEqualityA(val a: String) : NoPropEquality, EqualityBy<NoPropEqualityA>()
    class NoPropEqualityB(val a: String) : NoPropEquality, EqualityBy<NoPropEqualityB>()

    @Test
    fun `equality uses javaclass for empty property list`() {
        assertEquals(NoPropEqualityA("a"), NoPropEqualityA("b"))
        val a: NoPropEquality = NoPropEqualityA("a")
        val b: NoPropEquality = NoPropEqualityB("a")
        assertNotEquals(a, b)
    }

    @Test
    fun `hashCode uses javaclass for empty property list`() {
        assertEquals(NoPropEqualityA("a").hashCode(), NoPropEqualityA("b").hashCode())
        assertNotEquals(NoPropEqualityA("a").hashCode(), NoPropEqualityB("a").hashCode())
    }

    private class UnrelatedA(val x: String) : EqualityBy<UnrelatedA>()
    private class UnrelatedB(val x: String) : EqualityBy<UnrelatedA>(UnrelatedA::x)

    @Test
    fun `init guard prevents using properties from unrelated class`() {
        assertThrows<IllegalArgumentException> {
            UnrelatedB("x")
        }
    }

}
