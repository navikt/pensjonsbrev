package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import java.util.*

class PensjonLatexTest {
    @Test
    fun `table is not rendered when all the rows are filtered out`() {
        val doc = outlineTestTemplate {
            title1 { text(Bokmal to "THIS TEXT SHOULD RENDER") }
            table {
                title { text(Bokmal to "This text should not render") }
                showIf(true.expr()) {
                    columnHeaderRow {
                        cell {
                            text(
                                Bokmal to "This text should not render",
                            )
                        }
                    }
                }
                columnHeaderRow {
                    cell {
                        text(
                            Bokmal to "This text should not render",
                        )
                    }
                }
                showIf(false.expr()) {
                    row {
                        cell {
                            text(
                                Bokmal to "This text should not render",
                            )
                        }
                    }
                }
            }
        }

        Letter(doc, Unit, Bokmal, Fixtures.felles)
            .assertRenderedLetterDoesNotContainAnyOf("longtblr", "This text should not render")
            .assertRenderedLetterContainsAllOf("THIS TEXT SHOULD RENDER")
    }

    @Test
    fun `all table elements is rendered to LaTeX`() {
        val doc = outlineTestTemplate {
            table {
                title { text(Bokmal to "This text should render 1") }
                columnHeaderRow {
                    cell {
                        text(
                            Bokmal to "This text should render 2",
                        )
                    }
                }
                showIf(true.expr()) {
                    row {
                        cell {
                            text(
                                Bokmal to "This text should render 3",
                            )
                        }
                    }
                }
            }
        }

        Letter(doc, Unit, Bokmal, Fixtures.felles)
            .assertRenderedLetterContainsAllOf(
                "This text should render 1",
                "This text should render 2",
                "This text should render 3",
            )
    }

    @Test
    fun `list is not rendered when items are filtered out`() {
        val doc = outlineTestTemplate {
            title1 { text(Bokmal to "this text should render") }
            list {
                showIf(false.expr()) {
                    item { text(Bokmal to "This text should not render") }
                }
            }
        }

        Letter(doc, Unit, Bokmal, Fixtures.felles)
            .assertRenderedLetterDoesNotContainAnyOf("itemize", "This text should not render")
            .assertRenderedLetterContainsAllOf("this text should render")
    }

}
