package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object ForEachViewTestSelectors {

    object ListArgumentSelectors {
        val listeSelector = object : TemplateModelSelector<ForEachViewTest.ListArgument, List<String>> {
            override val className: String = "no.nav.pensjon.brev.template.render.dsl.ForEachViewTest.ListArgument"
            override val propertyName: String = "liste"
            override val propertyType: String = "kotlin.collections.List<kotlin.String>"
            override val selector = ForEachViewTest.ListArgument::liste
        }

        val TemplateGlobalScope<ForEachViewTest.ListArgument>.liste: Expression<List<String>>
            get() = Expression.UnaryInvoke(
                Expression.FromScope.Argument(),
                UnaryOperation.Select(listeSelector)
            )
    }
}
