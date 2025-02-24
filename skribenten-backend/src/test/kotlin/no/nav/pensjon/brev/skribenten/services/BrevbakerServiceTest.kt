package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class BrevbakerServiceTest {

    @Test
    fun testJacksonModule() {
        val markup = LetterMarkup(
            title = "the title",
            sakspart = SakspartImpl(
                gjelderNavn = "about name",
                gjelderFoedselsnummer = "12345678910",
                saksnummer = "12345",
                dokumentDato = "2024-05-16",
            ),
            blocks = listOf(
                Block.Paragraph(
                    1, true, listOf(
                        ItemList(
                            11, listOf(
                                ItemList.Item(111, listOf(Text.Literal(1111, "item 1", Text.FontType.BOLD)))
                            )
                        ),
                        Table(
                            12,
                            listOf(Table.Row(121, listOf(Table.Cell(1211, listOf(Text.Literal(12111, "cell1")))))),
                            Table.Header(
                                122,
                                listOf(Table.ColumnSpec(1221, Table.Cell(12211, listOf(Text.Literal(122111, "col1"))), Table.ColumnAlignment.RIGHT, 1))
                            )
                        )
                    )
                ),
                Block.Title1(2, true, emptyList()),
                Block.Title2(
                    3, true, listOf(
                        Text.Literal(31, "a text"),
                        Text.Variable(32, "another"),
                    )
                ),
            ),
            signatur = SignaturImpl(
                hilsenTekst = "Friendly regards",
                saksbehandlerRolleTekst = "case worker",
                saksbehandlerNavn = "case worker name",
                attesterendeSaksbehandlerNavn = "case worker name2",
                navAvsenderEnhet = "Nav abc"
            )
        )
        val mapper = jacksonObjectMapper().apply {
            registerModule(LetterMarkupModule)
        }

        assertEquals(markup, mapper.readValue<LetterMarkup>(mapper.writeValueAsString(markup)))
    }
}