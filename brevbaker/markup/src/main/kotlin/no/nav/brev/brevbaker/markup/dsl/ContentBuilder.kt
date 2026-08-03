package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Basis-scope for tekstinnhold i brev-DSL-en.
 */
@MarkupDsl
abstract class AbstractContentBuilder internal constructor() {
    internal val texts: MutableList<Text> = mutableListOf()

    internal fun build(): List<Text> = texts.toList()
}

/**
 * Scope for tekstinnhold i et brev.
 *
 * Tilgjengelige funksjoner:
 * - [text] for vanlig tekst
 * - [newLine] for linjeskift
 */
@MarkupDsl
class ContentBuilder internal constructor() : AbstractContentBuilder() {
    /**
     * Legg til fast tekst i innholdet, valgfritt med [fontType].
     *
     * ```
     * text("Vanlig tekst ")
     * text("uthevet", FontType.BOLD)
     * ```
     */
    fun text(text: String, fontType: FontType = FontType.PLAIN) {
        texts.add(Text.Literal(0, text, fontType))
    }

    /**
     * Legg til et linjeskift i innholdet.
     *
     * Linjeskift bør kun brukes mellom tekst. Linjeskift på starten eller slutten av ett avsnitt fjernes automatisk.
     *
     * ```
     * text("Linje 1")
     * newLine()
     * text("Linje 2")
     * ```
     */
    fun newLine() {
        texts.add(Text.NewLine(0))
    }
}

/** Begrenset tekst-scope for overskrifter med ren tekst uten formattering (fet/kursiv). */
@MarkupDsl
class PlainTextBuilder internal constructor() {
    internal val texts: MutableList<Text> = mutableListOf()

    /**
     * Legg til brødtekst.
     *
     * ```
     * text("Innledning")
     * ```
     */
    fun text(text: String) {
        texts.add(Text.Literal(0, text, FontType.PLAIN))
    }

    internal fun build(): List<Text> = texts.toList()
}

internal typealias ContentFactory<C> = () -> C

internal fun <C : AbstractContentBuilder> ContentFactory<C>.content(build: C.() -> Unit): List<Text> =
    invoke().apply(build).build()

/** Ren tekst fra en enkel [String] (kun [Text.Literal], [FontType.PLAIN], id `0`). */
internal fun plainText(text: String): List<Text> = listOf(Text.Literal(0, text, FontType.PLAIN))

/** Ren tekst fra DSL-blokk (kun [Text.Literal], [FontType.PLAIN], ingen linjeskift, id `0`). */
internal fun plainText(build: PlainTextBuilder.() -> Unit): List<Text> =
    PlainTextBuilder().apply(build).build()
