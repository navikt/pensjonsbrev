package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdAuto
import no.nav.pensjon.brev.maler.OmsorgEgenAuto
import no.nav.pensjon.brev.maler.UngUfoerAuto
import no.nav.pensjon.brev.maler.example.DesignReferenceLetter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.jacksonObjectMapper
import no.nav.pensjon.brev.template.render.TemplateDocumentation.*
import no.nav.pensjon.brev.template.render.TemplateMetaRenderer.flattenLiteralConcat
import no.nav.pensjon.brev.template.render.TemplateMetaRenderer.mergeLiterals
import no.nav.pensjon.brevbaker.api.model.Year
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateMetaRendererTest {

    private val mapper = jacksonObjectMapper()

    @Test
    fun renderTest() {
        val doc = TemplateMetaRenderer.render(ForhaandsvarselEtteroppgjoerUfoeretrygdAuto.template.attachments.first().template.outline, Language.Bokmal)

        ExpressionVisitor.visit(doc).forEach {
            println(mapper.writeValueAsString(it))
            println("----------------------------")
        }

    }

    @Test
    fun flattenAndMergeTextExpr() {
        val fourPlusThree = Expression.BinaryInvoke(Year(4).expr(), Year(3).expr(), BinaryOperation.IntPlus(::Year)).format()
        val expr = "hei hvordan ".expr() + "går det? 4 + 3 = " + fourPlusThree + " QED"

        val flattened = expr.flattenLiteralConcat()
        assertEquals(listOf("hei hvordan ".expr(), "går det? 4 + 3 = ".expr(), fourPlusThree, " QED".expr()), flattened)
        assertEquals(listOf("hei hvordan går det? 4 + 3 = ".expr(), fourPlusThree, " QED".expr()), flattened.mergeLiterals())
    }

}

object ExpressionVisitor {
    fun <T: Element> visit(nodes: List<ContentOrControlStructure<T>>): List<TemplateDocumentation.Expression> =
        nodes.flatMap { visit(it) }
    fun <T: Element> visit(node: ContentOrControlStructure<T>): List<TemplateDocumentation.Expression> =
        when (node) {
            is ContentOrControlStructure.Conditional -> visit(node.showIf) + visit(node.showElse) + node.predicate
            is ContentOrControlStructure.Content -> visit(node.content)
            is ContentOrControlStructure.ForEach -> visit(node.body) + node.items
        }

    fun visit(element: Element): List<TemplateDocumentation.Expression> =
        when (element) {
            is Element.ParagraphContent.ItemList.Item -> visit(element.text)
            is Element.OutlineContent.Paragraph -> visit(element.paragraph)
            is Element.OutlineContent.Title1 -> visit(element.text)
            is Element.OutlineContent.Title2 -> visit(element.text)
            is Element.ParagraphContent.ItemList -> visit(element.items)
            is Element.ParagraphContent.Table -> visit(element.header) + visit(element.rows)
            is Element.ParagraphContent.Text.Expression -> listOf(element.expression)
            is Element.ParagraphContent.Text.Literal -> emptyList()
            is Element.ParagraphContent.Table.Row -> element.cells.flatMap { visit(it.text) }
        }
}