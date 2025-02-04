package no.nav.pensjon.brev.template

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class StableHashTest {

    private data class StableImpl(val value: Int): StableHash {
        override fun stableHashCode() = hashCode()
    }

    @Test
    fun `list stableHash behaves as hashCode`() {
        val list = (0..1000).map { StableImpl(it) } + null
        assertEquals(list.hashCode(), StableHash.of(list).stableHashCode())
    }

    private data class ADataClass(val x: StableImpl?, val y: StableImpl, val z: StableImpl?)

    @Test
    fun `stableHasCode of fields behaves as data class hashCode`() {
        listOf(
            ADataClass(StableImpl(99), StableImpl(231), StableImpl("a string".hashCode())),
            ADataClass(null, StableImpl(31), StableImpl("another string".hashCode())),
            ADataClass(StableImpl("crazy horse".hashCode()), StableImpl(31), null),
        ).forEach {
            assertEquals(it.hashCode(), StableHash.hash(it.x, it.y, it.z))
        }
    }

    @Test
    fun `stableHashCode of map behaves as hashCode`() {
        val map = (0..1000).associate { StableImpl(it) to StableImpl(1000 - it) }
        assertEquals(map.hashCode(), StableHash.of(map).stableHashCode())
    }

    @Test
    fun `stableHashCode of string map behaves as hashCode`() {
        val map = (0..1000).associate { StableImpl(it) to "hello ${1000 - it}" }
        assertEquals(map.hashCode(), StableHash.of(map).stableHashCode())
    }

    @Test
    fun `stableHashCode of primitives and string delegates to hashCode`() {
        assertEquals(1.hashCode(), StableHash.of(1).stableHashCode())
        assertEquals(1L.hashCode(), StableHash.of(1L).stableHashCode())
        assertEquals(1.0.hashCode(), StableHash.of(1.0).stableHashCode())
        assertEquals(true.hashCode(), StableHash.of(true).stableHashCode())
        assertEquals(true.hashCode(), StableHash.of(true).stableHashCode())
    }

    private enum class MyEnum { ONE, TWO }

    @Test
    fun `stableHashCode of enum uses Enum name`() {
        assertEquals(MyEnum.ONE.name.hashCode(), StableHash.of(MyEnum.ONE).stableHashCode())
        assertEquals(MyEnum.TWO.name.hashCode(), StableHash.of(MyEnum.TWO).stableHashCode())
        assertNotEquals(MyEnum.ONE.name.hashCode(), StableHash.of(MyEnum.TWO).stableHashCode())
    }

}