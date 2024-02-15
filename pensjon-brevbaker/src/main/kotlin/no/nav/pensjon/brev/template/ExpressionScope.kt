package no.nav.pensjon.brev.template

import no.nav.pensjon.brevbaker.api.model.Felles

open class ExpressionScope<Argument : Any>(val argument: Argument, val felles: Felles, val language: Language) {

    class WithAssignment<Argument: Any, Var>(
        val value: Var,
        val expr: Expression.FromScope.Assigned<Var>,
        private val parent: ExpressionScope<Argument>
    ): ExpressionScope<Argument>(parent.argument, parent.felles, parent.language) {
        fun lookup(expr: Expression.FromScope.Assigned<Var>): Var =
            if (expr == this.expr) {
                value
            } else if (parent is WithAssignment<*, *>) {
                @Suppress("UNCHECKED_CAST")
                (parent as WithAssignment<*, Var>).lookup(expr)
            } else {
                throw MissingScopeForNextItemEvaluationException("Could not find scope matching: $expr")
            }
    }

    fun <Var> assign(value: Var, to: Expression.FromScope.Assigned<Var>): WithAssignment<Argument, Var> =
        WithAssignment(value, to, this)

}

fun <LetterData : Any> Letter<LetterData>.toScope() = ExpressionScope(argument, felles, language)
fun <LetterData : Any, AttachmentData : Any> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData>) =
    ExpressionScope(data.eval(letterScope), letterScope.felles, letterScope.language)
