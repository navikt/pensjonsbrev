package no.nav.pensjon.brev.template.render.dsl

import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.template.render.hasBlocks
import org.junit.jupiter.api.Test

class TemplateListTest {

    @Test
    fun `list is not rendered when items are filtered out`() {
        val doc = outlineTestTemplate<EmptyAutobrevdata> {
            title1 { text(bokmal { +"this text should render" }) }
            paragraph {
                list {
                    showIf(false.expr()) {
                        item { text(bokmal { +"This text should not render" }) }
                    }
                }
            }
        }

        Letter2Markup.render(LetterImpl(doc, EmptyAutobrevdata, Language.Bokmal, FellesFactory.felles)).letterMarkup.hasBlocks {
            title1 { literal("this text should render") }
            paragraph { }
        }
    }
}