package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.maler.ProductionTemplates
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.outlineTestTemplate
import no.nav.pensjon.brev.template.render.TemplateDocumentation.*
import no.nav.pensjon.brev.template.render.TemplateDocumentation.ContentOrControlStructure.Conditional
import no.nav.pensjon.brev.template.render.TemplateDocumentation.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Element.OutlineContent.Paragraph
import no.nav.pensjon.brev.template.render.TemplateDocumentation.Element.ParagraphContent.Text
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer.flattenLiteralConcat
import no.nav.pensjon.brev.template.render.TemplateDocumentationRenderer.mergeLiterals
import no.nav.pensjon.brevbaker.api.model.Kroner
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateDocumentationRendererTest {

    @Test
    fun canRenderDocumentationForAllTemplates() {
        ProductionTemplates.hentAutobrevmaler().forEach {
            TemplateDocumentationRenderer.render(it.template, it.template.language.all().first())
        }
    }

    @Test
    fun flattenAndMergeTextExpr() {
        val fourPlusThree = Kroner(4).expr().plus(Kroner(3).expr()).format()
        val expr = "hei hvordan ".expr() + "går det? 4 + 3 = " + fourPlusThree + " QED"

        val flattened = expr.flattenLiteralConcat()
        assertEquals(listOf("hei hvordan ".expr(), "går det? 4 + 3 = ".expr(), fourPlusThree, " QED".expr()), flattened)
        assertEquals(listOf("hei hvordan går det? 4 + 3 = ".expr(), fourPlusThree, " QED".expr()), flattened.mergeLiterals())
    }

    @Test
    fun `if-elseif-else chain loeftes opp slik at de er paa samme nivaa`() {
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                showIf(true.expr()) {
                    text(Bokmal to "første")
                }.orShowIf(false.expr()) {
                    text(Bokmal to "andre")
                }.orShowIf(1.expr().greaterThan(2.expr())) {
                    text(Bokmal to "tredje")
                } orShow {
                    text(Bokmal to "else")
                }
            }
        }
        val expected = Content(
            Paragraph(
                listOf(
                    Conditional(
                        predicate = Expression.Literal("true"),
                        showIf = listOf(Content(Text.Literal("første"))),
                        elseIf = listOf(
                            Conditional.ElseIf(
                                predicate = Expression.Literal("false"),
                                showIf = listOf(Content(Text.Literal("andre"))),
                            ),
                            Conditional.ElseIf(
                                predicate = Expression.Invoke(
                                    Expression.Invoke.Operation(">", BinaryOperation.Documentation.Notation.INFIX),
                                    Expression.Literal("1"),
                                    Expression.Literal("2"),
                                    type = "TODO"
                                ),
                                showIf = listOf(Content(Text.Literal("tredje"))),
                            ),
                        ),
                        showElse = listOf(Content(Text.Literal("else"))),
                    )
                )
            )
        )
        assertEquals(expected, TemplateDocumentationRenderer.render(templ, Bokmal).outline.first())
    }

    @Test
    fun `if-elseif-else chain loeftes kun opp om det er ett og bare ett element som er en conditional`() {
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                showIf(true.expr()) {
                    text(Bokmal to "første")
                } orShow {
                    showIf(false.expr()) {
                        text(Bokmal to "andre")
                    }.orShow {
                        text(Bokmal to "ekstra")
                        showIf(1.expr().greaterThan(2.expr())) {
                            text(Bokmal to "tredje")
                        } orShow {
                            text(Bokmal to "else")
                        }
                    }
                }
            }
        }
        val expected = Content(
            Paragraph(
                listOf(
                    Conditional(
                        predicate = Expression.Literal("true"),
                        showIf = listOf(Content(Text.Literal("første"))),
                        elseIf = listOf(
                            Conditional.ElseIf(
                                predicate = Expression.Literal("false"),
                                showIf = listOf(Content(Text.Literal("andre"))),
                            ),
                        ),
                        showElse = listOf(
                            Content(Text.Literal("ekstra")),
                            Conditional(
                                predicate = Expression.Invoke(
                                    Expression.Invoke.Operation(">", BinaryOperation.Documentation.Notation.INFIX),
                                    Expression.Literal("1"),
                                    Expression.Literal("2"),
                                    type = "TODO"
                                ),
                                showIf = listOf(Content(Text.Literal("tredje"))),
                                elseIf = emptyList(),
                                showElse = listOf(Content(Text.Literal("else"))),
                            ),
                        )
                    )
                )
            )
        )
        assertEquals(expected, TemplateDocumentationRenderer.render(templ, Bokmal).outline.first())
    }

    @Test
    fun `collection isEmpty expr blir forenklet`() {
        val templ = outlineTestTemplate<Unit> {
            paragraph {
                textExpr(Bokmal to emptyList<String>().expr().isEmpty().format(BooleanFormatter))
            }
        }
        val expected = Content(
            Paragraph(
                listOf(
                    Content(
                        Text.Expression(
                            Expression.Invoke(
                                Expression.Invoke.Operation(
                                    "isEmpty",
                                    BinaryOperation.Documentation.Notation.FUNCTION
                                ),
                                first = Expression.Literal("[]"),
                            )
                        )
                    )
                )
            )
        )
        assertEquals(expected, TemplateDocumentationRenderer.render(templ, Bokmal).outline.first())
    }
}

private object BooleanFormatter : LocalizedFormatter<Boolean>() {
    override fun apply(first: Boolean, second: Language) = first.toString()
    override fun stableHashCode(): Int = "BooleanFormatter".hashCode()
}

// Dette er et util som er greit å ha etterhvert som vi jobberr videre med å forenkle expressions
object ExpressionVisitor {
    fun <T : Element> visit(nodes: List<ContentOrControlStructure<T>>): List<Expression> =
        nodes.flatMap { visit(it) }

    fun <T : Element> visit(node: ContentOrControlStructure<T>): List<Expression> =
        when (node) {
            is Conditional -> visit(node.showIf) + visit(node.showElse) + node.predicate
            is Content -> visit(node.content)
            is ContentOrControlStructure.ForEach -> visit(node.body) + node.items
        }

    fun visit(element: Element): List<Expression> =
        when (element) {
            is Element.ParagraphContent.ItemList.Item -> visit(element.text)
            is Paragraph -> visit(element.paragraph)
            is Element.OutlineContent.Title1 -> visit(element.text)
            is Element.OutlineContent.Title2 -> visit(element.text)
            is Element.ParagraphContent.ItemList -> visit(element.items)
            is Element.ParagraphContent.Table -> visit(element.header) + visit(element.rows)
            is Text.Expression -> listOf(element.expression)
            is Text.Literal -> emptyList()
            is Element.ParagraphContent.Table.Row -> element.cells.flatMap { visit(it.text) }
        }
}