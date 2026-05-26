package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.expression.SelectorUsage
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles

sealed interface ExpressionScope<Argument : Any> {
    val argument: Argument
    val felles: BrevbakerFelles
    val language: Language
    val saksbehandlervalg: Map<String, SaksbehandlervalgVerdi>

    fun <Var> assign(value: Var, to: Expression.FromScope.Assigned<Var>): ExpressionScope<Argument> =
        AssignmentExpressionScope(value, to, this)

    fun markUsage(selector: TemplateModelSelector<*, *>)

    companion object {
        operator fun <Argument : Any> invoke(argument: Argument, felles: BrevbakerFelles, language: Language, saksbehandlervalg: Map<String, SaksbehandlervalgVerdi> = emptyMap(), selectorUsage: SelectorUsage? = null): ExpressionScope<Argument > =
            RootExpressionScope(argument, felles, language, saksbehandlervalg, selectorUsage)
    }
}

internal class RootExpressionScope<Argument : Any>(
    override val argument: Argument,
    override val felles: BrevbakerFelles,
    override val language: Language,
    override val saksbehandlervalg: Map<String, SaksbehandlervalgVerdi>,
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
    override val felles: BrevbakerFelles get() = parent.felles
    override val language: Language get() = parent.language
    override val saksbehandlervalg: Map<String, SaksbehandlervalgVerdi> get() = parent.saksbehandlervalg

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