package no.nav.pensjon.brev.template.render.dsl

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.hasBlocks
import org.junit.jupiter.api.Test

class TemplateTableTest {

    @Test
    fun `table is not rendered when all the rows are filtered out`() {
        val doc = outlineTestTemplate<Unit> {
            title1 { text(Language.Bokmal to "THIS TEXT SHOULD RENDER") }
            paragraph {
                table(
                    header = {
                        column {
                            text(
                                Language.Bokmal to "This text should not render1",
                            )
                        }
                    }
                ) {
                    showIf(false.expr()) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "This text should not render2",
                                )
                            }
                        }
                    }
                }
            }
        }

        val actual = Letter2Markup.render(LetterImpl(doc, Unit, Language.Bokmal, FellesFactory.felles)).letterMarkup
        assertThat(
            actual,
            hasBlocks {
                title1 { literal("THIS TEXT SHOULD RENDER") }
                paragraph { }
            }
        )
    }

    @Test
    fun `all table elements are rendered`() {
        val doc = outlineTestTemplate<Unit> {
            paragraph {
                table(
                    header = {
                        column {
                            text(
                                Language.Bokmal to "This text should render 1",
                            )
                        }
                    }
                ) {
                    showIf(true.expr()) {
                        row {
                            cell {
                                text(
                                    Language.Bokmal to "This text should render 2",
                                )
                            }
                        }
                    }
                }
            }
        }

        assertThat(
            Letter2Markup.render(LetterImpl(doc, Unit, Language.Bokmal, FellesFactory.felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    table {
                        header { column { literal("This text should render 1") } }
                        row { cell { literal("This text should render 2") } }
                    }
                }
            }
        )
    }
}