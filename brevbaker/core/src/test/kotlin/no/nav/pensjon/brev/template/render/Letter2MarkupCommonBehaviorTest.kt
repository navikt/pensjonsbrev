package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FellesFactory.felles
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.Letter2MarkupV2
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

/**
 * Behaviour that is expected to be identical between the v1 ([Letter2Markup]) and v2
 * ([Letter2MarkupV2]) rendering paths is asserted once here and run against both paths.
 *
 * Behaviour that differs between the paths (e.g. outline heading-level mapping, or v2 hoisting
 * tables/lists/forms out of paragraphs into sibling blocks) stays in the version-specific
 * [Letter2MarkupTest] and [Letter2MarkupV2Test].
 */
class Letter2MarkupCommonBehaviorTest {

    enum class RenderPath { V1, V2 }

    /** Normalized paragraph text token shared between the two markup shapes. */
    private sealed interface Token {
        data class Literal(val text: String) : Token
        data class Variable(val text: String) : Token
        data object NewLine : Token
    }

    private fun renderParagraphs(
        path: RenderPath,
        body: OutlineOnlyScope<LangBokmal, EmptyAutobrevdata>.() -> Unit,
    ): List<List<Token>> {
        val letter = LetterImpl(outlineTestTemplate(body), EmptyAutobrevdata, Bokmal, felles)
        return when (path) {
            RenderPath.V1 -> Letter2Markup.render(letter).letterMarkup.blocks
                .filterIsInstance<LetterMarkup.Block.Paragraph>()
                .map { paragraph -> paragraph.content.mapNotNull(::toTokenV1) }

            RenderPath.V2 -> Letter2MarkupV2.render(letter).letterMarkup.blocks
                .filterIsInstance<LetterMarkupV2.Block.Paragraph>()
                .map { paragraph -> paragraph.content.map(::toTokenV2) }
        }
    }

    private fun toTokenV1(content: LetterMarkup.ParagraphContent): Token? = when (content) {
        is LetterMarkup.ParagraphContent.Text.Literal -> Token.Literal(content.text)
        is LetterMarkup.ParagraphContent.Text.Variable -> Token.Variable(content.text)
        is LetterMarkup.ParagraphContent.Text.NewLine -> Token.NewLine
        else -> null
    }

    private fun toTokenV2(text: LetterMarkupV2.Text): Token = when (text) {
        is LetterMarkupV2.Text.Literal -> Token.Literal(text.text)
        is LetterMarkupV2.Text.Variable -> Token.Variable(text.text)
        else -> Token.NewLine
    }

    private fun renderBlockIds(
        path: RenderPath,
        body: OutlineOnlyScope<LangBokmal, EmptyAutobrevdata>.() -> Unit,
    ): List<Int> {
        val letter = LetterImpl(outlineTestTemplate(body), EmptyAutobrevdata, Bokmal, felles)
        return when (path) {
            RenderPath.V1 -> Letter2Markup.render(letter).letterMarkup.blocks.map { it.id }
            RenderPath.V2 -> Letter2MarkupV2.render(letter).letterMarkup.blocks.map { it.id }
        }
    }

    private fun renderTitleText(path: RenderPath): String {
        val template = createTemplate(
            letterDataType = EmptyAutobrevdata::class,
            languages = languages(Bokmal),
            letterMetadata = testLetterMetadata,
        ) {
            title { text(bokmal { +"noe tekst " + Year(2024).expr().format() }) }
            outline { paragraph { } }
        }
        val letter = LetterImpl(template, EmptyAutobrevdata, Bokmal, felles)
        return when (path) {
            RenderPath.V1 -> Letter2Markup.render(letter).letterMarkup.title.joinToString("") { it.text }
            RenderPath.V2 -> Letter2MarkupV2.render(letter).letterMarkup.title1.joinToString("") { it.text }
        }
    }

    @ParameterizedTest
    @EnumSource(RenderPath::class)
    fun `template title with expression renders as declared`(path: RenderPath) {
        assertThat(renderTitleText(path)).isEqualTo("noe tekst 2024")
    }

    @ParameterizedTest
    @EnumSource(RenderPath::class)
    fun `paragraph content is rendered in order`(path: RenderPath) {
        val result = renderParagraphs(path) {
            paragraph {
                text(bokmal { +"first" })
                text(bokmal { +"second" })
            }
        }

        assertThat(result).containsExactly(
            listOf(Token.Literal("first"), Token.Literal("second")),
        )
    }

    @ParameterizedTest
    @EnumSource(RenderPath::class)
    fun `newLine renders as declared`(path: RenderPath) {
        val result = renderParagraphs(path) {
            paragraph {
                text(bokmal { +"hei" })
                newline()
                text(bokmal { +"ha det bra" })
            }
        }

        assertThat(result).containsExactly(
            listOf(Token.Literal("hei"), Token.NewLine, Token.Literal("ha det bra")),
        )
    }

    @ParameterizedTest
    @EnumSource(RenderPath::class)
    fun `id for blokker i forEach blir unike for hver iterasjon`(path: RenderPath) {
        val forEachListe = listOf("1", "2", "3")
        val ids = renderBlockIds(path) {
            forEach(forEachListe.expr()) {
                title1 { text(bokmal { +"hei nr. " + it }) }
            }
        }

        assertThat(ids.distinct())
            .describedAs { "innholdet i listene har ikke betydning" }
            .hasSameSizeAs(forEachListe)
    }

    @ParameterizedTest
    @EnumSource(RenderPath::class)
    fun `id for blokker i noestet forEach blir unike for hver iterasjon`(path: RenderPath) {
        val forEachListe = listOf("1", "2", "3")
        val ids = renderBlockIds(path) {
            forEach(forEachListe.expr()) {
                forEach(forEachListe.expr()) {
                    title1 { text(bokmal { +"hei nr. " + it }) }
                }
            }
        }

        assertThat(ids.distinct())
            .describedAs { "innholdet i listene har ikke betydning" }
            .hasSameSizeAs(forEachListe.flatMap { forEachListe })
    }
}
