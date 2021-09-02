package no.nav.pensjon.brev.template

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.maler.Alderspensjon
import no.nav.pensjon.brev.something.Fagdelen
import no.nav.pensjon.brev.something.PensjonLatex
import no.nav.pensjon.brev.template.dsl.argument
import no.nav.pensjon.brev.template.dsl.str
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class DSLTest {

    object Fraser {
        val pensjonInnvilget =
            Phrase.Static.create("pensjonInnvilget", Language.Bokmal to "Du har fått innvilget pensjon")
    }

    val bokmalTittel = title(Language.Bokmal to "test brev")
    val nynorskTittel = title(Language.Nynorsk to "test brev")


    @Test
    fun `createTemplate can add title1 using text-builder`() {
        val doc = createTemplate(
            name = "test",
            title = bokmalTittel,
            base = PensjonLatex,
            lang = languages(Language.Bokmal)
        ) {
            outline {
                title1 {
                    text(Language.Bokmal to "Heisann. ")
                    phrase(Fraser.pensjonInnvilget)
                }
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                bokmalTittel,
                PensjonLatex,
                emptySet(),
                languages(Language.Bokmal),
                listOf(
                    Element.Title1(
                        listOf(
                            Element.Text.Literal.create(Language.Bokmal to "Heisann. "),
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
        val doc = createTemplate("test", bokmalTittel, PensjonLatex, languages(Language.Bokmal)) {
            outline {
                title1 { phrase(Fraser.pensjonInnvilget) }
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                bokmalTittel,
                PensjonLatex,
                emptySet(),
                languages(Language.Bokmal),
                listOf(Element.Title1(listOf(Element.Text.Phrase(Fraser.pensjonInnvilget))))
            ),
            doc
        )
    }

    @Test
    fun `createTemplate adds literal title`() {
        val doc = createTemplate("test", bokmalTittel, PensjonLatex, languages(Language.Bokmal)) {
            outline {
                title1 { text(Language.Bokmal to "jadda") }
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                bokmalTittel,
                PensjonLatex,
                emptySet(),
                languages(Language.Bokmal),
                listOf(Element.Title1(listOf(Element.Text.Literal.create(Language.Bokmal to "jadda"))))
            ), doc
        )
    }

    @Test
    fun `createTemplate adds parameters`() {
        val doc = createTemplate("test", nynorskTittel, PensjonLatex, languages(Language.Nynorsk)) {
            parameters {
                required { SaksNr }
                required { PensjonInnvilget }
                optional { KortNavn }
            }
        }

        assertEquals(
            LetterTemplate(
                "test",
                nynorskTittel,
                PensjonLatex,
                setOf(
                    RequiredParameter(SaksNr),
                    RequiredParameter(PensjonInnvilget),
                    OptionalParameter(KortNavn)
                ),
                languages(Language.Nynorsk),
                emptyList()
            ),
            doc
        )
    }

    @Test
    fun `createTemplate adds outline`() {
        val doc = createTemplate("test", bokmalTittel, PensjonLatex, languages(Language.Bokmal)) {
            outline {
                title1 { phrase(Fraser.pensjonInnvilget) }
                paragraph {
                    text(Language.Bokmal to "Dette er tekst som kun brukes i dette brevet.")
                }
            }
        }

        assertEquals(
            LetterTemplate(
                "test", bokmalTittel, PensjonLatex, emptySet(), languages(Language.Bokmal), listOf(
                    Element.Title1(listOf(Element.Text.Phrase(Fraser.pensjonInnvilget))),
                    Element.Paragraph(listOf(Element.Text.Literal.create(Language.Bokmal to "Dette er tekst som kun brukes i dette brevet.")))
                )
            ),
            doc
        )
    }

    @Test
    fun `createTemplate adds showIf`() {
        val doc = createTemplate("test", nynorskTittel, PensjonLatex, languages(Language.Nynorsk)) {
            parameters { required { PensjonInnvilget } }
            outline {
                showIf(argument(PensjonInnvilget)) {
                    text(Language.Nynorsk to "jadda")
                } orShow {
                    text(Language.Nynorsk to "neida")
                }
            }
        }

        assertEquals(
            LetterTemplate(
                "test", nynorskTittel, PensjonLatex, setOf(RequiredParameter(PensjonInnvilget)), languages(Language.Nynorsk), listOf(
                    Element.Conditional(
                        Expression.Argument(PensjonInnvilget),
                        listOf(Element.Text.Literal.create(Language.Nynorsk to "jadda")),
                        listOf(Element.Text.Literal.create(Language.Nynorsk to "neida"))
                    )
                )
            ),
            doc
        )
    }

    @Test
    @Disabled
    fun `LetterTemplate json roundtrip`() {

        val doc = createTemplate("test", bokmalTittel, PensjonLatex, languages(Language.Bokmal)) {
            parameters {
                required { SaksNr }
                optional { PensjonInnvilget }
            }

            outline {
                title1 { phrase(Fraser.pensjonInnvilget) }

                title1 { text(Language.Bokmal to "literal title") }
                paragraph {
                    text(Language.Bokmal to "kun brukes brevet")
                    eval(argument(SaksNr).str())
                }

                showIf(argument(PensjonInnvilget)) {
                    text(Language.Bokmal to "joda")
                } orShow {
                    text(Language.Bokmal to "neida")
                }
            }
        }

        val jsonRoundtrip =
            jacksonObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(doc)
                .let {
                    println(it)
                    jacksonObjectMapper().readValue<LetterTemplate<*>>(it)
                }

        assertEquals(doc, jsonRoundtrip)
    }

    @Test
    fun `createTemplate will fail if using undeclared parameters`() {
        assertThrows<IllegalArgumentException> {
            createTemplate("test", bokmalTittel, PensjonLatex, languages(Language.Bokmal)) {
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
        val template = createTemplate("test", bokmalTittel, PensjonLatex, languages(Language.Bokmal)) {
            parameters { required { SaksNr } }
            outline {
                eval(argument(SaksNr).str())
            }
        }

        assertEquals(
            LetterTemplate(
                "test", bokmalTittel, PensjonLatex, setOf(RequiredParameter(SaksNr)), languages(Language.Bokmal), listOf(
                    Element.Text.Expression(
                        Expression.UnaryInvoke(
                            Expression.Argument(SaksNr),
                            UnaryOperation.ToString()
                        )
                    )
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


    @Test
    fun test() {
        val templateArgs: Map<Parameter, Any> =
            mapOf(
                ReturAdresse to Fagdelen.ReturAdresse("En NAV enhet", "En adresse 1", "1337", "Et poststed"),
                SaksNr to 1234,
                PensjonInnvilget to true,
                Mottaker to
                        Fagdelen.Mottaker(
                            "FornavnMottaker",
                            "EtternavnMottaker",
                            "GatenavnMottaker",
                            "21 A",
                            "0123",
                            "PoststedMottaker"
                        ),
                NorskIdentifikator to 13374212345,
            )
        val rendered = Letter(Alderspensjon.template, templateArgs, Language.Bokmal).render()
        println("hei")
    }

}
