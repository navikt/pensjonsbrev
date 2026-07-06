package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.SimpleSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object ForEachStableHashTestSelectors {

    object ListArgumentSelectors {
        val listeSelector = SimpleSelector(ForEachStableHashTest.ListArgument::liste)
        val TemplateGlobalScope<ForEachStableHashTest.ListArgument>.liste: Expression<List<String>>
            get() = Expression.UnaryInvoke(
                Expression.FromScope.Argument(),
                UnaryOperation.Select(listeSelector)
            )
    }
}
