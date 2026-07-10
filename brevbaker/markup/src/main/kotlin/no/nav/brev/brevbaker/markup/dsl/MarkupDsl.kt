package no.nav.brev.brevbaker.markup.dsl

@DslMarker
annotation class MarkupDsl

internal class IdGenerator {
    private var next = 1

    fun next(): Int = next++
}
