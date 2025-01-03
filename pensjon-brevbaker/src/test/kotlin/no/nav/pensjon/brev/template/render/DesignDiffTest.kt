package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.renderTestPdfOutline
import no.nav.pensjon.brev.renderTestVedleggPdf
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

// kjør denne først, så script i test resources.
@Tag(TestTags.MANUAL_TEST)
class DesignDiffTest() {
    private fun render(
        page: Int,
        felles: Felles? = null,
        brevtype: LetterMetadata.Brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        outlineInit: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit,
    ) {
        renderTestPdfOutline(
            outputFolder = "test_design_diff",
            testName = "design_test_page_$page",
            felles = felles,
            brevtype = brevtype,
            outlineInit = outlineInit,
            title = "Brevtittel"
        )
    }

    private fun renderTestVedlegg(
        page: Int,
        vedleggOutlineInit: OutlineOnlyScope<LangBokmal, EmptyBrevdata>.() -> Unit,
    ) {
        renderTestVedleggPdf(
            outputFolder = "test_design_diff",
            testName = "design_test_page_$page",
            includeSakspart = false,
            outlineInit = vedleggOutlineInit,
            title = "Tittel vedlegg"
        )
    }


    @Test
    fun `diff design side 1`() {
        render(page = 1) {
            title1 {
                text(
                    Bokmal to "Overskrift",
                )
            }
            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. "
                )
            }
            title2 {
                text(
                    Bokmal to "Underoverskrift nivå en",
                )
            }

            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                            "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                            "imperdiet viverra tortor.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo.",
                )
            }
            title2 {
                text(
                    Bokmal to "Underoverskrift nivå to",
                )
            }
            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                            "posuere porta nec eu elit. Integer nec vestibulum leo.",
                )
            }
        }
    }

    @Test
    fun `diff design side 2`() {
        render(
            page = 2,
            felles = Fixtures.felles.copy(
                signerendeSaksbehandlere = null,
                vergeNavn = "Verge vergeson",
            ),

        ) {
            title1 {
                text(
                    Bokmal to "Overskrift",
                )
            }
            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. "
                )
            }
            title2 {
                text(
                    Bokmal to "Underoverskrift nivå en",
                )
            }

            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                            "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                            "imperdiet viverra tortor.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo.",
                )
            }
            title2 {
                text(
                    Bokmal to "Underoverskrift nivå to",
                )
            }
            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                            "posuere porta nec eu elit. Integer nec vestibulum leo.",
                )
            }
        }
    }


    @Test
    fun `diff design side 6`() {
        renderTestVedlegg(page = 6) {
            title1 {
                text(
                    Bokmal to "Overskrift",
                )
            }
            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                            "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                            "imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum\n" +
                            "leo.",
                )
            }
            title2 {
                text(
                    Bokmal to "Underoverskrift",
                )
            }
            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam\n" +
                            "posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae,\n" +
                            "imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum\n" +
                            "leo.",
                )
            }
            paragraph {
                table(header = {
                    column { text(Bokmal to "Type inntekt") }
                    column { text(Bokmal to "Mottatt av") }
                    column(alignment = RIGHT) { text(Bokmal to "Inntekt") }
                }) {
                    row { cell { text(Bokmal to "Arbeidsinntekt") }; cell { text(Bokmal to "<RegistreringsKilde>") }; cell { text(Bokmal to "44 520 kr") } }
                    row { cell { text(Bokmal to "Næringsinntekt") }; cell { text(Bokmal to "<RegistreringsKilde>") }; cell { text(Bokmal to "8 000 kr") } }
                    row { cell { text(Bokmal to "Næringsinntekt") }; cell { text(Bokmal to "<RegistreringsKilde>") }; cell { text(Bokmal to "8 000 kr") } }
                    row { cell { text(Bokmal to "Arbeidsinntekt") }; cell { text(Bokmal to "<RegistreringsKilde>") }; cell { text(Bokmal to "44 520 kr") } }
                    row { cell { text(Bokmal to "Arbeidsinntekt") }; cell { text(Bokmal to "<RegistreringsKilde>") }; cell { text(Bokmal to "44 520 kr") } }
                    row { cell { text(Bokmal to "Arbeidsinntekt") }; cell { text(Bokmal to "<RegistreringsKilde>") }; cell { text(Bokmal to "44 520 kr") } }
                }
            }
        }
    }
}