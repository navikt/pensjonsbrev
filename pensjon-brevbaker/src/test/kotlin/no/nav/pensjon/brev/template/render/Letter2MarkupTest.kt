package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.outlineTestTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.Year
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Letter2MarkupTest {

    private inline fun <reified LetterData : Any> renderTemplate(data: LetterData, noinline template: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit) =
        Letter2Markup.render(Letter(outlineTestTemplate(template), data, Bokmal, felles)).also {
            jacksonObjectMapper().writeValue(System.out, it)
        }

    @Test
    fun `outline root elements are rendered in same order`() {
        val result = renderTemplate(EmptyBrevdata) {
            title1 { text(Bokmal to "hei tittel") }
            paragraph { text(Bokmal to "hei paragraph") }
            paragraph { text(Bokmal to "hei paragraph2") }
        }

        assertThat(
            result.letterMarkup,
            hasBlocks {
                title1 { literal("hei tittel") }
                paragraph { literal("hei paragraph") }
                paragraph { literal("hei paragraph2") }
            }
        )
    }

    @Test
    fun `paragraph element renders as block of type PARAGRAPH`() {
        val result = renderTemplate(EmptyBrevdata) { paragraph { } }

        assertEquals(Block.Type.PARAGRAPH, result.letterMarkup.blocks.firstOrNull()?.type)
    }

    @Test
    fun `title1 element renders as block of type TITLE1`() {
        val result = renderTemplate(EmptyBrevdata) { title1 { } }

        assertEquals(Block.Type.TITLE1, result.letterMarkup.blocks.firstOrNull()?.type)
    }

    @Test
    fun `paragraph content is rendered in order`() {
        val result = renderTemplate(EmptyBrevdata) {
            paragraph {
                text(Bokmal to "first")
                text(Bokmal to "second")
            }
        }

        assertThat(
            result.letterMarkup,
            hasBlocks {
                paragraph {
                    literal("first")
                    literal("second")
                }
            }
        )
    }

    @Test
    fun `title1 content is rendered in order`() {
        val result = renderTemplate(EmptyBrevdata) {
            title1 {
                text(Bokmal to "first")
                text(Bokmal to "second")
            }
        }

        assertThat(
            result.letterMarkup,
            hasBlocks {
                title1 {
                    literal("first")
                    literal("second")
                }
            }
        )
    }

    @Test
    fun `title1 with expression renders as declared`() {
        val result = renderTemplate(EmptyBrevdata) {
            title1 {
                textExpr(Bokmal to "noe tekst ".expr() + Year(2024).expr().format())
            }
        }

        assertThat(
            result.letterMarkup,
            hasBlocks {
                title1 {
                    literal("noe tekst ")
                    variable("2024")
                }
            }
        )
    }

    @Test
    fun `template title with expression renders as declared`() {
        val template = createTemplate(
            name = "test",
            letterDataType = EmptyBrevdata::class,
            languages = languages(Bokmal),
            letterMetadata = testLetterMetadata,
        ) {
            title {
                textExpr(Bokmal to "noe tekst ".expr() + Year(2024).expr().format())
            }
            outline {
                paragraph { }
            }
        }
        val result = Letter2Markup.render(Letter(template, EmptyBrevdata, Bokmal, felles))

        assertThat(
            result.letterMarkup.title,
            equalTo("noe tekst 2024")
        )
    }

    @Test
    fun `template newLine renders as declared`() {
        val result = renderTemplate(EmptyBrevdata) {
            paragraph {
                text(Bokmal to "hei")
                newline()
                text(Bokmal to "ha det bra")
            }
        }

        assertThat(
            result.letterMarkup,
            hasBlocks {
                paragraph {
                    literal("hei")
                    newLine()
                    literal("ha det bra")
                }
            }
        )
    }

}