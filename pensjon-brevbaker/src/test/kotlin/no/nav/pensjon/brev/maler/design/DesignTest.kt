package no.nav.pensjon.brev.maler.design

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.TestTags
import no.nav.pensjon.brev.renderTestHtml
import no.nav.pensjon.brev.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.outlineTestTemplate
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.VISUAL_TEST)
class DesignTest {
    @Test
    fun `test tittel 1 sammen med ulikt innhold`() {
        val fileName = "DESIGN_TEST_TITLE1"
        Letter(
            outlineTestTemplate<Unit> {
                includePhrase(DesignTestUtils.Title1)
                includePhrase(DesignTestUtils.Title2)

                includePhrase(DesignTestUtils.Title1)
                paragraph { includePhrase(DesignTestUtils.ItemList) }


                includePhrase(DesignTestUtils.Title1)
                paragraph { includePhrase(DesignTestUtils.Lipsum) }

                includePhrase(DesignTestUtils.Title1)
                paragraph {
                    includePhrase(DesignTestUtils.Table)
                }
            }, Unit, Language.Bokmal, Fixtures.fellesAuto
        ).renderTestPDF(fileName).renderTestHtml(fileName)
    }

    @Test
    fun `test tittel 2 sammen med ulikt innhold`() {
        val fileName = "DESIGN_TEST_TITLE2"
        Letter(
            outlineTestTemplate<Unit> {
                includePhrase(DesignTestUtils.Title2)
                paragraph {
                    includePhrase(DesignTestUtils.Table)
                }

                includePhrase(DesignTestUtils.Title2)
                paragraph { includePhrase(DesignTestUtils.ItemList) }

                includePhrase(DesignTestUtils.Title2)
                paragraph { includePhrase(DesignTestUtils.Lipsum) }


            }, Unit, Language.Bokmal, Fixtures.fellesAuto
        ).renderTestPDF(fileName).renderTestHtml(fileName)
    }

    @Test
    fun `tekst sammen med ulikt innhold`() {
        val fileName = "DESIGN_TEST_TEXT"
        Letter(
            outlineTestTemplate<Unit> {
                paragraph {
                    includePhrase(DesignTestUtils.Lipsum)
                    includePhrase(DesignTestUtils.ItemList)
                }

                paragraph {
                    includePhrase(DesignTestUtils.Lipsum)
                    includePhrase(DesignTestUtils.Table)
                }
            }, Unit, Language.Bokmal, Fixtures.fellesAuto
        ).renderTestPDF(fileName).renderTestHtml(fileName)
    }


}