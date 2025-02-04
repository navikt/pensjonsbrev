package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.choice
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.render.dsl.SomeDtoSelectors.name
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateTest {

    @Test
    fun `createTemplate can add outline with title1 using text-builder`() {
        val doc = createTemplate(
            name = "test",
            letterDataType = Unit::class,
            languages = languages(Language.Bokmal),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(bokmalTittel)
            outline {
                title1 {
                    text(Language.Bokmal to "Heisann. ")
                }
            }
        }

        assertEquals(
            LetterTemplate(
                name = "test",
                title = listOf(bokmalTittel),
                letterDataType = Unit::class,
                language = languages(Language.Bokmal),
                outline = listOf(
                    Content(
                        Element.OutlineContent.Title1(
                            listOf(
                                Content(
                                    Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "Heisann. ")
                                ),
                            )
                        )
                    )
                ),
                letterMetadata = testLetterMetadata
            ),
            doc
        )
    }


    @Test
    fun `createTemplate adds attachment`() {
        val attachment = createAttachment<LangBokmalNynorskEnglish, Unit>(
            title = newText(
                Language.Bokmal to "asdf",
                Language.Nynorsk to "asdf",
                Language.English to "asdf",
            ),
        ) {
            paragraph {
                text(
                    Language.Bokmal to "hei",
                    Language.Nynorsk to "hei",
                    Language.English to "Hello",
                )
            }
        }

        val doc = createTemplate(
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Language.Bokmal),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(bokmalTittel)
            includeAttachment(attachment, Expression.Literal(Unit))
        }

        assertEquals(
            LetterTemplate(
                name = "test",
                title = listOf(bokmalTittel),
                letterDataType = SomeDto::class,
                language = languages(Language.Bokmal),
                outline = emptyList(),
                attachments = listOf(
                    IncludeAttachment(
                        Expression.Literal(Unit),
                        attachment
                    )
                ),
                letterMetadata = testLetterMetadata
            ),
            doc
        )
    }

    @Test
    fun `TemplateTextOnlyScope_eval adds Expression element`() {
        val element = TextOnlyScope<LangBokmal, SomeDto>().apply {
            eval(name)
        }.elements.first()

        val expected = Content(
            Element.OutlineContent.ParagraphContent.Text.Expression<LangBokmal>(
                Expression.FromScope.Argument<SomeDto>().name
            )
        )

        assertEquals(expected, element)
    }

    @Test
    fun `createTemplate adds literal title`() {
        val doc = createTemplate(
            name = "test",
            letterDataType = Unit::class,
            languages = languages(Language.Bokmal),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(bokmalTittel)
            outline {
                title1 { text(Language.Bokmal to "jadda") }
            }
        }

        assertEquals(
            LetterTemplate(
                name = "test",
                title = listOf(bokmalTittel),
                letterDataType = Unit::class,
                language = languages(Language.Bokmal),
                outline = listOf(
                    Content(
                        Element.OutlineContent.Title1(
                            listOf(
                                Content(
                                    Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "jadda")
                                )
                            )
                        )
                    )
                ),
                letterMetadata = testLetterMetadata
            ), doc
        )
    }

    @Test
    fun `createTemplate adds outline`() {
        val doc = createTemplate(
            name = "test",
            letterDataType = Unit::class,
            languages = languages(Language.Bokmal),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(bokmalTittel)
            outline {
                title1 { text(Language.Bokmal to "Tittel") }
                paragraph {
                    text(Language.Bokmal to "Dette er tekst som kun brukes i dette brevet.")
                }
            }
        }

        assertEquals(
            LetterTemplate(
                name = "test",
                title = listOf(bokmalTittel),
                letterDataType = Unit::class,
                language = languages(Language.Bokmal),
                outline = listOf(
                    Element.OutlineContent.Title1(listOf(Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "Tittel")))),
                    Element.OutlineContent.Paragraph(listOf(Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "Dette er tekst som kun brukes i dette brevet."))))
                ).map { Content(it) },
                letterMetadata = testLetterMetadata
            ),
            doc
        )
    }

    @Test
    fun `TemplateContainerScope_formText adds Form$Text element`() {
        val prompt = newText(Language.Bokmal to "hei")
        val element = ParagraphOnlyScope<LangBokmal, SomeDto>().apply {
            formText(Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT, prompt)
        }.elements.first()

        val expected = Content(Element.OutlineContent.ParagraphContent.Form.Text(prompt, Element.OutlineContent.ParagraphContent.Form.Text.Size.SHORT))

        assertEquals(expected, element)
    }

    @Test
    fun `TemplateContainerScope_formChoice adds Form$MultipleChoice`() {
        val prompt = newText(Language.Bokmal to "hei")

        val element = ParagraphOnlyScope<LangBokmal, SomeDto>().apply {
            formChoice(prompt) {
                choice(Language.Bokmal to "velg denne")
            }
        }.elements.first()

        val expected = Content(Element.OutlineContent.ParagraphContent.Form.MultipleChoice(prompt, listOf(Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "velg denne"))))

        assertEquals(expected, element)
    }

    @Test
    fun `TemplateContainerScope_includePhrase adds phrase`() {
        val argument = Expression.Literal("jadda")
        val actual = OutlineOnlyScope<LangBokmal, SomeDto>().apply {
            includePhrase(TestFrase(argument))
        }.elements

        val expected = OutlineOnlyScope<LangBokmal, SomeDto>().apply { TestFrase(argument).apply(this) }.elements

        assertEquals(expected, actual)
    }
}

data class TestFrase(val test: Expression<String>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            textExpr(
                Language.Bokmal to "Hei på deg fra TestFrase: ".expr() + test,
                Language.Nynorsk to "Hei på deg frå TestFrase: ".expr() + test,
                Language.English to "Hey you, from TestFrase: ".expr() + test,
            )
        }
}