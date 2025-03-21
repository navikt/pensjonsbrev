package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object SomeDtoSelectors {
    private val nameSelector = object : TemplateModelSelector<SomeDto, String> {
        override val className: String = "no.nav.pensjon.brev.template.render.dsl.SomeDto"
        override val propertyName: String = "name"
        override val propertyType: String = "kotlin.String"
        override val selector = SomeDto::name
    }

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

    val pensjonInnvilgetSelector = object :
        TemplateModelSelector<SomeDto, Boolean> {
        override val className: String = "no.nav.pensjon.brev.template.render.dsl.SomeDto"
        override val propertyName: String = "pensjonInnvilget"
        override val propertyType: String = "kotlin.Boolean"
        override val selector = SomeDto::pensjonInnvilget
    }

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