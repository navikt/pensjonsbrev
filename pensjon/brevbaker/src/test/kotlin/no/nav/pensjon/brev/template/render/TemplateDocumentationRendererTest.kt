package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.outlineTestTemplate
import no.nav.pensjon.brev.pensjonOgUfoereProductionTemplates
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.routing.modelSpecification
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.TemplateDocumentation.ContentOrControlStructure.Conditional
import no.nav.pensjon.brev.template.render.TemplateDocumentation.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Element.OutlineContent.Paragraph
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Element.OutlineContent.Title1
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Element.ParagraphContent.Text
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Expr
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateDocumentationRendererTest {

    @Test
    fun `gir hvert synlige blokk et stabilt soekeindeks paa tvers av overskrifter og avsnitt`() {
        val templ = outlineTestTemplate<Unit> {
            title1 { text(bokmal { +"overskrift" }) }
            paragraph { text(bokmal { +"første avsnitt" }) }
            paragraph { text(bokmal { +"andre avsnitt" }) }
        }

        val documentation = TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification())
        val indexes = documentation.outline.mapNotNull { cocs ->
            (cocs as? Content<*>)?.content?.let { content ->
                when (content) {
                    is Title1 -> content.index
                    is Paragraph -> content.index
                    else -> null
                }
            }
        }

        assertEquals(listOf(0, 1, 2), indexes)
        val lines = TemplateTextExtractor.extract(documentation)
        assertEquals(listOf(0, 1, 2), lines.map { it.index })
        assertEquals(
            listOf("overskrift", "første avsnitt", "andre avsnitt"),
            lines.map { (it.segments.single() as TextSegment.Text).value },
        )
    }

    @Test
    fun canRenderAndExtractDocumentationForAllTemplates() {
        (pensjonOgUfoereProductionTemplates.hentAutobrevmaler() + pensjonOgUfoereProductionTemplates.hentRedigerbareMaler()).forEach {
            val documentation = TemplateDocumentationRenderer.render(
                it.template,
                it.template.language.all().first(),
                it.template.modelSpecification()
            )
            TemplateTextExtractor.extract(documentation)
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
        val conditional = TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first()
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.AssociativeOp(
                                TemplateDocumentation.AssocOp.AND,
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
        val conditional = TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first()
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.Comparison(Expr.Literal("1", TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER), TemplateDocumentation.CompareOp.NOT_EQUAL, Expr.Literal("2", TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER)),
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
        val conditional = TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first()
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
        val conditional = TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first()
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.Comparison(
                                left = Expr.FieldPath(TemplateDocumentation.DataSource.Scope("argument"), emptyList(), leafType = null),
                                op = TemplateDocumentation.CompareOp.GREATER_THAN,
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
        val text = ((TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first() as Content<Paragraph>)
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
        val text = ((TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first() as Content<Paragraph>)
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
        val text = ((TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first() as Content<Paragraph>)
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
        val conditional = TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first()

        val listeFieldPath = Expr.FieldPath(
            TemplateDocumentation.DataSource.Scope("argument"),
            listOf("liste"),
            leafType = "List<Vilkar>",
            leafOwnerType = "VilkarListeArg",
        )
        val getOrNullCall = Expr.FunctionCall(
            "getOrNull",
            listOf(listeFieldPath, Expr.Literal("0", TemplateModelSpecification.FieldType.Scalar.Kind.NUMBER)),
        )
        val fieldAccess = Expr.FieldPath(
            TemplateDocumentation.DataSource.Computed(getOrNullCall),
            listOf("unguforresultat"),
            leafType = "String",
            leafOwnerType = "Vilkar",
        )
        assertEquals(
            Content(
                Paragraph(
                    listOf(
                        Conditional(
                            predicate = Expr.Comparison(
                                left = Expr.NullCoalesce(fieldAccess, Expr.Literal("", TemplateModelSpecification.FieldType.Scalar.Kind.STRING)),
                                op = TemplateDocumentation.CompareOp.NOT_EQUAL,
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

    private data class PersonArg(val fodselsnummer: String?, val antall: Int?)

    @Test
    fun `FieldPath faar eksempeltekst for kjente tekstfelt, men ikke for andre typer`() {
        fun selector(name: String, type: String) = object : TemplateModelSelector<PersonArg, Any?> {
            override val className = "PersonArg"
            override val propertyName = name
            override val propertyType = type
            override val selector: PersonArg.() -> Any? = { null }
        }

        fun fieldPathFor(name: String, type: String): Expr.FieldPath {
            @Suppress("UNCHECKED_CAST")
            val sel = selector(name, type) as TemplateModelSelector<PersonArg, String?>
            val templ = outlineTestTemplate<PersonArg> {
                paragraph { text(bokmal { +argument.select(sel).ifNull("") }) }
            }
            @Suppress("UNCHECKED_CAST")
            val text = ((TemplateDocumentationRenderer.render(templ, Bokmal, templ.modelSpecification()).outline.first() as Content<Paragraph>)
                .content.paragraph.first() as Content<Text>).content
            return ((text as Text.Expression).expression as Expr.NullCoalesce).value as Expr.FieldPath
        }

        assertEquals("12345678901", fieldPathFor("fodselsnummer", "kotlin.String?").exampleText)
        assertEquals(null, fieldPathFor("fodselsnummer", "kotlin.Int?").exampleText)
        assertEquals(null, fieldPathFor("navn", "kotlin.String?").exampleText)
    }
}
