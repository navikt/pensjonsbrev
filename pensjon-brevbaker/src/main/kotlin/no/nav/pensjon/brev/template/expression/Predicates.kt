package no.nav.pensjon.brev.template.expression

interface Predicate<T> {
    fun validate(input: T): Boolean
}

object Collections {
    data class ContainsExclusively<T>(val required: Set<T>, val anyOfs: List<Set<T>>): Predicate<Collection<T>> {

        override fun validate(input: Collection<T>): Boolean =
            (required union anyOfs.flatten()).containsAll(input)
                    && input.containsAll(required)
                    && anyOfs.all { anyOf -> anyOf.any { input.contains(it) } }

        class Builder<T>(private val requiredItems: MutableSet<T> = mutableSetOf(), private val anyOfItems: MutableList<Set<T>> = mutableListOf()) {
            fun required(vararg items: T) {
                requiredItems.addAll(items)
            }

            fun anyOf(vararg items: T) {
                anyOfItems.add(items.toSet())
            }

            fun build(): ContainsExclusively<T> = ContainsExclusively(requiredItems, anyOfItems)
        }
    }

    data class ContainsAny<T>(val anyOf: Set<T>): Predicate<Collection<T>> {
        override fun validate(input: Collection<T>): Boolean =
            anyOf.any { input.contains(it) }
    }

    object IsEmpty: Predicate<Collection<*>> {
        override fun validate(input: Collection<*>): Boolean = input.isEmpty()
    }

    data class ContainsAll<T>(val anyOf: Set<T>): Predicate<Collection<T>> {
        override fun validate(input: Collection<T>): Boolean = input.containsAll(anyOf)
    }
}
