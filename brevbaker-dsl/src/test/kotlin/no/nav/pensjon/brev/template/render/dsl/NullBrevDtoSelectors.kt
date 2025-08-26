package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object NullBrevDtoSelectors {
    private val test1Selector = SimpleSelector(NullBrevDto::test1)
    val TemplateGlobalScope<NullBrevDto>.test1: Expression<String?>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(test1Selector)
        )

    val Expression<NullBrevDto>.test1: Expression<String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(test1Selector)
        )

    val test2Selector = SimpleSelector(NullBrevDto::test2)
    val TemplateGlobalScope<NullBrevDto>.test2: Expression<String?>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(test2Selector)
        )

    val Expression<NullBrevDto>.test2: Expression<String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(test2Selector)
        )


}
