package no.nav.pensjon.brev.routing

import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.TestTags
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.maler.example.EnkeltRedigerbartTestbrev
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.testBrevbakerApp
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Paragraph
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Title1
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block.Title2
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Form.MultipleChoice
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.ItemList.Item
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table.Cell
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table.Header
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Literal
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.NewLine
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.Variable
import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

@Tag(TestTags.INTEGRATION_TEST)
class EndeTilEndeTest {

    private val bestillMarkupRequest = BestillBrevRequest(
        kode = EnkeltRedigerbartTestbrev.kode,
        letterData = EmptyRedigerbarBrevdata,
        felles = Fixtures.felles,
        language = LanguageCode.BOKMAL,
    )

    @Test
    fun `all text is still there`() = testBrevbakerApp { client ->
        val bestillinga = LetterImpl(
            template = EnkeltRedigerbartTestbrev.template,
            argument = bestillMarkupRequest.letterData,
            language = Bokmal,
            felles = bestillMarkupRequest.felles
        ).let { LetterTestRenderer.renderLetterOnly(it) }
            .let {
                with(bestillMarkupRequest) {
                    BestillRedigertBrevRequest(kode, letterData as EmptyRedigerbarBrevdata, felles, language, it)
                }
            }

        val body: LetterResponse = client.post("/letter/redigerbar/pdf") {
            accept(ContentType.Application.Json)
            setBody(bestillinga)
        }.body()

        val tekstIMarkup: List<String> = finnTekstPerAvsnitt(bestillinga.letterMarkup)
        val tekstIPDF = PDFTextStripper().getText(Loader.loadPDF(body.file))

        assertContains(tekstIPDF, bestillinga.letterMarkup.title)

        assertEquals(8, tekstIMarkup.size)
        tekstIMarkup.forEach {
            assertContains(tekstIPDF.fjernWhitespace(), it.fjernWhitespace())
        }
    }
}

private fun String.fjernWhitespace() = replace("\\s".toRegex(), "")

private fun finnTekstPerAvsnitt(letterMarkup: LetterMarkup): List<String> = letterMarkup.blocks.flatMap { finnTekst(it) }

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
    element.prompt.map { it.text } + element.choices.map { it.text }.map { it.joinToString(" ") { i -> i.text } }

private fun finnTekstForTabell(table: Table): List<String> {
    val header = finnTekstForHeader(table.header)
    val rows = table.rows.map { it.cells.joinToString(" ") { c -> finnTekstForCell(c) } }
    return listOf(header) + rows
}

private fun finnTekstForHeader(header: Header): String =
    header.colSpec.map { it.headerContent }.joinToString(" ") { finnTekstForCell(it) }

private fun finnTekstForCell(cell: Cell): String = cell.text.joinToString(" ") { it.text }

private fun finnTekstForItemList(itemList: ItemList): List<String> = itemList.items.map { finnTekstForItem(it) }

private fun finnTekstForItem(item: Item): String = item.content.joinToString(" ") { it.text }