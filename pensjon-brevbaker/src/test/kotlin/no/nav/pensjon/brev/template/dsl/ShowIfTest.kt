package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.expression.*
import org.junit.jupiter.api.*

class ShowIfTest {

    @Test
    fun `createTemplate adds showIf`() {
        val expected = LetterTemplate(
            name = "test",
            title = nynorskTittel,
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
            outline = listOf(
                Element.Conditional(
                    predicate = Expression.FromScope(ExpressionScope<SomeDto, *>::argument).select(SomeDto::pensjonInnvilget),
                    showIf = listOf(newText(Language.Nynorsk to "jadda")),
                    showElse = listOf(newText(Language.Nynorsk to "neida"))
                )
            )
        )

        val actual = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            lang = languages(Language.Nynorsk),
            title = nynorskTittel,
            letterMetadata = testLetterMetadata,
        ) {
            outline {
                showIf(argument().select(SomeDto::pensjonInnvilget)) {
                    text(Language.Nynorsk to "jadda")
                } orShow {
                    text(Language.Nynorsk to "neida")
                }
            }
        }

        assertThat(expected, equalTo(actual))
    }

    @Test
    fun `orShowIf adds a conditional element as else`() {
        val exprScope = Expression.FromScope(ExpressionScope<SomeDto, *>::argument)
        val expected = LetterTemplate(
            name = "test",
            title = nynorskTittel,
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
            outline = listOf(
                Element.Conditional(
                    predicate = exprScope.select(SomeDto::pensjonInnvilget),
                    showIf = listOf(newText(Language.Nynorsk to "jadda")),
                    showElse = listOf(
                        Element.Conditional(
                            predicate = exprScope.select(SomeDto::name) equalTo "Test",
                            showIf = listOf(newText(Language.Nynorsk to "neidaJoda")),
                            showElse = emptyList()
                        )
                    )
                )
            )
        )

        val actual = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            lang = languages(Language.Nynorsk),
            title = nynorskTittel,
            letterMetadata = testLetterMetadata,
        ) {
            outline {
                showIf(argument().select(SomeDto::pensjonInnvilget)) {
                    text(Language.Nynorsk to "jadda")
                }.orShowIf(argument().select(SomeDto::name) equalTo "Test") {
                    text(Language.Nynorsk to "neidaJoda")
                }
            }
        }

        assertThat(expected, equalTo(actual))
    }

    @Test
    fun `final orShow nests as showOr in inner-most conditional element`() {
        val exprScope = Expression.FromScope(ExpressionScope<SomeDto, *>::argument)
        val expected = LetterTemplate(
            name = "test",
            title = nynorskTittel,
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
            outline = listOf(
                Element.Conditional(
                    predicate = exprScope.select(SomeDto::pensjonInnvilget),
                    showIf = listOf(newText(Language.Nynorsk to "jadda")),
                    showElse = listOf(
                        Element.Conditional(
                            predicate = exprScope.select(SomeDto::name) equalTo "Test",
                            showIf = listOf(newText(Language.Nynorsk to "neidaJoda")),
                            showElse = listOf(newText(Language.Nynorsk to "neida")),
                        )
                    )
                )
            )
        )

        val actual = createTemplate(
            name = "test",
            base = PensjonLatex,
            letterDataType = SomeDto::class,
            lang = languages(Language.Nynorsk),
            title = nynorskTittel,
            letterMetadata = testLetterMetadata,
        ) {
            outline {
                showIf(argument().select(SomeDto::pensjonInnvilget)) {
                    text(Language.Nynorsk to "jadda")
                }.orShowIf(argument().select(SomeDto::name) equalTo "Test") {
                    text(Language.Nynorsk to "neidaJoda")
                } orShow {
                    text(Language.Nynorsk to "neida")
                }
            }
        }

        assertThat(expected, equalTo(actual))
    }

}