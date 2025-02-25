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
                Block.ParagraphImpl(
                    1, true, listOf(
                        ItemListImpl(
                            11, listOf(
                                ItemListImpl.ItemImpl(111, listOf(Text.LiteralImpl(1111, "item 1", Text.FontType.BOLD)))
                            )
                        ),
                        TableImpl(
                            12,
                            listOf(TableImpl.RowImpl(121, listOf(TableImpl.CellImpl(1211, listOf(Text.LiteralImpl(12111, "cell1")))))),
                            TableImpl.HeaderImpl(
                                122,
                                listOf(TableImpl.ColumnSpecImpl(1221, TableImpl.CellImpl(12211, listOf(Text.LiteralImpl(122111, "col1"))), Table.ColumnAlignment.RIGHT, 1))
                            )
                        )
                    )
                ),
                Block.Title1Impl(2, true, emptyList()),
                Block.Title2Impl(
                    3, true, listOf(
                        Text.LiteralImpl(31, "a text"),
                        Text.VariableImpl(32, "another"),
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