package no.nav.pensjon.brev.template.render.dsl

import no.nav.brev.brevbaker.createTemplate
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.dsl.NullBrevDtoSelectors.test1
import no.nav.pensjon.brev.template.render.dsl.NullBrevDtoSelectors.test2
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

data class NullBrevDto(val test1: String?, val test2: String?)

@Suppress("unused")
@TemplateModelHelpers
object Helpers : HasModel<NullBrevDto>

class IfNotNullTest {

    val template = createTemplate(
        letterDataType = NullBrevDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            "Jadda",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title { text(bokmal { +"Heisann" }) }

        outline {
            paragraph {
                text(bokmal { +"alltid med" })
                val nullTing1 = test1
                ifNotNull(nullTing1) { ting ->
                    text(
                        bokmal { +"hei: " + ting }
                    )
                }.orIfNotNull(test2) { ting ->
                    text(
                        bokmal { +"tall: " + ting }
                    )
                }
            }
        }
    }

    @Test
    fun `ifNotNull and orIfNotNull adds a conditional checks`() {
        val navn = Expression.FromScope.Argument<NullBrevDto>().test1
        val noegreier = Expression.FromScope.Argument<NullBrevDto>().test2

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
}

fun <Lang : LanguageSupport, LetterData : Any> LetterTemplate<Lang, LetterData>.copy(outline: List<OutlineElement<Lang>>) = LetterTemplate(
    title = this.title,
    letterDataType = this.letterDataType,
    language = this.language,
    outline = outline,
    attachments = this.attachments,
    letterMetadata = this.letterMetadata
)