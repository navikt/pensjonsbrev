@file:Suppress("unused", "UNUSED_PARAMETER")

package no.nav.pensjon.brev.template

//TODO: This file can be removed if template classes and dsl (no.nav.pensjon.brev.template) is moved to a separate module that template-model-generator can depend on

abstract class Expression<T> {
    class Literal<T>(val value: T): Expression<T>()
    class UnaryInvoke<In, T>(expression: Expression<In>, operation: UnaryOperation<In, T>): Expression<T>()
    class FromScope<ParameterType, T>(val selector: ExpressionScope<ParameterType, *>.() -> T): Expression<T>()
}

abstract class UnaryOperation<In, T> {
    class Select<In: Any, T>(selector: TemplateModelSelector<In, T>): UnaryOperation<In, T>()
    class SafeCall<In: Any, Out>(selector: TemplateModelSelector<In, Out>) : UnaryOperation<In?, Out?>()
}

class ExpressionScope<Argument, Lang>(val argument: Argument, val language: Lang)

