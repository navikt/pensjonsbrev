package no.nav.pensjon.brev.template

import java.time.LocalDate

/**
 * Classes implementing StableHash guarantee that they provide a hashCode
 * through stableHashCode that only changes when the contained data changes.
 * Typically, many hashCode implementations depends on the memory location
 * of the value/object, and will change across different running instances.
 *
 * The standard hashCode implementation of some types in Java and Kotlin,
 * e.g. String, primitives and data classes, are deterministic and don't depend
 * on changing factors like memory location. Normal (non data) classes, enum classes
 * and objects have by default have a non-deterministic implementation of hashCode
 * that depend on their memory location, unless overridden. This also means that
 * any data class that holds any of these (in any way) will have a hashCode
 * method that returns a non-deterministic value.
 *
 * With StableHash we attempt to provide StableHash-implementations for all
 * value types used in our LetterTemplates, such that we can provide a deterministic
 * hashCode that will not change between running instances of Brevbaker.
 *
 * Classes can easily implement StableHash by using delegation.
 *
 *     class Example(
 *         val anEnum: Color,
 *         val list: List<ThingImplementingStableHash>
 *     ): StableHash by StableHash.of(StableHash.of(anEnum), StableHash.of(list))
 *
 */
interface StableHash {
    /**
     * Calculate a deterministic hashCode that does not depend on volatile factors
     * like memory location.
     */
    fun stableHashCode(): Int

    companion object {
        fun hash(firstField: StableHash?, vararg fields: StableHash?): Int =
            fields.toList().stableHashCode(firstField?.stableHashCode() ?: 0)

        fun of(firstField: StableHash?, vararg fields: StableHash?): StableHash = ClassFieldsStableHash(firstField, *fields)
        fun <T : StableHash?> of(list: List<T>): StableHash = ListStableHash(list)
        fun <K : StableHash?, V : StableHash?> of(map: Map<K, V>): StableHash = MapStableHash(map)
        @JvmName("ofStringMap")
        fun <K : StableHash?> of(map: Map<K, String>): StableHash = StringMapStableHash(map)
        fun of(selector: TemplateModelSelector<*, *>): StableHash = SelectorStableHash(selector)
        fun of(enum: Enum<*>): StableHash = EnumStableHash(enum)
        fun of(number: Number): StableHash = HashCodeStableHash(number)
        fun of(boolean: Boolean): StableHash = HashCodeStableHash(boolean)
        fun of(localDate: LocalDate): StableHash = HashCodeStableHash(localDate)
        fun of(string: String): StableHash = HashCodeStableHash(string)
    }

}

private class HashCodeStableHash(val value: Any) : StableHash {
    override fun stableHashCode() = value.hashCode()
}

private class SelectorStableHash(val selector: TemplateModelSelector<*, *>) : StableHash {
    override fun stableHashCode() = selector.stableHashCode()
}

private class ClassFieldsStableHash(val firstField: StableHash?, vararg val fields: StableHash?) : StableHash {
    override fun stableHashCode() = fields.toList().stableHashCode(firstField?.stableHashCode() ?: 0)
}

private class EnumStableHash(val enum: Enum<*>) : StableHash {
    override fun stableHashCode() = enum.name.hashCode()
}

private class ListStableHash<T : StableHash?>(val list: List<T>) : StableHash {
    override fun stableHashCode() = list.stableHashCode(1)
}

private class MapStableHash<K : StableHash?, V : StableHash?>(val map: Map<K, V>) : StableHash {
    override fun stableHashCode() = map.entries.fold(0) { hash, e -> hash + stableHashCode(e) }
    private fun stableHashCode(entry: Map.Entry<K, V>): Int = (entry.key?.stableHashCode() ?: 0) xor (entry.value?.stableHashCode() ?: 0)
}

private class StringMapStableHash<K : StableHash?>(val map: Map<K, String>) : StableHash {
    override fun stableHashCode() = map.entries.fold(0) { hash, e -> hash + stableHashCode(e) }
    private fun stableHashCode(entry: Map.Entry<K, String>): Int = (entry.key?.stableHashCode() ?: 0) xor entry.value.hashCode()
}

private fun Iterable<StableHash?>.stableHashCode(initial: Int): Int =
    fold(initial) { hash, e -> 31 * hash + (e?.stableHashCode() ?: 0) }