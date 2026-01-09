package no.nav.pensjon.brev.template.render.dsl

import no.nav.brev.brevbaker.FellesFactory.felles
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.text
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.dsl.SomeDtoSelectors.name
import no.nav.pensjon.brev.template.render.hasBlocks
import org.junit.jupiter.api.Test

class ShowIfTest {

    @Test
    fun `showIf renders when condition evaluates to true`() {
        hasBlocks {
            paragraph {
                literal("showIf tekst")
            }
        }(Letter2Markup.render(
            LetterImpl(
                showIfTemplate,
                SomeDto("showIf", false),
                Language.Bokmal,
                felles
            )
        ).letterMarkup)
    }

    @Test
    fun `orShowIf renders when condition evaluates to true`() {
        hasBlocks {
            paragraph {
                literal("orShowIf tekst")
            }
        }(Letter2Markup.render(
            LetterImpl(
                showIfTemplate,
                SomeDto("orShowIf", false),
                Language.Bokmal,
                felles
            )
        ).letterMarkup)
    }

    @Test
    fun `orShow renders when condition evaluates to true`() {
        hasBlocks {
            paragraph {
                literal("orShow tekst")
            }
        }(Letter2Markup.render(
            LetterImpl(
                showIfTemplate,
                SomeDto("orShow", false),
                Language.Bokmal,
                felles
            )
        ).letterMarkup)
    }

    private val showIfTemplate = outlineTestTemplate<SomeDto> {
        paragraph {
            showIf(name equalTo "showIf") {
                text(bokmal { +"showIf tekst" })
            }.orShowIf(name equalTo "orShowIf") {
                text(bokmal { +"orShowIf tekst" })
            } orShow {
                text(bokmal { +"orShow tekst" })
            }
        }
    }

}