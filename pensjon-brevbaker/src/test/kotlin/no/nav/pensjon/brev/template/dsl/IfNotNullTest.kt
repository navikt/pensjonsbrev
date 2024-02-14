package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.NullBrevDtoSelectors.test1
import no.nav.pensjon.brev.template.dsl.NullBrevDtoSelectors.test2
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

data class NullBrevDto(val test1: String?, val test2: String?)
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
                }.orIfNotNull(test2){ ting ->
                    textExpr(
                        Bokmal to "tall: ".expr() + ting
                    )
                }
            }
        }
    }

    @Test
    fun `ifNotNull and orIfNotNull adds a conditional checks`() {
        val navn = Expression.FromScope.argument(ExpressionScope<NullBrevDto, *>::argument).test1
        val noegreier = Expression.FromScope.argument(ExpressionScope<NullBrevDto, *>::argument).test2

        @Suppress("UNCHECKED_CAST") // (navn as Expression<String>)
        val expected = template.copy(
            outline = listOf(
                Content(
                    Element.OutlineContent.Paragraph(
                        listOf(
                            newText(Bokmal to "alltid med"),
                            Conditional(
                                predicate = navn.notNull(),
                                showIf = listOf(
                                    Content(
                                        Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
                                            Bokmal to "hei: ".expr() + (navn as Expression<String>)
                                        )
                                    )
                                ),
                                showElse = listOf(
                                    Conditional(
                                        predicate = noegreier.notNull(),
                                        showIf = listOf(
                                            Content(
                                                Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(
                                                    Bokmal to "tall: ".expr() + (noegreier as Expression<String>)
                                                )
                                            )
                                        ),
                                        showElse = emptyList(),
                                    )
                                ),
                            )
                        )
                    )
                )
            )
        )

        assertEquals(expected, template)
    }

    @Test
    fun `ifNotNull renders successfully for non-null value`() {
        Letter(template, NullBrevDto("Ole", null), Bokmal, felles)
            .assertRenderedLetterContainsAllOf("alltid med", "hei: Ole")
    }

    @Test
    fun `ifNotNull renders successfully but without null-block`() {
        Letter(template, NullBrevDto(null, null), Bokmal, felles)
            .assertRenderedLetterContainsAllOf("alltid med")
            .assertRenderedLetterDoesNotContainAnyOf("hei: Ole")
    }

    @Nested
    @DisplayName("orIfNotNull")
    inner class AbsoluteValue{
        @Test
        fun `renders when preceding condition is not met and orShowIf condition is met`(){
            Letter(template, NullBrevDto(null, "138513"), Bokmal, felles)
                .assertRenderedLetterContainsAllOf("alltid med", "tall: 138513")
                .assertRenderedLetterDoesNotContainAnyOf("hei: Ole")
        }

        @Test
        fun `does not render when preceding condition met`(){
            Letter(template, NullBrevDto("Ole", "138513"), Bokmal, felles)
                .assertRenderedLetterContainsAllOf("alltid med", "hei: Ole")
                .assertRenderedLetterDoesNotContainAnyOf("tall: 138513")
        }

        @Test
        fun `does not render when preceding condition is not met and orShowIf condition is not met`(){
            Letter(template, NullBrevDto(null, null), Bokmal, felles)
                .assertRenderedLetterContainsAllOf("alltid med")
                .assertRenderedLetterDoesNotContainAnyOf("138513", "hei: Ole")
        }
    }

}