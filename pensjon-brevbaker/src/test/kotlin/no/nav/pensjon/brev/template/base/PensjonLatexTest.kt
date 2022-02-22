package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.testLetterMetadata
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import java.util.*

class PensjonLatexTest {
    @Test
    fun `table is not rendered when all the rows are filtered out`() {
        val doc = createTemplate(
            name = "TEST",
            base = PensjonLatex,
            letterDataType = Unit::class,
            title = newText(Language.Bokmal to "THIS TEXT SHOULD RENDER"),
            letterMetadata = testLetterMetadata,
        ) {
            outline {
                table {
                    title { text(Language.Bokmal to "This text should not render") }
                    showIf(true.expr()) {
                        columnHeaderRow {
                            cell {
                                text(
                                    Language.Bokmal to "This text should not render",
                                )
                            }
                        }
                    }
                    columnHeaderRow {
                        cell {
                            text(
                                Language.Bokmal to "This text should not render",
                            )
                        }
                    }
                    showIf(false.expr()) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "This text should not render",
                                )
                            }
                        }
                    }
                }
            }
        }

        Letter(doc, Unit, Language.Bokmal, Fixtures.felles)
            .assertRenderedLetterDoesNotContainAnyOf("longtblr", "This text should not render")
            .assertRenderedLetterContainsAllOf("THIS TEXT SHOULD RENDER")
    }

    fun <Param : Any> Letter<Param>.assertRenderedLetterDoesNotContainAnyOf(vararg searchText: String): Letter<Param> {
        val letterString = this.render().base64EncodedFiles()["letter.tex"]
        searchText.forEach {
            Assertions.assertFalse(
                Base64.getDecoder().decode(letterString).toString(Charset.defaultCharset()).contains(it),
                """Letter should not contain "$it""""
            )
        }
        return this
    }
    fun <Param : Any> Letter<Param>.assertRenderedLetterContainsAllOf(vararg searchText: String): Letter<Param> {
        val letterString = this.render().base64EncodedFiles()["letter.tex"]
        searchText.forEach {
            Assertions.assertTrue(
                Base64.getDecoder().decode(letterString).toString(Charset.defaultCharset()).contains(it),
                """Letter should contain "$it""""
            )
        }
        return this
    }
}
