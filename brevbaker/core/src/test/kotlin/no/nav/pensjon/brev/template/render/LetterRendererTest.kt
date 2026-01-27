package no.nav.pensjon.brev.template.render

import no.nav.brev.brevbaker.FellesFactory.felles
import no.nav.brev.brevbaker.LiteralFactory.createByLanguage
import no.nav.brev.brevbaker.LiteralFactory.createText
import no.nav.brev.brevbaker.createContent
import no.nav.brev.brevbaker.createIncludeAttachment
import no.nav.brev.brevbaker.createParagraph
import no.nav.brev.brevbaker.createTextOnlyScope
import no.nav.brev.brevbaker.createTitle1
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.IncludeAttachment
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.brev.brevbaker.template.render.LetterRenderer
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.OutlineElement
import no.nav.pensjon.brev.template.ParagraphContentElement
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.render.TestVedleggDtoSelectors.testVerdi1
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.dsl.text
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class LetterRendererTest {

    val letter = LetterImpl(
        LetterExample.template,
        createLetterExampleDto(),
        Bokmal,
        felles
    )

    internal class MockRenderer : LetterRenderer<Document>() {
        var letterScope: ExpressionScope<*>? = null
        var template: LetterTemplate<*, *>? = null

        override fun renderLetter(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): Document {
            letterScope = scope
            this.template = template

            return object : Document {
                override val files: List<DocumentFile> get() = listOf()
            }
        }

        @JvmName("publicRenderOutlineContent")
        fun publicRender(scope: ExpressionScope<*>, elements: List<OutlineElement<*>>, renderBlock: (scope: ExpressionScope<*>, element: Element.OutlineContent<*>) -> Unit) =
            render(scope, elements, renderBlock)

        @JvmName("publicRenderParagraphContent")
        fun publicRender(scope: ExpressionScope<*>, elements: List<ParagraphContentElement<*>>, renderBlock: (scope: ExpressionScope<*>, element: Element.OutlineContent.ParagraphContent<*>) -> Unit) =
            render(scope, elements, renderBlock)

        fun publicRenderAttachments(scope: ExpressionScope<*>, attachments: List<IncludeAttachment<*, *>>, renderBlock: (scope: ExpressionScope<*>, id: Int, attachment: AttachmentTemplate<*, *>) -> Unit) =
            render(scope, attachments, renderBlock)
    }

    @Test
    fun `renderLetter receives letter scope and template`() {
        val renderer = MockRenderer()

        renderer.render(letter)

        assertScopeEquals(letter.toScope(), renderer.letterScope)
        assertEquals(letter.template, renderer.template)
    }

    @Test
    fun `render will pass each element of Content and letterScope to block`() {
        val expectedScope = letter.toScope()
        val expectedElements = listOf(
            createParagraph(
                listOf(
                    createContent(
                        createText(
                            Bokmal to "hei"
                        )
                    ),
                    createContent(
                        createText(
                            Bokmal to "jadda"
                        )
                    ),
                )
            ),
            createTitle1(emptyList()),
        )

        val actualElements = mutableListOf<Element.OutlineContent<*>>()
        val actualScopes = mutableListOf<ExpressionScope<*>>()
        MockRenderer().publicRender(
            expectedScope,
            expectedElements.map { createContent(it) }) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        assertEquals(expectedElements, actualElements)
        assertFalse(actualScopes.isEmpty())
        actualScopes.forEach { assertScopeEquals(expectedScope, it) }
    }

    @Test
    fun `render ForEach will pass body elements with next value and scope to evaluate expressions in to block`() {
        var nextExpression: Expression<String> = "hei".expr() // Will be changed to actual nextExpression below
        val itemsExpr = listOf("hei", "hallo", "Hi")

        val content = createTextOnlyScope<LangBokmal, Unit>().apply {
            forEach(itemsExpr.expr()) {
                text(bokmal { + it + " person"})
                text(bokmal { + "jadda" })
                nextExpression = it
            }
        }
        val expectedElements = listOf(
            createByLanguage(Bokmal to nextExpression + " person"),
            createText(Bokmal to "jadda")
        )

        val actualElements = mutableListOf<Element.OutlineContent.ParagraphContent<*>>()
        val actualScopes = mutableListOf<ExpressionScope<*>>()
        MockRenderer().publicRender(ExpressionScope(Unit, felles, Bokmal), content.elements) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        // Three times expectedElements because itemsExpr contains three items, so loop executes three times
        assertEquals(expectedElements + expectedElements + expectedElements, actualElements)

        // The actualScopes should allow us to evaluate nextExpression and end up with itemsExpr
        assertEquals(itemsExpr, actualScopes.map { nextExpression.eval(it) }.distinct())
    }

    @Test
    fun `render Conditional will pass showIf elements to block when predicate is true`() {
        val content = createTextOnlyScope<LangBokmal, Unit>().apply {
            showIf(true.expr()) {
                text(bokmal { + "hei "})
                text(bokmal { + "person"})
            } orShow {
                text(bokmal { + "should not be rendered"})
            }
        }

        val expectedScope = ExpressionScope(Unit, felles, Bokmal)
        val expectedElements = listOf(
            createText(Bokmal to "hei "),
            createText(Bokmal to "person"),
        )

        val actualScopes = mutableListOf<ExpressionScope<*>>()
        val actualElements = mutableListOf<Element.OutlineContent.ParagraphContent<*>>()
        MockRenderer().publicRender(expectedScope, content.elements) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        assertEquals(expectedElements, actualElements)
        // No scope changes
        assertFalse(actualScopes.isEmpty())
        actualScopes.forEach { assertScopeEquals(expectedScope, it) }
    }

    @Test
    fun `render Conditional will pass showElse elements to block when predicate is false`() {
        val content = createTextOnlyScope<LangBokmal, Unit>().apply {
            showIf(false.expr()) {
                text(bokmal { + "should not be rendered"})
            } orShow {
                text(bokmal { + "hei "})
                text(bokmal { + "person"})
            }
        }

        val expectedScope = ExpressionScope(Unit, felles, Bokmal)
        val expectedElements = listOf(
            createText(Bokmal to "hei "),
            createText(Bokmal to "person"),
        )

        val actualScopes = mutableListOf<ExpressionScope<*>>()
        val actualElements = mutableListOf<Element.OutlineContent.ParagraphContent<*>>()
        MockRenderer().publicRender(expectedScope, content.elements) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        assertEquals(expectedElements, actualElements)
        // No scope changes
        assertFalse(actualScopes.isEmpty())
        actualScopes.forEach { assertScopeEquals(expectedScope, it) }
    }

    @Test
    fun `render attachments will only render attachments where predicate is true`() {
        val attachment1 = createAttachment<LangBokmal, EmptyVedleggData>(title = {bokmal {+"tittel"}}, false) {
            paragraph { text(bokmal { + "Attachment #1"}) }
        }
        val attachment2 = createAttachment<LangBokmal, EmptyVedleggData>(title = {bokmal {+"tittel2"}}, false) {
            paragraph { text(bokmal { + "Attachment #2"}) }
        }
        val attachments = listOf(
            createIncludeAttachment(EmptyVedleggData.expr(), attachment1, true.expr()),
            createIncludeAttachment(EmptyVedleggData.expr(), attachment2, false.expr())
        )

        val actualAttachments = mutableListOf<AttachmentTemplate<*, *>>()
        MockRenderer().publicRenderAttachments(
            ExpressionScope(
                Unit,
                felles,
                Bokmal
            ), attachments) { _, _, attachment ->
            actualAttachments.add(attachment)
        }

        assertEquals(listOf(attachment1), actualAttachments)
    }

    data class LetterData(val name: String, val vedlegg: TestVedleggDto)

    @Test
    fun `render attachments will receive scope based on letterScope and data Expression and can evaluate attachment expressions`() {
        var attachmentScopedExpr: Expression<String>? = null
        val attachment1 = createAttachment(title = { text(bokmal { +"tittel" }) }, false) {
            paragraph { text(bokmal { +testVerdi1 }) }
            attachmentScopedExpr = testVerdi1
        }
        val letterData = LetterData("Anonymous", TestVedleggDto("a value", "another value"))

        val vedleggDataSelector = object : TemplateModelSelector<LetterData, TestVedleggDto> {
            override val className = "LetterData"
            override val propertyName = "vedlegg"
            override val propertyType = "TestVedleggDto"
            override val selector = LetterData::vedlegg
            override val redigerbar = false
        }
        val vedleggDataExpr = Expression.FromScope.Argument<LetterData>().select(vedleggDataSelector)

        var evaluatedAttachmentScopedExpr: String? = null
        MockRenderer().publicRenderAttachments(
            ExpressionScope(letterData, felles, Bokmal),
            listOf(createIncludeAttachment(vedleggDataExpr, attachment1, true.expr()))
        ) { scope, _, _ ->
            evaluatedAttachmentScopedExpr = attachmentScopedExpr?.eval(scope)
        }

        assertEquals(letterData.vedlegg.testVerdi1, evaluatedAttachmentScopedExpr)
    }


    private fun assertScopeEquals(expected: ExpressionScope<*>, actual: ExpressionScope<*>?) {
        assertEquals(expected.felles, actual?.felles)
        assertEquals(expected.argument, actual?.argument)
        assertEquals(expected.language, actual?.language)
    }
}