package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.SomeDtoSelectors.name
import no.nav.pensjon.brev.template.dsl.SomeDtoSelectors.pensjonInnvilget
import no.nav.pensjon.brev.template.dsl.expression.*
import org.junit.jupiter.api.*

class ShowIfTest {

    @Test
    fun `createTemplate adds showIf`() {
        val expected = LetterTemplate(
            name = "test",
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                ContentOrControlStructure.Conditional(
                    predicate = Expression.FromScope(ExpressionScope<SomeDto, *>::argument).pensjonInnvilget,
                    showIf = listOf(newText(Language.Nynorsk to "jadda")),
                    showElse = listOf(newText(Language.Nynorsk to "neida"))
                )
            ),
            letterMetadata = testLetterMetadata
        )

        val actual = createTemplate(
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)

            outline {
                showIf(pensjonInnvilget) {
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
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                ContentOrControlStructure.Conditional(
                    predicate = exprScope.pensjonInnvilget,
                    showIf = listOf(newText(Language.Nynorsk to "jadda")),
                    showElse = listOf(
                        ContentOrControlStructure.Conditional(
                            predicate = exprScope.name equalTo "Test",
                            showIf = listOf(newText(Language.Nynorsk to "neidaJoda")),
                            showElse = emptyList()
                        )
                    )
                )
            ),
            letterMetadata = testLetterMetadata
        )

        val actual = createTemplate(
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)
            outline {
                showIf(pensjonInnvilget) {
                    text(Language.Nynorsk to "jadda")
                }.orShowIf(name equalTo "Test") {
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
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                ContentOrControlStructure.Conditional(
                    predicate = exprScope.pensjonInnvilget,
                    showIf = listOf(newText(Language.Nynorsk to "jadda")),
                    showElse = listOf(
                        ContentOrControlStructure.Conditional(
                            predicate = exprScope.name equalTo "Test",
                            showIf = listOf(newText(Language.Nynorsk to "neidaJoda")),
                            showElse = listOf(newText(Language.Nynorsk to "neida")),
                        )
                    )
                )
            ),
            letterMetadata = testLetterMetadata
        )

        val actual = createTemplate(
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)
            outline {
                showIf(pensjonInnvilget) {
                    text(Language.Nynorsk to "jadda")
                }.orShowIf(name equalTo "Test") {
                    text(Language.Nynorsk to "neidaJoda")
                } orShow {
                    text(Language.Nynorsk to "neida")
                }
            }
        }

        assertThat(expected, equalTo(actual))
    }

}