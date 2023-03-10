package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.RenderedJsonLetter.*
import org.junit.Test
import kotlin.test.assertEquals

// TODO: PL-6535 Kommenter inn igjen!!
class UpdateRenderedLetterTest {

    //TODO: Burde denne kun teste at VARIABLE er endret?
//    @Test
//    fun `updates variables and not literals`() {
//        val edited = letter(
//            block(
//                Content.Type.LITERAL to "Det var en gang ",
//                Content.Type.VARIABLE to "Ole",
//                Content.Type.LITERAL to " ikke bodde i et hus.",
//            )
//        )
//        val next = letter(
//            block(
//                Content.Type.LITERAL to "Det var en gang ",
//                Content.Type.VARIABLE to "Gunnar",
//                Content.Type.LITERAL to " bodde i et hus.",
//            )
//        )
//        val actual = updatedEditedLetter(edited, next).blocks.first().content
//
//        assertEquals(edited.blocks.first().content[0], actual[0])
//        assertEquals(next.blocks.first().content[1], actual[1])
//        assertEquals(edited.blocks.first().content[2], actual[2])
//    }
//
//    @Test
//    fun `adds new literals and keeps edited`() {
//        val edited = letter(
//            block(
//                Content.Type.LITERAL to "Det var en gang",
//                Content.Type.VARIABLE to "Ole",
//                Content.Type.LITERAL to " ikke bodde i et hus.",
//            )
//        )
//        val next = letter(
//            block(
//                Content.Type.LITERAL to "Det var en gang ",
//                Content.Type.VARIABLE to "Ole",
//                Content.Type.LITERAL to " bodde i et hus.",
//                Content.Type.LITERAL to " Der hadde han det veldig fint"
//            )
//        )
//        val actual = updatedEditedLetter(edited, next)
//
//        assertEquals(edited.blocks.first().content[2], actual.blocks.first().content[2])
//        assertEquals(next.blocks.first().content[3], actual.blocks.first().content[3])
//    }
//
//    @Test
//    fun `removes content`() {
//        val edited = letter(
//            block(
//                Content.Type.LITERAL to "Det var en gang",
//                Content.Type.VARIABLE to "Ole",
//                Content.Type.LITERAL to " ikke bodde i et hus.",
//            )
//        )
//        val next = letter(edited.blocks.first().let { it.copy(content = it.content.subList(1, it.content.size)) })
//        val actual = updatedEditedLetter(edited, next)
//
//        assertEquals(edited.blocks.first().content[1], actual.blocks.first().content[0])
//        assertEquals(edited.blocks.first().content[2], actual.blocks.first().content[1])
//    }
//
//
//    private fun letter(vararg blocks: Block) =
//        RenderedJsonLetter(
//            title = "En tittel",
//            sakspart = Sakspart("Test Testeson", "1234568910", "1234", "20.12.2022"),
//            blocks = blocks.toList(),
//            signatur = Signatur("Med vennlig hilsen", "Saksbehandler", "Kjersti Saksbehandler", null, "NAV Familie- og pensjonsytelser Porsgrunn")
//        )
//
//    private fun block(vararg content: Pair<Content.Type, String>) =
//        Block(
//            id = 1,
//            type = Block.Type.PARAGRAPH,
//            location = emptyList(),
//            content = content.mapIndexed { index, c -> Content(c.first, index, emptyList(), c.second) },
//            editable = true
//        )
}