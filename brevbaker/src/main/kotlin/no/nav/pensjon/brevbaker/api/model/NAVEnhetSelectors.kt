package no.nav.pensjon.brevbaker.api.model

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.TemplateModelSelector
import no.nav.pensjon.brev.template.UnaryOperation
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope

object NAVEnhetSelectors {
    val navnSelector = object : TemplateModelSelector<NAVEnhet, String> {
        override val className: String = "no.nav.pensjon.brevbaker.api.model.NavEnhet"
        override val propertyName: String = "navn"
        override val propertyType: String = "kotlin.String"
        override val selector = NAVEnhet::navn
    }

    val TemplateGlobalScope<NAVEnhet>.navn: Expression<String>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(navnSelector)
        )

    val Expression<NAVEnhet>.navn: Expression<String>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(navnSelector)
        )

    val Expression<NAVEnhet?>.navn_safe: Expression<String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(navnSelector)
        )

    val nettsideSelector = object : TemplateModelSelector<NAVEnhet, String> {
        override val className: String = "no.nav.pensjon.brevbaker.api.model.NAVEnhet"
        override val propertyName: String = "nettside"
        override val propertyType: String = "kotlin.String"
        override val selector = NAVEnhet::nettside
    }

    val TemplateGlobalScope<NAVEnhet>.nettside: Expression<String>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(nettsideSelector)
        )

    val Expression<NAVEnhet>.nettside: Expression<String>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(nettsideSelector)
        )

    val Expression<NAVEnhet?>.nettside_safe: Expression<String?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(nettsideSelector)
        )

    val telefonnummerSelector = object :
        TemplateModelSelector<NAVEnhet, Telefonnummer> {
        override val className: String = "no.nav.pensjon.brevbaker.api.model.NAVEnhet"
        override val propertyName: String = "telefonnummer"
        override val propertyType: String = "no.nav.pensjon.brevbaker.api.model.Telefonnummer"
        override val selector = NAVEnhet::telefonnummer
    }

    val TemplateGlobalScope<NAVEnhet>.telefonnummer: Expression<Telefonnummer>
        get() = Expression.UnaryInvoke(
            Expression.FromScope.Argument(),
            UnaryOperation.Select(telefonnummerSelector)
        )

    val Expression<NAVEnhet>.telefonnummer: Expression<Telefonnummer>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.Select(telefonnummerSelector)
        )

    val Expression<NAVEnhet?>.telefonnummer_safe: Expression<Telefonnummer?>
        get() = Expression.UnaryInvoke(
            this,
            UnaryOperation.SafeCall(telefonnummerSelector)
        )


}
