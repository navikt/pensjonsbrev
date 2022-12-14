package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.*

abstract class LetterRenderer<R : RenderedLetter> {

    fun render(letter: Letter<*>): R = renderLetter(letter.toScope(), letter.template)

    private fun <C : Element<*>> controlStructure(scope: ExpressionScope<*, *>, element: ContentOrControlStructure<*, C>, block: (s: ExpressionScope<*, *>, e: C) -> Unit) {
        when (element) {
            is ContentOrControlStructure.Content -> block(scope, element.content)

            is ContentOrControlStructure.Conditional -> {
                val body = if (element.predicate.eval(scope)) element.showIf else element.showElse
                render(scope, body, block)
            }

            is ContentOrControlStructure.ForEach<*, C, *> -> element.render(scope) { s, e -> controlStructure(s, e, block) }
        }
    }

    protected open fun <C : Element<*>> render(scope: ExpressionScope<*, *>, elements: List<ContentOrControlStructure<*, C>>, renderBlock: (scope: ExpressionScope<*, *>, element: C) -> Unit) {
        elements.forEach { controlStructure(scope, it, renderBlock) }
    }

    @JvmName("renderAttachments")
    protected fun render(scope: ExpressionScope<*, *>, attachments: List<IncludeAttachment<*, *>>, renderAttachment: (attachmentScope: ExpressionScope<*, *>, id: Int, attachment: AttachmentTemplate<*, *>) -> Unit) {
        attachments.filter { it.predicate.eval(scope) }
            .mapIndexed { index, attachment -> renderAttachment(attachment.toScope(scope), index, attachment.template) }
    }

    protected fun <C : Element<*>> hasAnyContent(scope: ExpressionScope<*, *>, elements: List<ContentOrControlStructure<*, C>>): Boolean {
        var hasContent = false
        elements.forEach {
            controlStructure(scope, it) { _, _ -> hasContent = true }
        }
        return hasContent
    }

    protected fun getResource(fileName: String): ByteArray =
        this::class.java.getResourceAsStream("/$fileName")
            ?.use { it.readAllBytes() }
            ?: throw IllegalStateException("""Could not find resource /$fileName""")

    protected abstract fun renderLetter(scope: ExpressionScope<*, *>, template: LetterTemplate<*, *>): R

}