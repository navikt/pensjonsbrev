package no.nav.pensjon.brev.routing

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import no.nav.brev.brevbaker.Fixtures.felles
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.fixtures.createLetterExampleDto
import no.nav.pensjon.brev.maler.example.EnkeltRedigerbartTestbrev
import no.nav.pensjon.brev.maler.example.LetterExample
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form.MultipleChoice
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table.Header
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.*
import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@Tag(TestTags.INTEGRATION_TEST)
class BrevtekstITest {


    @Test
    fun `all text is still there`() = testBrevbakerApp { client ->

        val markup = client.post("/letter/redigerbar/markup") {
            accept(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = EnkeltRedigerbartTestbrev.kode,
                    letterData = EmptyRedigerbarBrevdata,
                    felles = felles,
                    language = LanguageCode.BOKMAL,
                )
            )
        }.body<LetterMarkup>()

        val pdf: LetterResponse = client.post("/letter/redigerbar/pdf") {
            accept(ContentType.Application.Json)
            setBody(
                BestillRedigertBrevRequest(
                    kode = EnkeltRedigerbartTestbrev.kode,
                    letterData = EmptyRedigerbarBrevdata,
                    felles = felles,
                    language = LanguageCode.BOKMAL,
                    letterMarkup = markup
                )
            )
        }.body()

        val tekstIMarkup: List<String> = finnTekstPerAvsnitt(markup)
        val tekstIPDF = PDFTextStripper().getText(Loader.loadPDF(pdf.file))

        assertContains(tekstIPDF, markup.title.tekst())

        assertEquals(10, tekstIMarkup.size)
        tekstIMarkup.forEach { assertFalse(it.isEmpty()) }

        tekstIMarkup.forEach {
            assertContains(tekstIPDF.fjernWhitespace(), it.fjernWhitespace())
        }
    }

    @Test
    fun `can read all text from autobrev`() = testBrevbakerApp { client ->
        val pdf: LetterResponse = client.post("/letter/autobrev/pdf") {
            accept(ContentType.Application.Json)
            setBody(
                BestillBrevRequest(
                    kode = LetterExample.kode,
                    letterData = createLetterExampleDto(),
                    felles = felles,
                    language = LanguageCode.BOKMAL,
                )
            )
        }.body()

        val tekstIPDF = PDFTextStripper().getText(Loader.loadPDF(pdf.file))
        assertContains(tekstIPDF, "Saksnummer: 1337123 side 1 av 6")
        assertContains(tekstIPDF, "Du har fått innvilget pensjon")
        assertContains(tekstIPDF, "Hei Test, håper du har en fin dag!")
        assertContains(tekstIPDF, "• Du har fått tilleg1 for Test testerson 2 på 100 kroner\n")
        assertContains(tekstIPDF, "En liste med navn har elementet: test testerson1\n")
        assertContains(tekstIPDF, "Vedlegget gjelder: Test \"bruker\" Testerson\n")
    }
}

private fun String.fjernWhitespace() = replace("\\s".toRegex(), "")

private fun finnTekstPerAvsnitt(letterMarkup: LetterMarkup): List<String> =
    letterMarkup.blocks.flatMap { finnTekst(it) }

private fun finnTekst(block: Block): List<String> = when (block) {
    is Paragraph -> finnTekstForParagraph(block)
    is Title1 -> block.content.map { it.text }
    is Title2 -> block.content.map { it.text }
}

private fun finnTekstForParagraph(paragraph: Paragraph): List<String> = paragraph.content.flatMap {
    when (it) {
        is MultipleChoice -> finnTekstForMultipleChoice(it)
        is ParagraphContent.Form.Text -> it.prompt.map { t -> t.text }
        is ItemList -> finnTekstForItemList(it)
        is Table -> finnTekstForTabell(it)
        is Literal -> listOf(it.text)
        is NewLine -> listOf(it.text)
        is Variable -> listOf(it.text)
    }
}

private fun finnTekstForMultipleChoice(element: MultipleChoice): List<String> =
    element.prompt.map { it.text } + element.choices.map { it.text }.map { it.tekst() }

private fun finnTekstForTabell(table: Table): List<String> {
    val header = finnTekstForHeader(table.header)
    val rows = table.rows.map { it.cells.joinToString(" ") { c -> c.text.tekst() } }
    return listOf(header) + rows
}

private fun finnTekstForHeader(header: Header): String =
    header.colSpec.map { it.headerContent }.joinToString(" ") { it.text.tekst() }

private fun finnTekstForItemList(itemList: ItemList): List<String> = itemList.items.map { it.content.tekst() }

private fun List<ParagraphContent.Text>.tekst() = joinToString("") { it.text }