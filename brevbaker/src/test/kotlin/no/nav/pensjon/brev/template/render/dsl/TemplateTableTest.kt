package no.nav.pensjon.brev.template.render.dsl

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.render.assertHasBlocks
import org.junit.jupiter.api.Test

class TemplateTableTest {

    @Test
    fun `table is not rendered when all the rows are filtered out`() {
        val doc = outlineTestTemplate<EmptyAutobrevdata> {
            title1 { text(bokmal { +"THIS TEXT SHOULD RENDER" }) }
            paragraph {
                table(
                    header = {
                        column {
                            text(
                                bokmal { +"This text should not render1" },
                            )
                        }
                    }
                ) {
                    showIf(false.expr()) {
                        row {
                            cell {
                                text(
                                    bokmal { +"This text should not render2" },
                                )
                            }
                        }
                    }
                }
            }
        }

        val actual = Letter2Markup.render(LetterImpl(doc, EmptyAutobrevdata, Language.Bokmal, FellesFactory.felles)).letterMarkup
        actual.assertHasBlocks {
            title1 { literal("THIS TEXT SHOULD RENDER") }
            paragraph { }
        }
    }

    @Test
    fun `all table elements are rendered`() {
        val doc = outlineTestTemplate<EmptyAutobrevdata> {
            paragraph {
                table(
                    header = {
                        column {
                            text(
                                bokmal { +"This text should render 1" },
                            )
                        }
                    }
                ) {
                    showIf(true.expr()) {
                        row {
                            cell {
                                text(
                                    bokmal { +"This text should render 2" },
                                )
                            }
                        }
                    }
                }
            }
        }

        Letter2Markup.render(LetterImpl(doc, EmptyAutobrevdata, Language.Bokmal, FellesFactory.felles)).letterMarkup.assertHasBlocks {
            paragraph {
                table {
                    header { column { literal("This text should render 1") } }
                    row { cell { literal("This text should render 2") } }
                }
            }
        }
    }
}