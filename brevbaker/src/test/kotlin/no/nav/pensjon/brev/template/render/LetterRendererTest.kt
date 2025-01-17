package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.IncludeAttachment
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterRenderer
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.OutlineElement
import no.nav.pensjon.brev.template.ParagraphContentElement
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.render.Fixtures.felles
import no.nav.pensjon.brev.template.render.TestVedleggDtoSelectors.testVerdi1
import no.nav.pensjon.brev.template.toScope
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LetterRendererTest {

    val letter = Letter(
        LetterExample.template,
        createLetterExampleDto(),
        Language.Bokmal,
        felles
    )

    class MockRenderer : LetterRenderer<Document>() {
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
        Assertions.assertEquals(letter.template, renderer.template)
    }

    @Test
    fun `render will pass each element of Content and letterScope to block`() {
        val expectedScope = letter.toScope()
        val expectedElements = listOf(
            Element.OutlineContent.Paragraph(
                listOf(
                    ContentOrControlStructure.Content(
                        Element.OutlineContent.ParagraphContent.Text.Literal.create(
                            Language.Bokmal to "hei"
                        )
                    ),
                    ContentOrControlStructure.Content(
                        Element.OutlineContent.ParagraphContent.Text.Literal.create(
                            Language.Bokmal to "jadda"
                        )
                    ),
                )
            ),
            Element.OutlineContent.Title1(emptyList()),
        )

        val actualElements = mutableListOf<Element.OutlineContent<*>>()
        val actualScopes = mutableListOf<ExpressionScope<*>>()
        MockRenderer().publicRender(
            expectedScope,
            expectedElements.map { ContentOrControlStructure.Content(it) }) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        Assertions.assertEquals(expectedElements, actualElements)
        Assertions.assertFalse(actualScopes.isEmpty())
        actualScopes.forEach { assertScopeEquals(expectedScope, it) }
    }

    @Test
    fun `render ForEach will pass body elements with next value and scope to evaluate expressions in to block`() {
        var nextExpression: Expression<String> = "hei".expr() // Will be changed to actual nextExpression below
        val itemsExpr = listOf("hei", "hallo", "Hi")

        val content = TextOnlyScope<LangBokmal, Unit>().apply {
            forEach(itemsExpr.expr()) {
                textExpr(Language.Bokmal to it + " person")
                text(Language.Bokmal to "jadda")
                nextExpression = it
            }
        }
        val expectedElements = listOf(
            Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(Language.Bokmal to nextExpression + " person"),
            Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "jadda")
        )

        val actualElements = mutableListOf<Element.OutlineContent.ParagraphContent<*>>()
        val actualScopes = mutableListOf<ExpressionScope<*>>()
        MockRenderer().publicRender(ExpressionScope(Unit, felles, Language.Bokmal), content.elements) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        // Three times expectedElements because itemsExpr contains three items, so loop executes three times
        Assertions.assertEquals(expectedElements + expectedElements + expectedElements, actualElements)

        // The actualScopes should allow us to evaluate nextExpression and end up with itemsExpr
        Assertions.assertEquals(itemsExpr, actualScopes.map { nextExpression.eval(it) }.distinct())
    }

    @Test
    fun `render Conditional will pass showIf elements to block when predicate is true`() {
        val content = TextOnlyScope<LangBokmal, Unit>().apply {
            showIf(true.expr()) {
                text(Language.Bokmal to "hei ")
                text(Language.Bokmal to "person")
            } orShow {
                text(Language.Bokmal to "should not be rendered")
            }
        }

        val expectedScope = ExpressionScope(Unit, felles, Language.Bokmal)
        val expectedElements = listOf(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "hei "),
            Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "person"),
        )

        val actualScopes = mutableListOf<ExpressionScope<*>>()
        val actualElements = mutableListOf<Element.OutlineContent.ParagraphContent<*>>()
        MockRenderer().publicRender(expectedScope, content.elements) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        Assertions.assertEquals(expectedElements, actualElements)
        // No scope changes
        Assertions.assertFalse(actualScopes.isEmpty())
        actualScopes.forEach { assertScopeEquals(expectedScope, it) }
    }

    @Test
    fun `render Conditional will pass showElse elements to block when predicate is false`() {
        val content = TextOnlyScope<LangBokmal, Unit>().apply {
            showIf(false.expr()) {
                text(Language.Bokmal to "should not be rendered")
            } orShow {
                text(Language.Bokmal to "hei ")
                text(Language.Bokmal to "person")
            }
        }

        val expectedScope = ExpressionScope(Unit, felles, Language.Bokmal)
        val expectedElements = listOf(
            Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "hei "),
            Element.OutlineContent.ParagraphContent.Text.Literal.create(Language.Bokmal to "person"),
        )

        val actualScopes = mutableListOf<ExpressionScope<*>>()
        val actualElements = mutableListOf<Element.OutlineContent.ParagraphContent<*>>()
        MockRenderer().publicRender(expectedScope, content.elements) { scope, element ->
            actualElements.add(element)
            actualScopes.add(scope)
        }

        Assertions.assertEquals(expectedElements, actualElements)
        // No scope changes
        Assertions.assertFalse(actualScopes.isEmpty())
        actualScopes.forEach { assertScopeEquals(expectedScope, it) }
    }

    @Test
    fun `render attachments will only render attachments where predicate is true`() {
        val attachment1 = createAttachment<LangBokmal, Unit>(newText(Language.Bokmal to "tittel"), false) {
            paragraph { text(Language.Bokmal to "Attachment #1") }
        }
        val attachment2 = createAttachment<LangBokmal, Unit>(newText(Language.Bokmal to "tittel2"), false) {
            paragraph { text(Language.Bokmal to "Attachment #2") }
        }
        val attachments = listOf(
            IncludeAttachment(Unit.expr(), attachment1, true.expr()),
            IncludeAttachment(Unit.expr(), attachment2, false.expr())
        )

        val actualAttachments = mutableListOf<AttachmentTemplate<*, *>>()
        MockRenderer().publicRenderAttachments(
            ExpressionScope(
                Unit,
                felles,
                Language.Bokmal
            ), attachments) { _, _, attachment ->
            actualAttachments.add(attachment)
        }

        Assertions.assertEquals(listOf(attachment1), actualAttachments)
    }

    data class LetterData(val name: String, val vedlegg: TestVedleggDto)

    @Test
    fun `render attachments will receive scope based on letterScope and data Expression and can evaluate attachment expressions`() {
        var attachmentScopedExpr: Expression<String>? = null
        val attachment1 = createAttachment(newText(Language.Bokmal to "tittel"), false) {
            paragraph { textExpr(Language.Bokmal to testVerdi1) }
            attachmentScopedExpr = testVerdi1
        }
        val letterData = LetterData("Anonymous", TestVedleggDto("a value", "another value"))

        val vedleggDataSelector = object : TemplateModelSelector<LetterData, TestVedleggDto> {
            override val className = "LetterData"
            override val propertyName = "vedlegg"
            override val propertyType = "TestVedleggDto"
            override val selector = LetterData::vedlegg
        }
        val vedleggDataExpr = Expression.FromScope.Argument<LetterData>().select(vedleggDataSelector)

        var evaluatedAttachmentScopedExpr: String? = null
        MockRenderer().publicRenderAttachments(
            ExpressionScope(letterData, felles, Language.Bokmal),
            listOf(IncludeAttachment(vedleggDataExpr, attachment1, true.expr()))
        ) { scope, _, _ ->
            evaluatedAttachmentScopedExpr = attachmentScopedExpr?.eval(scope)
        }

        Assertions.assertEquals(letterData.vedlegg.testVerdi1, evaluatedAttachmentScopedExpr)
    }


    private fun assertScopeEquals(expected: ExpressionScope<*>, actual: ExpressionScope<*>?) {
        Assertions.assertEquals(expected.felles, actual?.felles)
        Assertions.assertEquals(expected.argument, actual?.argument)
        Assertions.assertEquals(expected.language, actual?.language)
    }
}