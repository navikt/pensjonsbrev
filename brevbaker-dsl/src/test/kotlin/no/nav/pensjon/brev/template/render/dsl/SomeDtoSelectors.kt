package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object SomeDtoSelectors {
    val nameSelector = object : TemplateModelSelector<no.nav.pensjon.brev.template.render.dsl.SomeDto, kotlin.String> {
        override val className: String = "no.nav.pensjon.brev.template.render.dsl.SomeDto"
        override val propertyName: String = "name"
        override val propertyType: String = "kotlin.String"
        override val selector = no.nav.pensjon.brev.template.render.dsl.SomeDto::name
    }

    val TemplateGlobalScope<no.nav.pensjon.brev.template.render.dsl.SomeDto>.name: Expression<kotlin.String>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(nameSelector)
        )

    val Expression<no.nav.pensjon.brev.template.render.dsl.SomeDto>.name: Expression<kotlin.String>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(nameSelector)
        )

    val Expression<no.nav.pensjon.brev.template.render.dsl.SomeDto?>.name_safe: Expression<kotlin.String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(nameSelector)
        )

    val pensjonInnvilgetSelector = object :
        TemplateModelSelector<no.nav.pensjon.brev.template.render.dsl.SomeDto, kotlin.Boolean> {
        override val className: String = "no.nav.pensjon.brev.template.render.dsl.SomeDto"
        override val propertyName: String = "pensjonInnvilget"
        override val propertyType: String = "kotlin.Boolean"
        override val selector = no.nav.pensjon.brev.template.render.dsl.SomeDto::pensjonInnvilget
    }

    val TemplateGlobalScope<no.nav.pensjon.brev.template.render.dsl.SomeDto>.pensjonInnvilget: Expression<kotlin.Boolean>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(pensjonInnvilgetSelector)
        )

    val Expression<no.nav.pensjon.brev.template.render.dsl.SomeDto>.pensjonInnvilget: Expression<kotlin.Boolean>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(pensjonInnvilgetSelector)
        )

    val Expression<no.nav.pensjon.brev.template.render.dsl.SomeDto?>.pensjonInnvilget_safe: Expression<kotlin.Boolean?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(pensjonInnvilgetSelector)
        )

    val kortNavnSelector = object :
        TemplateModelSelector<no.nav.pensjon.brev.template.render.dsl.SomeDto, kotlin.String?> {
        override val className: String = "no.nav.pensjon.brev.template.render.dsl.SomeDto"
        override val propertyName: String = "kortNavn"
        override val propertyType: String = "kotlin.String?"
        override val selector = no.nav.pensjon.brev.template.render.dsl.SomeDto::kortNavn
    }

    val TemplateGlobalScope<no.nav.pensjon.brev.template.render.dsl.SomeDto>.kortNavn: Expression<kotlin.String?>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(kortNavnSelector)
        )

    val Expression<no.nav.pensjon.brev.template.render.dsl.SomeDto>.kortNavn: Expression<kotlin.String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(kortNavnSelector)
        )

    val Expression<no.nav.pensjon.brev.template.render.dsl.SomeDto?>.kortNavn_safe: Expression<kotlin.String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(kortNavnSelector)
        )


}