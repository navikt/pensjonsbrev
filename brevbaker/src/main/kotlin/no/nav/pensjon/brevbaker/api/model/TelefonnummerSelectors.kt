package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object TelefonnummerSelectors {
    val valueSelector = object : TemplateModelSelector<Telefonnummer, String> {
        override val className: String = "no.nav.pensjon.brevbaker.api.model.Telefonnummer"
        override val propertyName: String = "value"
        override val propertyType: String = "kotlin.String"
        override val selector = Telefonnummer::value
    }

    val TemplateGlobalScope<Telefonnummer>.value: Expression<String>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(valueSelector)
        )

    val Expression<Telefonnummer>.value: Expression<String>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(valueSelector)
        )

    val Expression<Telefonnummer?>.value_safe: Expression<String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(valueSelector)
        )


}
