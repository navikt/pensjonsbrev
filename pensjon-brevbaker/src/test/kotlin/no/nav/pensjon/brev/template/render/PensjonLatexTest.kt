package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.assertRenderedLetterContainsAllOf
import no.nav.pensjon.brev.template.assertRenderedLetterDoesNotContainAnyOf
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.outlineTestTemplate
import org.junit.jupiter.api.Test

class PensjonLatexTest {
    @Test
    fun `table is not rendered when all the rows are filtered out`() {
        val doc = outlineTestTemplate<Unit> {
            title1 { text(Bokmal to "THIS TEXT SHOULD RENDER") }
            table(
                header = {
                    column {
                        text(
                            Bokmal to "This text should not render1",
                        )
                    }
                }
            ) {
                showIf(false.expr()) {
                    row {
                        cell {
                            text(
                                Bokmal to "This text should not render2",
                            )
                        }
                    }
                }
            }
        }

        Letter(doc, Unit, Bokmal, Fixtures.felles)
            .assertRenderedLetterDoesNotContainAnyOf(
                "longtblr",
                "This text should not render1",
                "This text should not render2"
            )
            .assertRenderedLetterContainsAllOf("THIS TEXT SHOULD RENDER")
    }

    @Test
    fun `all table elements is rendered to LaTeX`() {
        val doc = outlineTestTemplate<Unit> {
            table(
                header = {
                    column {
                        text(
                            Bokmal to "This text should render 1",
                        )
                    }
                }
            ) {
                showIf(true.expr()) {
                    row {
                        cell {
                            text(
                                Bokmal to "This text should render 2",
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
            )
    }

    @Test
    fun `list is not rendered when items are filtered out`() {
        val doc = outlineTestTemplate<Unit> {
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
