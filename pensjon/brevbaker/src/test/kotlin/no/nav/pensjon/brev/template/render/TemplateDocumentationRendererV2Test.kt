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
}
