package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.MarkupInternalApi
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Basis-scope for tekstinnhold i brev-DSL-en.
 */
@MarkupDsl
abstract class AbstractContentBuilder @MarkupInternalApi constructor() {
    internal val _texts: MutableList<Text> = mutableListOf()

    internal fun _build(): List<Text> = _texts.toList()
}

/**
 * Scope for tekstinnhold i et brev.
 *
 * Tilgjengelige funksjoner:
 * - [text] for vanlig tekst
 * - [newLine] for linjeskift
 */
@MarkupDsl
class ContentBuilder @MarkupInternalApi constructor() : AbstractContentBuilder() {
    /**
     * Legg til fast tekst i innholdet, valgfritt med [fontType].
     *
     * ```
     * text("Vanlig tekst ")
     * text("uthevet", FontType.BOLD)
     * ```
     */
    fun text(text: String, fontType: FontType = FontType.PLAIN) {
        _texts.add(Text.Literal(0, text, fontType))
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
        _texts.add(Text.NewLine(0))
    }
}

/** Begrenset tekst-scope for overskrifter med ren tekst uten formattering (fet/kursiv). */
@MarkupDsl
class PlainTextBuilder @MarkupInternalApi constructor() {
    internal val _texts: MutableList<Text> = mutableListOf()

    /**
     * Legg til brødtekst.
     *
     * ```
     * text("Innledning")
     * ```
     */
    fun text(text: String) {
        _texts.add(Text.Literal(0, text, FontType.PLAIN))
    }

    internal fun _build(): List<Text> = _texts.toList()
}

typealias ContentFactory<C> = () -> C

@MarkupInternalApi
fun <C : AbstractContentBuilder> ContentFactory<C>.content(build: C.() -> Unit): List<Text> =
    invoke().apply(build)._build()

/** Ren tekst fra en enkel [String] (kun [Text.Literal], [FontType.PLAIN], id `0`). */
internal fun plainText(text: String): List<Text> = listOf(Text.Literal(0, text, FontType.PLAIN))

/** Ren tekst fra DSL-blokk (kun [Text.Literal], [FontType.PLAIN], ingen linjeskift, id `0`). */
internal fun plainText(build: PlainTextBuilder.() -> Unit): List<Text> =
    PlainTextBuilder().apply(build)._build()
