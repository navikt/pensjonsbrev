package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FellesFactory.felles
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.brev.brevbaker.template.render.Letter2MarkupV2
import no.nav.brev.brevbaker.template.render.UnsupportedInLetterMarkupV2
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.LetterMarkupV2Asserter.Companion.assertThat
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2.Block
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Letter2MarkupV2Test {

    private inline fun <reified LetterData : AutobrevData> renderTemplate(data: LetterData, noinline template: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit) =
        Letter2MarkupV2.render(LetterImpl(outlineTestTemplate(template), data, Bokmal, felles))

    @Test
    fun `template title is rendered as title1`() {
        val template = createTemplate(
            letterDataType = EmptyAutobrevdata::class,
            languages = languages(Bokmal),
            letterMetadata = testLetterMetadata,
        ) {
            title {
                text(bokmal { +"noe tekst " + Year(2024).expr().format() })
            }
            outline {
                paragraph { }
            }
        }
        val result = Letter2MarkupV2.render(LetterImpl(template, EmptyAutobrevdata, Bokmal, felles))

        assertThat(result.title1.joinToString("") { it.text }).isEqualTo("noe tekst 2024")
    }

    @Test
    fun `outline title1 becomes block Title2`() {
        val result = renderTemplate(EmptyAutobrevdata) { title1 { text(bokmal { +"heading" }) } }

        assertEquals(Block.Type.TITLE2, result.blocks.firstOrNull()?.type)
        assertThat(result).hasBlocks { title2 { literal("heading") } }
    }

    @Test
    fun `outline title2 becomes block Title3`() {
        val result = renderTemplate(EmptyAutobrevdata) { title2 { text(bokmal { +"heading" }) } }

        assertEquals(Block.Type.TITLE3, result.blocks.firstOrNull()?.type)
        assertThat(result).hasBlocks { title3 { literal("heading") } }
    }

    @Test
    fun `outline title3 becomes block Title4`() {
        val result = renderTemplate(EmptyAutobrevdata) { title3 { text(bokmal { +"heading" }) } }

        assertEquals(Block.Type.TITLE4, result.blocks.firstOrNull()?.type)
        assertThat(result).hasBlocks { title4 { literal("heading") } }
    }

    @Test
    fun `outline root elements are rendered in same order`() {
        val result = renderTemplate(EmptyAutobrevdata) {
            title1 { text(bokmal { +"hei tittel" }) }
            paragraph { text(bokmal { +"hei paragraph" }) }
            paragraph { text(bokmal { +"hei paragraph2" }) }
        }

        assertThat(result).hasBlocks {
            title2 { literal("hei tittel") }
            paragraph { literal("hei paragraph") }
            paragraph { literal("hei paragraph2") }
        }
    }

    @Test
    fun `paragraph content is rendered in order`() {
        val result = renderTemplate(EmptyAutobrevdata) {
            paragraph {
                text(bokmal { +"first" })
                text(bokmal { +"second" })
            }
        }

        assertThat(result).hasBlocks {
            paragraph {
                literal("first")
                literal("second")
            }
        }
    }

    @Test
    fun `newLine renders as declared`() {
        val result = renderTemplate(EmptyAutobrevdata) {
            paragraph {
                text(bokmal { +"hei" })
                newline()
                text(bokmal { +"ha det bra" })
            }
        }

        assertThat(result).hasBlocks {
            paragraph {
                literal("hei")
                newLine()
                literal("ha det bra")
            }
        }
    }

    @Test
    fun `list in the middle of a paragraph splits it into three blocks`() {
        val result = renderTemplate(EmptyAutobrevdata) {
            paragraph {
                text(bokmal { +"before" })
                list {
                    item { text(bokmal { +"item 1" }) }
                    item { text(bokmal { +"item 2" }) }
                }
                text(bokmal { +"after" })
            }
        }

        assertThat(result).hasBlocks {
            paragraph { literal("before") }
            list {
                item { literal("item 1") }
                item { literal("item 2") }
            }
            paragraph { literal("after") }
        }
    }

    @Test
    fun `numbered list is hoisted out to its own block`() {
        val result = renderTemplate(EmptyAutobrevdata) {
            paragraph {
                text(bokmal { +"before" })
                numberedList {
                    item { text(bokmal { +"item 1" }) }
                }
            }
        }

        assertThat(result).hasBlocks {
            paragraph { literal("before") }
            numberedList {
                item { literal("item 1") }
            }
        }
    }

    @Test
    fun `table at the start of a paragraph does not produce a leading empty paragraph`() {
        val result = renderTemplate(EmptyAutobrevdata) {
            paragraph {
                table(header = {
                    column { text(bokmal { +"col1" }) }
                }) {
                    row { cell { text(bokmal { +"a" }) } }
                }
                text(bokmal { +"after" })
            }
        }

        assertThat(result).hasBlocks {
            table {
                header { column { literal("col1") } }
                row { cell { literal("a") } }
            }
            paragraph { literal("after") }
        }
    }

    @Test
    fun `consecutive lists in the same paragraph become separate sibling blocks`() {
        val result = renderTemplate(EmptyAutobrevdata) {
            paragraph {
                list {
                    item { text(bokmal { +"a" }) }
                }
                list {
                    item { text(bokmal { +"b" }) }
                }
            }
        }

        assertThat(result).hasBlocks {
            list { item { literal("a") } }
            list { item { literal("b") } }
        }
    }

    @Test
    fun `form content throws UnsupportedInLetterMarkupV2`() {
        assertThatThrownBy {
            renderTemplate(EmptyAutobrevdata) {
                paragraph {
                    formText(size = Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT, prompt = { text(bokmal { +"prompt" }) })
                }
            }
        }.isInstanceOf(UnsupportedInLetterMarkupV2::class.java)
    }

    @Test
    fun `id for blokker i forEach blir unike for hver iterasjon`() {
        val forEachListe = listOf("1", "2", "3")
        val result = renderTemplate(EmptyAutobrevdata) {
            forEach(forEachListe.expr()) {
                title1 { text(bokmal { +"hei nr. " + it }) }
            }
        }
        assertThat(result.blocks.map { it.id }.distinct())
            .describedAs { "innholdet i listene har ikke betydning" }
            .hasSameSizeAs(forEachListe)
    }
}
