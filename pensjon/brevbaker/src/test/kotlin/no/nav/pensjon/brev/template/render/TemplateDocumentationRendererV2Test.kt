package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.pensjonOgUfoereProductionTemplates
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.ContentOrControlStructure.Conditional
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.Element.OutlineContent.Paragraph
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.Element.ParagraphContent.Text
import no.nav.pensjon.brev.template.render.TemplateDocumentationV2.Expr
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateDocumentationRendererV2Test {

    @Test
    fun canRenderDocumentationForAllTemplates() {
        (pensjonOgUfoereProductionTemplates.hentAutobrevmaler() + pensjonOgUfoereProductionTemplates.hentRedigerbareMaler()).forEach {
            TemplateDocumentationRendererV2.render(
                it.template,
                it.template.language.all().first(),
                it.template.modelSpecification()
            )
        }
    }

    @Test
    fun `and-kjede flates ut til en AssociativeOp i stedet for noestede binaerinvokes`() {
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                showIf(true.expr().and(false.expr()).and(true.expr())) {
                    text(bokmal { +"innhold" })
                }
            }
        }
        val conditional = TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first()
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.AssociativeOp(
                                TemplateDocumentationV2.AssocOp.AND,
                                listOf(Expr.Literal("true", TemplateModelSpecification.FieldType.Scalar.Kind.BOOLEAN), Expr.Literal("false", TemplateModelSpecification.FieldType.Scalar.Kind.BOOLEAN), Expr.Literal("true", TemplateModelSpecification.FieldType.Scalar.Kind.BOOLEAN))
                            ),
                            showIf = listOf(Content(Text.Literal("innhold"))),
                            elseIf = emptyList(),
                            showElse = emptyList(),
                        )
                    )
                )
            ),
            conditional,
        )
    }

    @Test
    fun `not-equal blir en eksplisitt Comparison, ikke Not(Equal)`() {
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                showIf(1.expr().equalTo(2.expr()).not()) {
                    text(bokmal { +"ulik" })
                }
            }
        }
        val conditional = TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first()
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.Comparison(Expr.Literal("1", TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER), TemplateDocumentationV2.CompareOp.NOT_EQUAL, Expr.Literal("2", TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER)),
                            showIf = listOf(Content(Text.Literal("ulik"))),
                            elseIf = emptyList(),
                            showElse = emptyList(),
                        )
                    )
                )
            ),
            conditional,
        )
    }

    @Test
    fun `IfNull vises alltid som eksplisitt NullCoalesce, ogsaa for boolean fallback false`() {
        val nullableFalse: Boolean? = null
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                showIf(nullableFalse.expr().ifNull(false.expr())) {
                    text(bokmal { +"vises" })
                }
            }
        }
        val conditional = TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first()
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.NullCoalesce(Expr.Literal("null", null), Expr.Literal("false", TemplateModelSpecification.FieldType.Scalar.Kind.BOOLEAN)),
                            showIf = listOf(Content(Text.Literal("vises"))),
                            elseIf = emptyList(),
                            showElse = emptyList(),
                        )
                    )
                )
            ),
            conditional,
        )
    }

    /**
     * `value class`-typer (Kroner, Year, Percent, Postnummer, ...) er for oss bare en semantisk
     * innpakning rundt én verdi. `.value`-utpakkingen skal derfor IKKE legge til et eget
     * FieldPath-segment — det gir ingen ekstra menneskelig informasjon, kun støy
     * (`argument.value` i stedet for f.eks. bare `argument`).
     */
    @Test
    fun `utpakking av value class (Kroner-value) legger ikke til et eget FieldPath-segment`() {
        val templ = outlineTestTemplate<no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner> {
            paragraph {
                showIf(argument.value.greaterThan(0)) {
                    text(bokmal { +"har beløp" })
                }
            }
        }
        val conditional = TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first()
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.Comparison(
                                left = Expr.FieldPath(TemplateDocumentationV2.DataSource.Scope("argument"), emptyList(), leafType = null),
                                op = TemplateDocumentationV2.CompareOp.GREATER_THAN,
                                right = Expr.Literal("0", TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER),
                            ),
                            showIf = listOf(Content(Text.Literal("har beløp"))),
                            elseIf = emptyList(),
                            showElse = emptyList(),
                        )
                    )
                )
            ),
            conditional,
        )
    }

    @Test
    fun `tekstkonkatenering flates ut og merger nabo-literals`() {
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                text(bokmal { +("hei ".expr() + "på" + " deg") })
            }
        }
        @Suppress("UNCHECKED_CAST")
        val text = ((TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first() as Content<Paragraph>)
            .content.paragraph.first() as Content<Text>).content
        assertEquals(Text.Literal("hei på deg"), text)
    }

    private data class Vilkar(val unguforresultat: String?)
    private data class VilkarListeArg(val liste: List<Vilkar>)

    /**
     * `Format`-noder (LocalizedFormatter, f.eks. dato-/kroneformattering) skal ha et
     * `exampleText` produsert ved å faktisk kalle den ekte formatterings-logikken
     * (`LocalizedFormatter.apply`) med en syntetisk eksempelverdi og riktig språk — se
     * designnotatet. Dette gir et 100% korrekt eksempel uten å reimplementere
     * formatteringslogikk i frontend.
     */
    @Test
    fun `Format-node faar et korrekt formattert eksempel via LocalizedFormatter-apply`() {
        val templ = outlineTestTemplate<no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner> {
            paragraph {
                text(bokmal { +argument.format(no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormatKroner()) })
            }
        }
        @Suppress("UNCHECKED_CAST")
        val text = ((TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first() as Content<Paragraph>)
            .content.paragraph.first() as Content<Text>).content
        val format = (text as Text.Expression).expression as Expr.Format
        assertEquals("CurrencyFormatKroner", format.formatterName)
        assertEquals("12\u00A0345 kroner", format.exampleText)
    }

    /**
     * For en ukjent/uimplementert formatterer (som ikke er dekket i `formatterExampleText`)
     * skal `exampleText` være `null` i stedet for å kaste eller gjette feil verdi — frontend
     * faller da tilbake til dagens fraseviisning.
     */
    @Test
    fun `Format-node faar exampleText = null for ukjent formatterer`() {
        val ukjentFormatterer = object : no.nav.pensjon.brev.template.LocalizedFormatter<String>() {
            override fun stableHashCode(): Int = "UkjentFormatterer".hashCode()
            override fun apply(first: String, second: no.nav.pensjon.brev.template.Language): String = first
        }
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                text(bokmal { +"verdi".expr().format(ukjentFormatterer) })
            }
        }
        @Suppress("UNCHECKED_CAST")
        val text = ((TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first() as Content<Paragraph>)
            .content.paragraph.first() as Content<Text>).content
        val format = (text as Text.Expression).expression as Expr.Format
        assertEquals(null, format.exampleText)
    }

    /**
     * Reproduserer det virkelige, legacy-mønsteret fra
     * `LegacySelectors.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat`:
     * `liste.getOrNull().safe { vilkar }.safe { unguforresultat }.ifNull("")`. `.select(...)`
     * på resultatet av `getOrNull()` er akkurat tilfellet som tidligere ga en bakvendt
     * `FunctionCall(".unguforresultat", [...])`-representasjon (se commit-melding/PR).
     */
    @Test
    fun `feltaksess paa et beregnet uttrykk (getOrNull) blir en lesbar FieldPath med Computed-base`() {
        val listeSelector = object : TemplateModelSelector<VilkarListeArg, List<Vilkar>> {
            override val className = "VilkarListeArg"
            override val propertyName = "liste"
            override val propertyType = "List<Vilkar>"
            override val selector: VilkarListeArg.() -> List<Vilkar> = { liste }
        }
        val vilkarSelector = object : TemplateModelSelector<Vilkar, String?> {
            override val className = "Vilkar"
            override val propertyName = "unguforresultat"
            override val propertyType = "String"
            override val selector: Vilkar.() -> String? = { unguforresultat }
        }

        val templ = outlineTestTemplate<VilkarListeArg> {
            paragraph {
                showIf(argument.select(listeSelector).getOrNull().select(vilkarSelector).ifNull("").notEqualTo("oppfylt")) {
                    text(bokmal { +"vises" })
                }
            }
        }
        val conditional = TemplateDocumentationRendererV2.render(templ, Bokmal, templ.modelSpecification()).outline.first()

        val listeFieldPath = Expr.FieldPath(TemplateDocumentationV2.DataSource.Scope("argument"), listOf("liste"), leafType = null)
        val getOrNullCall = Expr.FunctionCall(
            "getOrNull",
            listOf(listeFieldPath, Expr.Literal("0", TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER)),
        )
        val fieldAccess = Expr.FieldPath(TemplateDocumentationV2.DataSource.Computed(getOrNullCall), listOf("unguforresultat"), leafType = null)
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.Comparison(
                                left = Expr.NullCoalesce(fieldAccess, Expr.Literal("", TemplateModelSpecification.FieldType.Scalar.Kind.STRING)),
                                op = TemplateDocumentationV2.CompareOp.NOT_EQUAL,
                                right = Expr.Literal("oppfylt", TemplateModelSpecification.FieldType.Scalar.Kind.STRING),
                            ),
                            showIf = listOf(Content(Text.Literal("vises"))),
                            elseIf = emptyList(),
                            showElse = emptyList(),
                        )
                    )
                )
            ),
            conditional,
        )
    }
}
