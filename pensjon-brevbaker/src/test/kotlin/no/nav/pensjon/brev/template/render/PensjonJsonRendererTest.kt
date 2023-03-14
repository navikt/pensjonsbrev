package no.nav.pensjon.brev.template.render

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.api.model.RenderedJsonLetter
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.Block
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class PensjonJsonRendererTest {

    private inline fun <reified LetterData : Any> renderTemplate(data: LetterData, noinline template: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit) =
        PensjonJsonRenderer.render(Letter(outlineTestTemplate(template), data, Bokmal, felles)).also {
            jacksonObjectMapper().writeValue(System.out, it.blocks)
        }

    @Disabled
    @Test
    fun `outline root elements are rendered in same order`() {
        val result = renderTemplate(Unit) {
            title1 { text(Bokmal to "hei tittel") }
            paragraph { text(Bokmal to "hei paragraph") }
            paragraph { text(Bokmal to "hei paragraph2") }
        }

        assertThat(result.blocks[0], isA<Block.Title1>())
        assertThat(result.blocks[1], isA<Block.Paragraph>())
        assertThat(result.blocks[2], isA<Block.Paragraph>())
        val p2 = result.blocks[2]
        if (p2 is Block.Paragraph) {
            val text = p2.content.first()
            assertThat(text, isA<RenderedJsonLetter.ParagraphContent.Text.Literal>())
            if (text is RenderedJsonLetter.ParagraphContent.Text.Literal) {
                assertEquals("hei paragraph2", text.text)
            }
        }

        assertEquals(listOf("hei tittel", "hei paragraph", "hei paragraph2"), result.blocks.textInOrder())
    }

    @Test
    fun `paragraph element renders as block of type PARAGRAPH`() {
        val result = renderTemplate(Unit) { paragraph { } }

        assertEquals(Block.Type.PARAGRAPH, result.blocks.firstOrNull()?.type)
    }

    @Test
    fun `title1 element renders as block of type TITLE1`() {
        val result = renderTemplate(Unit) { title1 { } }

        assertEquals(Block.Type.TITLE1, result.blocks.firstOrNull()?.type)
    }

    @Test
    fun `paragraph content is rendered in order`() {
        val result = renderTemplate(Unit) {
            paragraph {
                text(Bokmal to "first")
                text(Bokmal to "second")
            }
        }

        assertEquals(listOf("first", "second"), result.blocks.first().textInOrder())
    }

    @Test
    fun `title1 content is rendered in order`() {
        val result = renderTemplate(Unit) {
            title1 {
                text(Bokmal to "first")
                text(Bokmal to "second")
            }
        }

        assertEquals(listOf("first", "second"), result.blocks.first().textInOrder())
    }

    @Test
    fun `repeated equal elements are distinguishable by location`() {
        val (p1, p2) = renderTemplate(Unit) {
            paragraph { text(Bokmal to "hei") }
            paragraph { text(Bokmal to "hei") }
        }.blocks

        assertEquals(p1.id, p2.id)
        assertNotEquals(p1.location, p2.location)
    }

    //TODO: Følgende tester er deaktivert fordi jeg ikke vet om location er nødvendig enda, ei hvordan det bør fungere.
    @Disabled
    @Test
    fun `control structures adds step to location but still counts blocks in outer scope`() {
        val result = renderTemplate(Unit) {
            paragraph {
                text(Bokmal to "before")
                forEach(listOf("hei", "joda").expr()) {
                    text(Bokmal to "abc")
                }
                text(Bokmal to "after")
            }
        }

        assertEquals(listOf("0"), result.blocks[0].location)
        assertEquals(listOf("c", "1"), result.blocks[1].location)
        assertEquals(listOf("c", "2"), result.blocks[2].location)
        assertEquals(listOf("3"), result.blocks[3].location)
    }

    @Disabled
    @Test
    fun `forEach rendering adds step to location`() {
        val result = renderTemplate(Unit) {
            paragraph {
                forEach(listOf("hei", "joda").expr()) {
                    text(Bokmal to "abc")
                }
            }
        }

        assertEquals(2, result.blocks.size)
        assertEquals(listOf("c", "0"), result.blocks[0].location)
        assertEquals(listOf("c", "1"), result.blocks[1].location)
    }

    @Disabled
    @Test
    fun `showIf rendering adds step to location`() {
        val result = renderTemplate(Unit) {
            showIf(true.expr()) {
                paragraph {
                    text(Bokmal to "abc")
                }
            }
        }

        assertEquals(1, result.blocks.size)
        assertEquals(listOf("c", "0"), result.blocks[0].location)
    }

    private fun List<Block>.textInOrder(): List<String> =
        flatMap { it.textInOrder() }

    private fun Block.textInOrder(): List<String> =
        when(this) {
            is Block.Paragraph -> content.flatMap { it.textInOrder() }
            is Block.Title1 -> content.map { it.text }
        }

    private fun RenderedJsonLetter.ParagraphContent.textInOrder(): List<String> =
        when(this) {
            is RenderedJsonLetter.ParagraphContent.ItemList -> items.flatMap { item -> item.content.map { it.text } }
            is RenderedJsonLetter.ParagraphContent.Text -> listOf(text)
        }
}