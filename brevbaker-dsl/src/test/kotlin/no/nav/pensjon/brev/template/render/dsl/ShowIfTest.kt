package no.nav.pensjon.brev.template.render.dsl

import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.newText
import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.dsl.SomeDtoSelectors.name
import no.nav.pensjon.brev.template.render.dsl.SomeDtoSelectors.pensjonInnvilget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ShowIfTest {
    @Test
    fun `createTemplate adds showIf`() {
        val expected = LetterTemplate(
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                ContentOrControlStructure.Content(
                    Element.OutlineContent.Paragraph(
                        listOf(
                            ContentOrControlStructure.Conditional(
                                predicate = Expression.FromScope.Argument<SomeDto>().pensjonInnvilget,
                                showIf = listOf(newText(Language.Nynorsk to "jadda")),
                                showElse = listOf(newText(Language.Nynorsk to "neida"))
                            )
                        )
                    )
                )
            ),
            letterMetadata = testLetterMetadata
        )

        val actual = createTemplate(
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)

            outline {
                paragraph {
                    showIf(pensjonInnvilget) {
                        text(nynorsk { +"jadda" })
                    } orShow {
                        text(nynorsk { +"neida" })
                    }
                }
            }
        }

        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `orShowIf adds a conditional element as else`() {
        val exprScope = Expression.FromScope.Argument<SomeDto>()
        val expected = LetterTemplate(
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                ContentOrControlStructure.Content(
                    Element.OutlineContent.Paragraph(
                        listOf(
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
                        )
                    )
                )
            ),
            letterMetadata = testLetterMetadata
        )

        val actual = createTemplate(
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)
            outline {
                paragraph {
                    showIf(pensjonInnvilget) {
                        text(nynorsk { +"jadda" })
                    }.orShowIf(name equalTo "Test") {
                        text(nynorsk { +"neidaJoda" })
                    }
                }
            }
        }

        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `final orShow nests as showOr in inner-most conditional element`() {
        val exprScope = Expression.FromScope.Argument<SomeDto>()
        val expected = LetterTemplate(
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                ContentOrControlStructure.Content(
                    Element.OutlineContent.Paragraph(
                        listOf(
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
                        )
                    )
                )
            ),
            letterMetadata = testLetterMetadata
        )

        val actual = createTemplate(
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)
            outline {
                paragraph {
                    showIf(pensjonInnvilget) {
                        text(nynorsk { +"jadda" })
                    }.orShowIf(name equalTo "Test") {
                        text(nynorsk { +"neidaJoda" })
                    } orShow {
                        text(nynorsk { +"neida" })
                    }
                }
            }
        }

        assertThat(expected).isEqualTo(actual)
    }
}