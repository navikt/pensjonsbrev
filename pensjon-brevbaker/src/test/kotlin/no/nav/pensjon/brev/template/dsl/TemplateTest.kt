package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.expression.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias BokmalLang = LanguageSupport.Single<Language.Bokmal>

class TemplateTest {

    @Test
    fun `createTemplate can add outline with title1 using text-builder`() {
        val doc = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = Unit::class,
            languages = bokmalTittel.languages,
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
                base = PensjonLatex,
                letterDataType = Unit::class,
                language = languages(Language.Bokmal),
                letterMetadata = testLetterMetadata,
                outline = listOf(
                    Element.Title1(
                        listOf(
                            Element.Text.Literal.create(Language.Bokmal to "Heisann. "),
                        )
                    )
                )
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
        ){
            text(
                Language.Bokmal to "hei",
                Language.Nynorsk to "hei",
                Language.English to "Hello",
            )
        }

        val doc = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            languages = bokmalTittel.languages,
            letterMetadata = testLetterMetadata,
        ) {
            title.add(bokmalTittel)
            includeAttachment(attachment, Expression.Literal(Unit))
        }

        assertEquals(
            LetterTemplate(
                name = "test",
                title = listOf(bokmalTittel),
                base = PensjonLatex,
                letterDataType = SomeDto::class,
                language = languages(Language.Bokmal),
                letterMetadata = testLetterMetadata,
                outline = emptyList(),
                attachments = listOf(
                    IncludeAttachment(
                        Expression.Literal(Unit),
                        attachment
                    )
                )
            ),
            doc
        )
    }

    @Test
    fun `TemplateTextOnlyScope_eval adds Expression element`() {
        val element = TextOnlyScope<BokmalLang, SomeDto>().apply {
            eval(argument().select(SomeDto::name))
        }.children.first()

        val expected = Element.Text.Expression<BokmalLang>(
            Expression.FromScope(ExpressionScope<SomeDto, *>::argument).select(SomeDto::name)
        )

        assertEquals(expected, element)
    }

    @Test
    fun `createTemplate adds literal title`() {
        val doc = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = Unit::class,
            languages = bokmalTittel.languages,
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
                base = PensjonLatex,
                letterDataType = Unit::class,
                letterMetadata = testLetterMetadata,
                language = languages(Language.Bokmal),
                outline = listOf(Element.Title1(listOf(Element.Text.Literal.create(Language.Bokmal to "jadda"))))
            ), doc
        )
    }

    @Test
    fun `createTemplate adds outline`() {
        val doc = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = Unit::class,
            languages = bokmalTittel.languages,
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
                base = PensjonLatex,
                letterDataType = Unit::class,
                language = languages(Language.Bokmal),
                letterMetadata = testLetterMetadata,
                outline = listOf(
                    Element.Title1(listOf(Element.Text.Literal.create(Language.Bokmal to "Tittel"))),
                    Element.Paragraph(listOf(Element.Text.Literal.create(Language.Bokmal to "Dette er tekst som kun brukes i dette brevet.")))
                )
            ),
            doc
        )
    }

    @Test
    fun `TemplateContainerScope_formText adds Form$Text element`() {
        val prompt = newText(Language.Bokmal to "hei")
        val element = ParagraphScope<BokmalLang, SomeDto>().apply {
            formText(1, prompt)
        }.children.first()

        val expected = Element.Form.Text(prompt, 1)

        assertEquals(expected, element)
    }

    @Test
    fun `TemplateContainerScope_formChoice adds Form$MultipleChoice`() {
        val prompt = newText(Language.Bokmal to "hei")

        val element = ParagraphScope<BokmalLang, SomeDto>().apply {
            formChoice(prompt) {
                choice(Language.Bokmal to "velg denne")
            }
        }.children.first()

        val expected = Element.Form.MultipleChoice(prompt, listOf(newText(Language.Bokmal to "velg denne")))

        assertEquals(expected, element)
    }

    @Test
    fun `TemplateContainerScope_includePhrase adds phrase`() {
        val argument = Expression.Literal(TestFraseDto("jadda"))
        val actual = OutlineScope<BokmalLang, SomeDto>().apply {
            includePhrase(testFrase, argument)
        }.children

        val expected = OutlineScope<BokmalLang, SomeDto>().apply { testFrase.apply(this, argument) }.children

        assertEquals(expected, actual)
    }
}

data class TestFraseDto(val test: String)

val testFrase = OutlinePhrase<LangBokmalNynorskEnglish, TestFraseDto> {
    paragraph {
        val input = it.select(TestFraseDto::test)
        textExpr(
            Language.Bokmal to "Hei på deg fra TestFrase: ".expr() + input,
            Language.Nynorsk to "Hei på deg frå TestFrase: ".expr() + input,
            Language.English to "Hey you, from TestFrase: ".expr() + input,
        )
    }
}