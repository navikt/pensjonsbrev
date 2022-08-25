package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.NullBrevDtoSelectors.test1
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

data class NullBrevDto(val test1: String?)
@TemplateModelHelpers
object Helpers : HasModel<NullBrevDto>

class IfNotNullTest {

    val template = createTemplate(
        name = "NULL_BREV",
        base = PensjonLatex,
        letterDataType = NullBrevDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            "Jadda",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
        )
    ) {
        title { text(Bokmal to "Heisann") }

        outline {
            text(Bokmal to "alltid med")
            val nullTing1 = test1
            ifNotNull(nullTing1) { ting ->
                textExpr(
                    Bokmal to "hei: ".expr() + ting
                )
            }
        }
    }

    @Test
    fun `ifNotNull adds a conditional with null check for navn`() {
        val navn = Expression.FromScope(ExpressionScope<NullBrevDto, *>::argument).test1

        @Suppress("UNCHECKED_CAST") // (navn as Expression<String>)
        val expected = template.copy(
            outline = listOf(
                newText(Bokmal to "alltid med"),
                Conditional(
                    predicate = navn.notNull(),
                    showIf = listOf(
                        Content(
                            Element.ParagraphContent.Text.Expression.ByLanguage.create(
                                Bokmal to "hei: ".expr() + (navn as Expression<String>)
                            )
                        )
                    ),
                    showElse = emptyList(),
                )
            )
        )

        assertEquals(expected, template)
    }

    @Test
    fun `ifNotNull renders successfully for non-null value`() {
        Letter(template, NullBrevDto("Ole"), Bokmal, felles)
            .assertRenderedLetterContainsAllOf("alltid med", "hei: Ole")
    }

    @Test
    fun `ifNotNull renders successfully but without null-block`() {
        Letter(template, NullBrevDto(null), Bokmal, felles)
            .assertRenderedLetterContainsAllOf("alltid med")
            .assertRenderedLetterDoesNotContainAnyOf("hei:")
    }
}