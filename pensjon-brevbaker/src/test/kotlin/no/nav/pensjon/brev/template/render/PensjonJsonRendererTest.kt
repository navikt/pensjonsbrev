package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.api.model.Block
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PensjonJsonRendererTest {

    private inline fun <reified LetterData : Any> renderTemplate(data: LetterData, noinline template: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit) =
        PensjonJsonRenderer.render(Letter(outlineTestTemplate(template), data, Bokmal, felles)).also {
            jacksonObjectMapper().writeValue(System.out, it.blocks)
        }

    @Test
    fun `outline root elements are rendered in same order`() {
        val result = renderTemplate(Unit) {
            title1 { text(Bokmal to "hei tittel") }
            paragraph { text(Bokmal to "hei paragraph") }
            paragraph { text(Bokmal to "hei paragraph2") }
        }

        assertEquals(
            listOf("hei tittel", "hei paragraph", "hei paragraph2"),
            result.blocks.map { it.content.firstOrNull()?.text },
        )
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
    fun `text element renders as block of type TEXT`() {
        val result = renderTemplate(Unit) { text(Bokmal to "hei") }

        assertEquals(Block.Type.TEXT, result.blocks.firstOrNull()?.type)
    }

    @Test
    fun `paragraph content is rendered in order with location`() {
        val result = renderTemplate(Unit) {
            paragraph {
                text(Bokmal to "first")
                text(Bokmal to "second")
            }
        }

        assertEquals(listOf("first", "second"), result.blocks.first().content.map { it.text })
        assertEquals(listOf("0"), result.blocks.first().content.first().location)
    }

    @Test
    fun `title1 content is rendered in order with location`() {
        val result = renderTemplate(Unit) {
            title1 {
                text(Bokmal to "first")
                text(Bokmal to "second")
            }
        }

        assertEquals(listOf("first", "second"), result.blocks.first().content.map { it.text })
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

    @Test
    fun `repeated equal nested elements are distinguishable by location`() {
        val (p1, p2) = renderTemplate(Unit) {
            paragraph {
                text(Bokmal to "hei")
                text(Bokmal to "hei")
            }
        }.blocks[0].content

        assertEquals(p1.id, p2.id)
        assertNotEquals(p1.location, p2.location)
    }

    @Test
    fun `control structures adds step to location but still counts blocks in outer scope`() {
        val result = renderTemplate(Unit) {
            text(Bokmal to "before")
            forEach(listOf("hei", "joda").expr()) {
                text(Bokmal to "abc")
            }
            text(Bokmal to "after")
        }

        assertEquals(listOf("0"), result.blocks[0].location)
        assertEquals(listOf("c", "1"), result.blocks[1].location)
        assertEquals(listOf("c", "2"), result.blocks[2].location)
        assertEquals(listOf("3"), result.blocks[3].location)
    }

    @Test
    fun `forEach rendering adds step to location`() {
        val result = renderTemplate(Unit) {
            forEach(listOf("hei", "joda").expr()) {
                text(Bokmal to "abc")
            }
        }

        assertEquals(2, result.blocks.size)
        assertEquals(listOf("c", "0"), result.blocks[0].location)
        assertEquals(listOf("c", "1"), result.blocks[1].location)
    }

    @Test
    fun `showIf rendering adds step to location`() {
        val result = renderTemplate(Unit) {
            showIf(true.expr()) {
                text(Bokmal to "abc")
            }
        }

        assertEquals(1, result.blocks.size)
        assertEquals(listOf("c", "0"), result.blocks[0].location)
    }

    @Test
    fun `content in standalone text-block adds location for content`() {
        val result = renderTemplate(Unit) {
            text(Bokmal to "jadda")
            text(Bokmal to "abc")
        }

        assertEquals(listOf("0"), result.blocks[0].content[0].location)
        assertEquals(listOf("0"), result.blocks[1].content[0].location)
    }

}