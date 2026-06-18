package no.nav.brev.brevbaker.template.render

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.IncludeAttachment
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.StableHash

internal sealed interface StableHashProvider {
    fun of(element: StableHash): Int
    fun forEach(index: Int): StableHashProvider

    object Default : StableHashProvider {
        override fun of(element: StableHash): Int = element.stableHashCode()
        override fun forEach(index: Int): StableHashProvider = ForEach(index)
    }

    private class ForEach(private val hashCode: Int) : StableHashProvider, StableHash by StableHash.of(hashCode) {

        override fun of(element: StableHash): Int =
            StableHash.hash(element, this)

        override fun forEach(index: Int): StableHashProvider =
            ForEach(StableHash.hash(StableHash.of(index), this))
    }
}

internal class RenderContext private constructor(
    val scope: ExpressionScope<*>,
    val stableHashProvider: StableHashProvider,
) {
    constructor(scope: ExpressionScope<*>) : this(scope, StableHashProvider.Default)

    fun forEachAssignment(value: Any, to: Expression.FromScope.Assigned<*>, index: Int) =
        RenderContext(scope = scope.assign(value, to), stableHashProvider = stableHashProvider.forEach(index))

    fun createFor(attachment: IncludeAttachment<*, *>) =
        RenderContext(scope = attachment.toScope(scope))

    fun stableHash(element: StableHash): Int =
        stableHashProvider.of(element)
}

internal typealias RenderFunction<C> = (ctx: RenderContext, e: C) -> Unit

internal abstract class LetterRenderer<R : Any> {

    fun render(letter: Letter<*>): R = renderLetter(letter.toScope(), letter.template)

    private fun <C : Element<*>> controlStructure(context: RenderContext, element: ContentOrControlStructure<*, C>, block: RenderFunction<C>) {
        when (element) {
            is ContentOrControlStructure.Content -> block(context, element.content)

            is ContentOrControlStructure.Conditional -> {
                val body = if (element.predicate.eval(context.scope)) element.showIf else element.showElse
                render(context, body, block)
            }

            is ContentOrControlStructure.ForEach<*, C, *> ->
                element.items.eval(context.scope)
                    .mapIndexed { index, value -> context.forEachAssignment(value, element.next, index) }
                    .forEach { scopeWithItem ->
                        render(scopeWithItem, element.body, block)
                    }
        }
    }

    protected fun <C : Element<*>> render(context: RenderContext, elements: Collection<ContentOrControlStructure<*, C>>, renderBlock: RenderFunction<C>) {
        elements.forEach { controlStructure(context, it, renderBlock) }
    }

    @JvmName("renderAttachments")
    fun render(context: RenderContext, attachments: List<IncludeAttachment<*, *>>, renderAttachment: (ctx: RenderContext, id: Int, attachment: AttachmentTemplate<*, *>) -> Unit) {
        attachments.filter { it.predicate.eval(context.scope) }
            .mapIndexed { index, attachment -> renderAttachment(context.createFor(attachment), index, attachment.template) }
    }

    abstract fun renderLetter(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): R

}