package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.Felles

open class ExpressionScope<Argument : Any, Lang : Language>(val argument: Argument, val felles: Felles, val language: Lang) {

    class WithAssignment<Parent: Any, Lang: Language, Var>(
        val value: Var,
        val expr: Expression.FromScope.Assigned<Var>,
        private val parent: ExpressionScope<Parent, Lang>
    ): ExpressionScope<Parent, Lang>(parent.argument, parent.felles, parent.language) {
        fun lookup(expr: Expression.FromScope.Assigned<Var>): Var =
            if (expr == this.expr) {
                value
            } else if (parent is WithAssignment<*, *, *>) {
                @Suppress("UNCHECKED_CAST")
                (parent as WithAssignment<*, *, Var>).lookup(expr)
            } else {
                throw MissingScopeForNextItemEvaluationException("Could not find scope matching: $expr")
            }
    }

    fun <Var> assign(value: Var, to: Expression.FromScope.Assigned<Var>): WithAssignment<Argument, Lang, Var> =
        WithAssignment(value, to, this)

}

fun <LetterData : Any> Letter<LetterData>.toScope() = ExpressionScope(argument, felles, language)
fun <LetterData : Any, AttachmentData : Any> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData, *>) =
    ExpressionScope(data.eval(letterScope), letterScope.felles, letterScope.language)
