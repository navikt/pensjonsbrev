package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object NullBrevDtoSelectors {
    private val test1Selector = object : TemplateModelSelector<NullBrevDto, String?> {
        override val className: String = "no.nav.pensjon.brev.template.render.dsl.NullBrevDto"
        override val propertyName: String = "test1"
        override val propertyType: String = "kotlin.String?"
        override val selector = NullBrevDto::test1
    }

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

    val test2Selector = object : TemplateModelSelector<NullBrevDto, String?> {
        override val className: String = "no.nav.pensjon.brev.template.render.dsl.NullBrevDto"
        override val propertyName: String = "test2"
        override val propertyType: String = "kotlin.String?"
        override val selector = NullBrevDto::test2
    }

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
