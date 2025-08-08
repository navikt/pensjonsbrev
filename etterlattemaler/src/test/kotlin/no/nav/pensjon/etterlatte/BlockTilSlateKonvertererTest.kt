package no.nav.pensjon.etterlatte


import io.mockk.mockk
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.Brevbaker
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.ForhaandsvarselOmregningBP
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

@OptIn(InterneDataklasser::class)
class BlockTilSlateKonvertererTest {
    @Test
    fun `kan lese inn letter markup fra brevbakeren`() {
        val letter = lesInnBrev(ForhaandsvarselOmregningBP.template, Fixtures.create())
        val letterMarkup = Brevbaker(mockk()).renderLetterMarkup(letter)
        val konvertert = BlockTilSlateKonverterer.konverter(letterMarkup)
        assertEquals(konvertert.elements.size, letterMarkup.blocks.size)
    }

    private fun <T : BrevbakerBrevdata> lesInnBrev(template: LetterTemplate<*, T>, arg: T): LetterImpl<T> {
        val letter = LetterImpl(
            template,
            arg,
            Bokmal,
            Fixtures.felles
        )
        return letter
    }

    @Test
    fun `skal konvertere title1, title2 og paragraph til HEADING_TWO, HEADING_THREE og PARAGRAPH`() {
        val letterMarkup = brevbaker_payload_med_title1_title2_og_paragraf
        val konvertert = BlockTilSlateKonverterer.konverter(letterMarkup)

        assertEquals(konvertert.elements.size, letterMarkup.blocks.size)
        assertEquals(ElementType.HEADING_TWO, konvertert.elements[0].type)
        assertEquals(ElementType.HEADING_THREE, konvertert.elements[1].type)
        assertEquals(ElementType.PARAGRAPH, konvertert.elements[2].type)
        assertEquals(konvertert.elements.size, 3)
    }

    @Test
    fun `skal konvertere item_list til BULLETED_LIST og flytte fra paragraph til toppnode`() {
        val letterMarkup = brevbaker_payload_med_item_list
        val konvertert = BlockTilSlateKonverterer.konverter(letterMarkup)

        assertEquals(ElementType.PARAGRAPH, konvertert.elements[0].type)
        assertEquals(ElementType.BULLETED_LIST, konvertert.elements[1].type)
        assertEquals(3, konvertert.elements[1].children.size)
        konvertert.elements[1].children.forEach {
            assertEquals(ElementType.LIST_ITEM, it.type)
            assertEquals(ElementType.PARAGRAPH, it.children?.first()?.type)
            assertNotNull(it.children?.first()?.text)
        }
        assertEquals(ElementType.PARAGRAPH, konvertert.elements[2].type)
        assertEquals(2, konvertert.elements[2].children.size)
        assertEquals(ElementType.PARAGRAPH, konvertert.elements[3].type)
        assertEquals(konvertert.elements.size, 4)
    }

    private val brevbaker_payload_med_title1_title2_og_paragraf = LetterMarkupImpl(
        title = "Et dokument fra brevbakeren",
        sakspart = LetterMarkupImpl.SakspartImpl(
            gjelderNavn = "Ola Nordmann",
            gjelderFoedselsnummer = "1",
            saksnummer = "1",
            dokumentDato = LocalDate.of(2024, Month.JANUARY, 1),
            vergeNavn = null,
        ),
        signatur = LetterMarkupImpl.SignaturImpl(
            hilsenTekst = "Med vennlig hilsen",
            saksbehandlerRolleTekst = "Saksbehandler",
            saksbehandlerNavn = "Ole Saksbehandler",
            attesterendeSaksbehandlerNavn = "Per Attesterende",
            navAvsenderEnhet = "NAV Familie- og pensjonsytelser Porsgrunn"
        ),
        blocks = listOf(
            LetterMarkupImpl.BlockImpl.Title1Impl(
                id = 1,
                editable = true,
                content = listOf(
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 2,
                        text = "Dette er title1",
                    )
                )
            ),
            LetterMarkupImpl.BlockImpl.Title2Impl(
                id = 3,
                editable = true,
                content = listOf(
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 4,
                        text = "Dette er title2",
                    )
                )
            ),
            LetterMarkupImpl.BlockImpl.ParagraphImpl(
                id = 5,
                editable = true,
                content = listOf(
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 6,
                        text = "Dette er en paragraf",
                    )
                )
            )
        )
    )

    private val brevbaker_payload_med_item_list = LetterMarkupImpl(
        title = "Et dokument fra brevbakeren",
        sakspart = LetterMarkupImpl.SakspartImpl(
            gjelderNavn = "Ola Nordmann",
            gjelderFoedselsnummer = "1",
            saksnummer = "1",
            dokumentDato = LocalDate.of(2024, Month.JANUARY, 1),
            vergeNavn = null,
        ),
        blocks = listOf(
            LetterMarkupImpl.BlockImpl.ParagraphImpl(
                id = 1,
                editable = true,
                content = listOf(
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 2,
                        text = "Tekst før punktliste blir tatt ut som eget paragraf-element.",
                    ),
                    LetterMarkupImpl.ParagraphContentImpl.ItemListImpl(
                        id = 3,
                        items = listOf(
                            LetterMarkupImpl.ParagraphContentImpl.ItemListImpl.ItemImpl(
                                id = 44,
                                content = listOf(
                                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                        id = 4,
                                        text = "Denne punktlisten blir"
                                    )
                                )
                            ),
                            LetterMarkupImpl.ParagraphContentImpl.ItemListImpl.ItemImpl(
                                id = 55,
                                content = listOf(
                                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                        id = 5,
                                        text = "Trukket opp ett nivå"
                                    )
                                )
                            ),
                            LetterMarkupImpl.ParagraphContentImpl.ItemListImpl.ItemImpl(
                                id = 66,
                                content = listOf(
                                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                        id = 6,
                                        text = "Slik at den kan editeres i slate-editor"
                                    )
                                )
                            ),
                        )
                    ),
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 7,
                        text = "Tekst under punktlisten.",
                    ),
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 8,
                        text = "Blir slått sammen til ett paragraf-element så lenge det allerede befinner seg i samme paragraf-element."
                    )
                )
            ),
            LetterMarkupImpl.BlockImpl.ParagraphImpl(
                id = 9,
                editable = true,
                content = listOf(
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                        id = 10,
                        text = "Frittstående paragraf under punktlisten vil fortsatt være frittstående.",
                    )
                )
            )
        ),
        signatur = LetterMarkupImpl.SignaturImpl(
            hilsenTekst = "Med vennlig hilsen",
            saksbehandlerRolleTekst = "Saksbehandler",
            saksbehandlerNavn = "Ole Saksbehandler",
            attesterendeSaksbehandlerNavn = "Per Attesterende",
            navAvsenderEnhet = "NAV Familie- og pensjonsytelser Porsgrunn"
        )
    )
}