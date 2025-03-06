package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionImpl
import no.nav.pensjon.brevbaker.api.model.Felles

interface TemplateGlobalScope<LetterData : Any> {
    val argument: Expression<LetterData>
        get() = ExpressionImpl.FromScopeImpl.ArgumentImpl()

    val felles: Expression<Felles>
        get() = ExpressionImpl.FromScopeImpl.FellesImpl
}