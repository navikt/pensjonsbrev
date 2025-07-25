package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object SomeDtoSelectors {
    private val nameSelector = SimpleSelector(SomeDto::name)
    val TemplateGlobalScope<SomeDto>.name: Expression<String>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(nameSelector)
        )

    val Expression<SomeDto>.name: Expression<String>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(nameSelector)
        )

    val pensjonInnvilgetSelector = SimpleSelector(SomeDto::pensjonInnvilget)
    val TemplateGlobalScope<SomeDto>.pensjonInnvilget: Expression<Boolean>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(pensjonInnvilgetSelector)
        )

    val Expression<SomeDto>.pensjonInnvilget: Expression<Boolean>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(pensjonInnvilgetSelector)
        )
}