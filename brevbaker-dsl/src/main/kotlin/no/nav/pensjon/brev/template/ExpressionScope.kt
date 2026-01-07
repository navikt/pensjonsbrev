package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.expression.SelectorUsage
import no.nav.pensjon.brevbaker.api.model.Felles

sealed interface ExpressionScope<Argument : Any> {
    val argument: Argument
    val felles: Felles
    val language: Language

    fun <Var> assign(value: Var, to: Expression.FromScope.Assigned<Var>): ExpressionScope<Argument> =
        AssignmentExpressionScope(value, to, this)

    fun markUsage(selector: TemplateModelSelector<*, *>)

    companion object {
        operator fun <Argument : Any> invoke(argument: Argument, felles: Felles, language: Language, selectorUsage: SelectorUsage? = null): ExpressionScope<Argument> =
            RootExpressionScope(argument, felles, language, selectorUsage)
    }
}

internal class RootExpressionScope<Argument : Any>(
    override val argument: Argument,
    override val felles: Felles,
    override val language: Language,
    val selectorUsage: SelectorUsage? = null
) : ExpressionScope<Argument> {

    override fun markUsage(selector: TemplateModelSelector<*, *>) {
        if (selectorUsage != null) {
            selectorUsage += selector
        }
    }
}

internal class AssignmentExpressionScope<Argument: Any, Var>(
    val value: Var,
    val expr: Expression.FromScope.Assigned<Var>,
    private val parent: ExpressionScope<Argument>
): ExpressionScope<Argument> {

    override val argument: Argument get() = parent.argument
    override val felles: Felles get() = parent.felles
    override val language: Language get() = parent.language

    fun lookup(expr: Expression.FromScope.Assigned<Var>): Var =
        // Uses referential equality since nested ForEach over the same collection-expression will be equal.
        if (expr === this.expr) {
            value
        } else {
            (parent as? AssignmentExpressionScope<*, Var>)?.lookup(expr)
                ?: throw MissingScopeForNextItemEvaluationException("Could not find scope matching: $expr")
        }

    override fun markUsage(selector: TemplateModelSelector<*, *>) = parent.markUsage(selector)
}