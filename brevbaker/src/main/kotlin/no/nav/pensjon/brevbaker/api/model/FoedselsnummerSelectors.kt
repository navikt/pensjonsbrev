package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object FoedselsnummerSelectors {
    val valueSelector = object : TemplateModelSelector<Foedselsnummer, String> {
        override val className: String = "no.nav.pensjon.brevbaker.api.model.Foedselsnummer"
        override val propertyName: String = "value"
        override val propertyType: String = "kotlin.String"
        override val selector = Foedselsnummer::value
    }

    val TemplateGlobalScope<Foedselsnummer>.value: Expression<String>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(valueSelector)
        )

    val Expression<Foedselsnummer>.value: Expression<String>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(valueSelector)
        )

    val Expression<Foedselsnummer?>.value_safe: Expression<String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(valueSelector)
        )
}
