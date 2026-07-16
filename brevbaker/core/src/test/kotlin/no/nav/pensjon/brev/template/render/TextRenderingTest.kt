package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FellesFactory.felles
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.template.render.Letter2MarkupV2
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Fokuserte tester for tekst-komponenten som [Letter2MarkupV2] delegerer til
 * (`textFragmentsOf` / `appendText`): sammenslåing av literaler, taggede biter, variabler,
 * linjeskift og font-mapping. Behaviour verifiseres via renderen siden komponenten er intern.
 */
class TextRenderingTest {

    private fun firstParagraphTexts(body: OutlineOnlyScope<LangBokmal, EmptyAutobrevdata>.() -> Unit): List<Text> {
        val letter = LetterImpl(outlineTestTemplate(body), EmptyAutobrevdata, Bokmal, felles)
        return Letter2MarkupV2.render(letter).letterMarkup.blocks
            .filterIsInstance<Block.Paragraph>()
            .first()
            .content
    }

    @Test
    fun `adjacent literals within one expression are merged into a single literal`() {
        val texts = firstParagraphTexts {
            paragraph { text(bokmal { +("a".expr() + "b".expr()) }) }
        }

        assertThat(texts).singleElement().isInstanceOfSatisfying(Text.Literal::class.java) {
            assertThat(it.text).isEqualTo("ab")
            assertThat(it.tags).isEmpty()
        }
    }

    @Test
    fun `a variable between two literals prevents merging`() {
        val texts = firstParagraphTexts {
            paragraph { text(bokmal { +("a".expr() + Year(2024).expr().format() + "b".expr()) }) }
        }

        assertThat(texts.map { it.type }).containsExactly(Text.Type.LITERAL, Text.Type.VARIABLE, Text.Type.LITERAL)
        assertThat(texts.map { it.text }).containsExactly("a", "2024", "b")
    }

    @Test
    fun `literals from separate text elements are not merged across the element boundary`() {
        val texts = firstParagraphTexts {
            paragraph {
                text(bokmal { +"a" })
                text(bokmal { +"b" })
            }
        }

        assertThat(texts.filterIsInstance<Text.Literal>().map { it.text }).containsExactly("a", "b")
    }

    @Test
    fun `newLine breaks a paragraph into literal, newline, literal`() {
        val texts = firstParagraphTexts {
            paragraph {
                text(bokmal { +"a" })
                newline()
                text(bokmal { +"b" })
            }
        }

        assertThat(texts.map { it.type }).containsExactly(Text.Type.LITERAL, Text.Type.NEW_LINE, Text.Type.LITERAL)
    }

    @Test
    fun `font type is mapped through to the markup literal`() {
        val texts = firstParagraphTexts {
            paragraph { text(bokmal { +"bold" }, fontType = FontType.BOLD) }
        }

        assertThat(texts).singleElement().isInstanceOfSatisfying(Text.Literal::class.java) {
            assertThat(it.fontType).isEqualTo(Text.FontType.BOLD)
        }
    }
}
