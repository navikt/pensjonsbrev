package no.nav.pensjon.brev.template.render.dsl

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.brev.brevbaker.FellesFactory.felles
import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.dsl.SomeDtoSelectors.name
import no.nav.pensjon.brev.template.render.hasBlocks
import org.junit.jupiter.api.Test

class ShowIfTest {

    @Test
    fun `showIf renders when condition evaluates to true`() {
        assertThat(
            Letter2Markup.render(
                LetterImpl(
                    showIfTemplate,
                    SomeDto("showIf", false),
                    Language.Bokmal,
                    felles
                )
            ).letterMarkup,
            hasBlocks {
                paragraph {
                    literal("showIf tekst")
                }
            }
        )
    }

    @Test
    fun `orShowIf renders when condition evaluates to true`() {
        assertThat(
            Letter2Markup.render(
                LetterImpl(
                    showIfTemplate,
                    SomeDto("orShowIf", false),
                    Language.Bokmal,
                    felles
                )
            ).letterMarkup,
            hasBlocks {
                paragraph {
                    literal("orShowIf tekst")
                }
            }
        )
    }

    @Test
    fun `orShow renders when condition evaluates to true`() {
        assertThat(
            Letter2Markup.render(
                LetterImpl(
                    showIfTemplate,
                    SomeDto("orShow", false),
                    Language.Bokmal,
                    felles
                )
            ).letterMarkup,
            hasBlocks {
                paragraph {
                    literal("orShow tekst")
                }
            }
        )
    }

    private val showIfTemplate = outlineTestTemplate<SomeDto> {
        paragraph {
            showIf(name equalTo "showIf") {
                text(Language.Bokmal to "showIf tekst")
            }.orShowIf(name equalTo "orShowIf") {
                text(Language.Bokmal to "orShowIf tekst")
            } orShow {
                text(Language.Bokmal to "orShow tekst")
            }
        }
    }

}