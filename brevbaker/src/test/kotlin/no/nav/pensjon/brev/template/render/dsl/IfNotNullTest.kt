package no.nav.pensjon.brev.template.render.dsl

import com.natpryce.hamkrest.assertion.assertThat
import no.nav.pensjon.brev.template.HasModel
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.render.Fixtures.felles
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.dsl.NullBrevDtoSelectors.test1
import no.nav.pensjon.brev.template.render.dsl.NullBrevDtoSelectors.test2
import no.nav.pensjon.brev.template.render.hasBlocks
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

data class NullBrevDto(val test1: String?, val test2: String?)

@Suppress("unused")
@TemplateModelHelpers
object Helpers : HasModel<NullBrevDto>

class IfNotNullTest {

    val template = createTemplate(
        name = "NULL_BREV",
        letterDataType = NullBrevDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            "Jadda",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title { text(Bokmal to "Heisann") }

        outline {
            paragraph {
                text(Bokmal to "alltid med")
                val nullTing1 = test1
                ifNotNull(nullTing1) { ting ->
                    textExpr(
                        Bokmal to "hei: ".expr() + ting
                    )
                }.orIfNotNull(test2) { ting ->
                    textExpr(
                        Bokmal to "tall: ".expr() + ting
                    )
                }
            }
        }
    }

    @Test
    fun `ifNotNull renders successfully for non-null value`() {
        assertThat(
            Letter2Markup.render(LetterImpl(template, NullBrevDto("Ole", null), Bokmal, felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    literal("alltid med")
                    literal("hei: ")
                    variable("Ole")
                }
            }
        )
    }

    @Test
    fun `ifNotNull renders successfully but without null-block`() {
        assertThat(
            Letter2Markup.render(LetterImpl(template, NullBrevDto(null, null), Bokmal, felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    literal("alltid med")
                }
            }
        )
    }

    @Nested
    @DisplayName("orIfNotNull")
    inner class AbsoluteValue {
        @Test
        fun `renders when preceding condition is not met and orShowIf condition is met`() {
            assertThat(
                Letter2Markup.render(LetterImpl(template, NullBrevDto(null, "138513"), Bokmal, felles)).letterMarkup,
                hasBlocks {
                    paragraph {
                        literal("alltid med")
                        literal("tall: ")
                        variable("138513")
                    }
                }
            )
        }

        @Test
        fun `does not render when preceding condition met`() {
            assertThat(
                Letter2Markup.render(LetterImpl(template, NullBrevDto("Ole", "138513"), Bokmal, felles)).letterMarkup,
                hasBlocks {
                    paragraph {
                        literal("alltid med")
                        literal("hei: ")
                        variable("Ole")
                    }
                }
            )
        }

        @Test
        fun `does not render when preceding condition is not met and orShowIf condition is not met`() {
            assertThat(
                Letter2Markup.render(LetterImpl(template, NullBrevDto(null, null), Bokmal, felles)).letterMarkup,
                hasBlocks {
                    paragraph {
                        literal("alltid med")
                    }
                }
            )
        }
    }

}