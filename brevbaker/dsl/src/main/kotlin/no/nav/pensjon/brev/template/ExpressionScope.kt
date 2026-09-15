package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.expression.SelectorUsage
import no.nav.pensjon.brev.template.validation.MissingScopeForNextItemEvaluationException
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles

sealed interface ExpressionScope<Argument : Any> {
    val argument: Argument
    val felles: BrevbakerFelles
    val language: Language
    val saksbehandlerValg: SaksbehandlervalgIDSL

    fun <Var> assign(value: Var, to: Expression.FromScope.Assigned<Var>): ExpressionScope<Argument> =
        AssignmentExpressionScope(value, to, this)

    fun markUsage(selector: TemplateModelSelector<*, *>)

    companion object {
        // TODO: trur denne bør vera strengere enn Any, feks FagsystemBrevdata
        operator fun <Argument : Any> invoke(
            argument: Argument,
            felles: BrevbakerFelles,
            language: Language,
            selectorUsage: SelectorUsage? = null,
            saksbehandlerValg: SaksbehandlervalgIDSL,
        ): ExpressionScope<Argument> =
            RootExpressionScope(argument, felles, language, selectorUsage, saksbehandlerValg)
    }
}

internal class RootExpressionScope<Argument : Any>(
    override val argument: Argument,
    override val felles: BrevbakerFelles,
    override val language: Language,
    val selectorUsage: SelectorUsage? = null,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
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
    override val saksbehandlerValg: SaksbehandlervalgIDSL get() = parent.saksbehandlerValg

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

data object EmptySaksbehandlervalgIDSL : SaksbehandlervalgIDSL {
    override val size = 0
    override val keys: Set<String> = emptySet()
    override val values: Collection<Any?> = emptySet()
    override val entries: Set<Map.Entry<String, Any?>> = emptySet()
    override fun isEmpty() = true
    override fun containsKey(key: String) = true
    override fun containsValue(value: Any?) = false
    override fun get(key: String): Any? = null
}