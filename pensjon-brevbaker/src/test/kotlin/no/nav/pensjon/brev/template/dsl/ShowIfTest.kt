package no.nav.pensjon.brev.template.dsl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import no.nav.pensjon.brev.Fixtures.felles
import no.nav.pensjon.brev.outlineTestTemplate
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Element.OutlineContent.Paragraph
import no.nav.pensjon.brev.template.dsl.SomeDtoSelectors.name
import no.nav.pensjon.brev.template.dsl.SomeDtoSelectors.pensjonInnvilget
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.hasBlocks
import org.junit.jupiter.api.Test

class ShowIfTest {
    @Test
    fun `createTemplate adds showIf`() {
        val expected = LetterTemplate(
            name = "test",
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                Content(
                    Paragraph(
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
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)

            outline {
                paragraph {
                    showIf(pensjonInnvilget) {
                        text(Language.Nynorsk to "jadda")
                    } orShow {
                        text(Language.Nynorsk to "neida")
                    }
                }
            }
        }

        assertThat(expected, equalTo(actual))
    }

    @Test
    fun `orShowIf adds a conditional element as else`() {
        val exprScope = Expression.FromScope.Argument<SomeDto>()
        val expected = LetterTemplate(
            name = "test",
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                Content(
                    Paragraph(
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
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)
            outline {
                paragraph {
                    showIf(pensjonInnvilget) {
                        text(Language.Nynorsk to "jadda")
                    }.orShowIf(name equalTo "Test") {
                        text(Language.Nynorsk to "neidaJoda")
                    }
                }
            }
        }

        assertThat(expected, equalTo(actual))
    }

    @Test
    fun `final orShow nests as showOr in inner-most conditional element`() {
        val exprScope = Expression.FromScope.Argument<SomeDto>()
        val expected = LetterTemplate(
            name = "test",
            title = listOf(nynorskTittel),
            letterDataType = SomeDto::class,
            language = languages(Language.Nynorsk),
            outline = listOf(
                Content(
                    Paragraph(
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
            name = "test",
            letterDataType = SomeDto::class,
            languages = languages(Language.Nynorsk),
            letterMetadata = testLetterMetadata,
        ) {
            title.add(nynorskTittel)
            outline {
                paragraph {
                    showIf(pensjonInnvilget) {
                        text(Language.Nynorsk to "jadda")
                    }.orShowIf(name equalTo "Test") {
                        text(Language.Nynorsk to "neidaJoda")
                    } orShow {
                        text(Language.Nynorsk to "neida")
                    }
                }
            }
        }

        assertThat(expected, equalTo(actual))
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

    @Test
    fun `showIf renders when condition evaluates to true`() {
        assertThat(
            Letter2Markup.render(Letter(showIfTemplate, SomeDto("showIf", false), Language.Bokmal, felles)).letterMarkup,
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
            Letter2Markup.render(Letter(showIfTemplate, SomeDto("orShowIf", false), Language.Bokmal, felles)).letterMarkup,
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
            Letter2Markup.render(Letter(showIfTemplate, SomeDto("orShow", false), Language.Bokmal, felles)).letterMarkup,
            hasBlocks {
                paragraph {
                    literal("orShow tekst")
                }
            }
        )
    }
}