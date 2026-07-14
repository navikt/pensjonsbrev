package no.nav.brev.brevbaker.markup.dsl

import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/**
 * Basis-scope for tekstinnhold i brev-DSL-en.
 *
 * Denne modulen genererer aldri id-er. I plain-DSL-en ([letterMarkup]) settes alle element-id-er til `0`.
 * Den utvidede DSL-en (api-internal) krever en eksplisitt id på hvert element.
 */
@MarkupDsl
abstract class AbstractContentBuilder internal constructor() {
    internal val texts: MutableList<Text> = mutableListOf()

    internal fun build(): List<Text> = texts.toList()
}

/**
 * Tekstinnhold uten `variable`. Alle id-er settes til `0`.
 *
 * Tilgjengelige funksjoner:
 * - [text] for vanlig tekst
 * - [newLine] for linjeskift
 *
 * En utvidet variant med `variable` og metadata (tags) finnes i api-internal-modulen.
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

/** Begrenset tekst-scope uten `variable` for plain-only overskrifter i [letterMarkup]. Alle id-er settes til `0`. */
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
