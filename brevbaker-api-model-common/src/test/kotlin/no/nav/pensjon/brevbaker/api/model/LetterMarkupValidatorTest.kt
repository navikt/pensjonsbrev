package no.nav.pensjon.brevbaker.api.model

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SakspartImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

@OptIn(InterneDataklasser::class)
class LetterMarkupValidatorTest {

    @Test
    fun `fungerer med avsnitt med innhold`() {
        assertDoesNotThrow {
            letter(
                LetterMarkupImpl.BlockImpl.ParagraphImpl(
                    id = 1,
                    editable = false,
                    content = listOf(
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                            id = 2,
                            text = "hei"
                        ),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(3),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                            id = 4,
                            text = "hallo"
                        ),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(5),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                            id = 7,
                            text = "hallois"
                        ),
                    )
                )
            )
        }
    }

    @Test
    fun `feiler ved tomme avsnitt`() {
        assertThrows<IllegalLetterMarkupException> {
            letter(
                LetterMarkupImpl.BlockImpl.ParagraphImpl(
                    id = 1,
                    editable = false,
                    content = listOf()
                )
            )
        }
    }

    @Test
    fun `feiler ved avsnitt med tomt tekstinnhold`() {
        assertThrows<IllegalLetterMarkupException> {
            letter(
                LetterMarkupImpl.BlockImpl.ParagraphImpl(
                    id = 1,
                    editable = false,
                    content = listOf(
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                            id = 2,
                            text = ""
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `feiler ved to newlines paa rad i starten av avsnittet`() {
        assertThrows<IllegalLetterMarkupException> {
            letter(
                LetterMarkupImpl.BlockImpl.ParagraphImpl(
                    id = 1,
                    editable = false,
                    content = listOf(
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(2),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(3),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                            id = 4,
                            text = "hei"
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `feiler ved to newlines paa rad paa slutten av avsnittet`() {
        assertThrows<IllegalLetterMarkupException> {
            letter(
                LetterMarkupImpl.BlockImpl.ParagraphImpl(
                    id = 1,
                    editable = false,
                    content = listOf(
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                            id = 2,
                            text = "hei"
                        ),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(3),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                            id = 4,
                            text = ""
                        ),
                        LetterMarkupImpl.ParagraphContentImpl.TextImpl.NewLineImpl(5)
                    )
                )
            )
        }
    }

    @Test
    fun `feiler viss rader og header har ulikt antall celler`() {
        assertThrows<IllegalLetterMarkupException> {
            letter(
                LetterMarkupImpl.BlockImpl.ParagraphImpl(
                    id = 1,
                    editable = false,
                    content = listOf(
                        LetterMarkupImpl.ParagraphContentImpl.TableImpl(
                            id = 2,
                            rows = listOf(
                                LetterMarkupImpl.ParagraphContentImpl.TableImpl.RowImpl(
                                    id = 3,
                                    cells = listOf(
                                        LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                            id = 4,
                                            text = listOf(
                                                LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                                    id = 5,
                                                    text = "hei"
                                                )
                                            )
                                        ),
                                        LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                            id = 14,
                                            text = listOf(
                                                LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                                    id = 15,
                                                    text = "hallo"
                                                )
                                            )
                                        )
                                    )
                                )
                            ),
                            header = LetterMarkupImpl.ParagraphContentImpl.TableImpl.HeaderImpl(
                                id = 6,
                                colSpec = listOf(
                                    LetterMarkupImpl.ParagraphContentImpl.TableImpl.ColumnSpecImpl(
                                        id = 7,
                                        headerContent = LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                            id = 8,
                                            text = listOf(
                                                LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                                    id = 9,
                                                    text = "hoi",
                                                )
                                            )
                                        ),
                                        alignment = LetterMarkup.ParagraphContent.Table.ColumnAlignment.LEFT,
                                        span = 1
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
    }


    private fun letter(vararg blocks: LetterMarkup.Block) =
        LetterMarkupImpl(
            title = "En tittel",
            sakspart = SakspartImpl(
                gjelderNavn = "Test Testeson",
                gjelderFoedselsnummer = "1234568910",
                vergeNavn = null,
                saksnummer = "1234",
                dokumentDato = LocalDate.now()
            ),
            blocks = blocks.toList(),
            signatur = SignaturImpl(
                "Med vennlig hilsen",
                "Saksbehandler",
                "Kjersti Saksbehandler",
                null,
                "Nav Familie- og pensjonsytelser Porsgrunn"
            )
        )
}
