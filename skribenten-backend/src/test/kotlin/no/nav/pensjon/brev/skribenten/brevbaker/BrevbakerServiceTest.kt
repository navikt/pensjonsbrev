package no.nav.pensjon.brev.skribenten.brevbaker

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.brev.Listetype
import no.nav.pensjon.brev.skribenten.serialize.LetterMarkupJacksonModule
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BrevbakerServiceTest {

    private val markup = LetterMarkupImpl(
        title = listOf(LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(-1, "the title")),
        sakspart = LetterMarkupImpl.SakspartImpl(
            gjelderNavn = "about name",
            gjelderFoedselsnummer = Foedselsnummer("12345678910"),
            annenMottakerNavn = null,
            saksnummer = "12345",
            dokumentDato = LocalDate.now(),
        ),
        blocks = listOf(
            LetterMarkupImpl.BlockImpl.ParagraphImpl(
                1, true, listOf(
                    LetterMarkupImpl.ParagraphContentImpl.ItemListImpl(
                        11, listOf(
                            LetterMarkupImpl.ParagraphContentImpl.ItemListImpl.ItemImpl(
                                111,
                                listOf(
                                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                        1111,
                                        "item 1",
                                        LetterMarkup.ParagraphContent.Text.FontType.BOLD
                                    )
                                )
                            )
                        ),
                        Listetype.PUNKTLISTE,
                    ),
                    LetterMarkupImpl.ParagraphContentImpl.TableImpl(
                        12,
                        listOf(
                            LetterMarkupImpl.ParagraphContentImpl.TableImpl.RowImpl(
                                121,
                                listOf(
                                    LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                        1211,
                                        listOf(
                                            LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                                12111,
                                                "cell1"
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        LetterMarkupImpl.ParagraphContentImpl.TableImpl.HeaderImpl(
                            122,
                            listOf(
                                LetterMarkupImpl.ParagraphContentImpl.TableImpl.ColumnSpecImpl(
                                    1221,
                                    LetterMarkupImpl.ParagraphContentImpl.TableImpl.CellImpl(
                                        12211,
                                        listOf(
                                            LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                                                122111,
                                                "col1"
                                            )
                                        )
                                    ),
                                    LetterMarkup.ParagraphContent.Table.ColumnAlignment.RIGHT,
                                    1
                                )
                            )
                        )
                    )
                )
            ),
            LetterMarkupImpl.BlockImpl.Title1Impl(2, true, emptyList()),
            LetterMarkupImpl.BlockImpl.Title2Impl(
                3, true, listOf(
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(31, "a text"),
                    LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl(32, "another"),
                )
            ),
        ),
        signatur = LetterMarkupImpl.SignaturImpl(
            hilsenTekst = "Friendly regards",
            saksbehandlerNavn = "case worker name",
            attesterendeSaksbehandlerNavn = "case worker name2",
            navAvsenderEnhet = "Nav abc"
        )
    )

    val mapper = jacksonObjectMapper().apply {
        registerModule(LetterMarkupJacksonModule)
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Test
    fun testJacksonModule() {
        Assertions.assertEquals(markup, mapper.readValue<LetterMarkup>(mapper.writeValueAsString(markup)))
    }

}