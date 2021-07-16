package no.nav.pensjon.brev.template

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.something.PensjonLatex
import org.junit.Ignore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class DSLTest {

    object Fraser {
        val pensjonInnvilget = Phrase.Static("pensjonInnvilget", "Du har fått innvilget pensjon")
    }

    @Test
    fun `createTemplate can add title1 using text-builder`() {
        val doc = createTemplate("test", PensjonLatex) {
            outline {
                title1 {
                    text("Heisann. ")
                    phrase(Fraser.pensjonInnvilget)
                }
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                PensjonLatex,
                emptySet(),
                listOf(
                    Element.Title1(
                        listOf(
                            Element.Text.Literal("Heisann. "),
                            Element.Text.Phrase(Fraser.pensjonInnvilget)
                        )
                    )
                )
            ),
            doc
        )
    }

    @Test
    fun `createTemplate adds phrase title`() {
        val doc = createTemplate("test", PensjonLatex) {
            outline {
                title1(Fraser.pensjonInnvilget)
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                PensjonLatex,
                emptySet(),
                listOf(Element.Title1(listOf(Element.Text.Phrase(Fraser.pensjonInnvilget))))
            ),
            doc
        )
    }

    @Test
    fun `createTemplate adds literal title`() {
        val doc = createTemplate("test", PensjonLatex) {
            outline {
                title1("jadda")
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                PensjonLatex,
                emptySet(),
                listOf(Element.Title1(listOf(Element.Text.Literal("jadda"))))
            ), doc
        )
    }

    @Test
    fun `createTemplate adds parameters`() {
        val doc = createTemplate("test", PensjonLatex) {
            parameters {
                required { SaksNr }
                required { PensjonInnvilget }
                optional { KortNavn }
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                PensjonLatex,
                setOf(
                    RequiredParameter(SaksNr),
                    RequiredParameter(PensjonInnvilget),
                    OptionalParameter(KortNavn)
                ),
                emptyList()
            ),
            doc
        )
    }

    @Test
    fun `createTemplate adds outline`() {
        val doc = createTemplate("test", PensjonLatex) {
            outline {
                title1(Fraser.pensjonInnvilget)
                section {
                    text("Dette er tekst som kun brukes i dette brevet.")
                }
            }
        }

        assertEquals(
            LetterTemplate(
                "test", PensjonLatex, emptySet(), listOf(
                    Element.Title1(listOf(Element.Text.Phrase(Fraser.pensjonInnvilget))),
                    Element.Section(listOf(Element.Text.Literal("Dette er tekst som kun brukes i dette brevet.")))
                )
            ),
            doc
        )
    }

    @Test
    fun `createTemplate adds showIf`() {
        val doc = createTemplate("test", PensjonLatex) {
            parameters { required { PensjonInnvilget } }
            outline {
                showIf(argument(PensjonInnvilget)) {
                    text("jadda")
                } orShow {
                    text("neida")
                }
            }
        }

        assertEquals(
            LetterTemplate(
                "test", PensjonLatex, setOf(RequiredParameter(PensjonInnvilget)), listOf(
                    Element.Conditional(
                        Expression.Argument(PensjonInnvilget),
                        listOf(Element.Text.Literal("jadda")),
                        listOf(Element.Text.Literal("neida"))
                    )
                )
            ),
            doc
        )
    }

    @Test
    @Disabled
    fun `LetterTemplate json roundtrip`() {

        val doc = createTemplate("test", PensjonLatex) {
            parameters {
                required { SaksNr }
                optional { PensjonInnvilget }
            }

            outline {
                title1(Fraser.pensjonInnvilget)

                section {
                    title1("literal title")
                    text("kun brukes brevet")
                    eval(argument(SaksNr).str())
                }

                showIf(argument(PensjonInnvilget)) {
                    text("joda")
                } orShow {
                    text("neida")
                }
            }
        }

        val jsonRoundtrip =
            jacksonObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(doc)
                .let {
                    println(it)
                    jacksonObjectMapper().readValue<LetterTemplate>(it)
                }

        assertEquals(doc, jsonRoundtrip)
    }

    @Test
    fun `createTemplate will fail if using undeclared parameters`() {
        assertThrows<IllegalArgumentException> {
            val template = createTemplate("test", PensjonLatex) {
                outline {
                    title1 {
                        eval(argument(KortNavn))
                    }
                }
            }
        }
    }

    @Test
    fun `createTemplate can add Text$Expression elements`() {
        val template = createTemplate("test", PensjonLatex) {
            parameters { required { SaksNr } }
            outline {
                eval(argument(SaksNr).str())
            }
        }

        assertEquals(
            LetterTemplate(
                "test", PensjonLatex, setOf(RequiredParameter(SaksNr)), listOf(
                    Element.Text.Expression(Expression.UnaryInvoke(Expression.Argument(SaksNr), UnaryOperation.ToString()))
                )
            ),
            template
        )

    }
//
//    @Test
//    fun `json deserialization perform type checks`() {
//        val json = """
//            {
//              "name" : "test",
//              "base" : {
//                "name" : "no.nav.pensjon.brev.latex.model.PensjonLatex"
//              },
//              "parameters" : [ {
//                "required" : {
//                  "name" : "no.nav.pensjon.brev.latex.model.SaksNr",
//                  "schema" : "TODO: add schema description"
//                }
//              } ],
//              "outline" : [ {
//                "parameter" : {
//                  "name" : "no.nav.pensjon.brev.latex.model.SaksNr",
//                  "schema" : "TODO: add schema description"
//                },
//                "transformer" : {
//                  "name" : "no.nav.pensjon.brev.latex.model.ArgumentTransformer${"\$"}ToString${"\$"}Number"
//                },
//                "schema" : "Element${"\$"}Text${"\$"}TransformedArgument"
//              } ]
//            }
//        """.trimIndent()
//
//        assertThrows<Exception> {
//            jacksonObjectMapper().readValue<LetterTemplate>(json)
//        }
//    }

}
