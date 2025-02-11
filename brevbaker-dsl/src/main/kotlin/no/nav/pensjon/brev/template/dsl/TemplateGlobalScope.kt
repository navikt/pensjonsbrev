package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brevbaker.api.model.Felles

interface TemplateGlobalScope<LetterData : Any> {
    val argument: Expression<LetterData>
        get() = Expression.FromScope.Argument()

    val felles: Expression<Felles>
        get() = Expression.FromScope.Felles
}