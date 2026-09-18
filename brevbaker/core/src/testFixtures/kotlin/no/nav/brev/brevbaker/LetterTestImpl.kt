package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.expression.SelectorUsage
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles

data class LetterTestImpl<ParameterType : Any>(
    override val template: LetterTemplate<*, ParameterType>,
    override val argument: ParameterType,
    val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val language: Language,
    override val felles: BrevbakerFelles,
) : Letter<ParameterType> {
    constructor(
        template: LetterTemplate<*, ParameterType>,
        argument: ParameterType,
        language: Language,
        felles: BrevbakerFelles,
    ) : this(template, argument, lagSaksbehandlervalg(), language, felles)

    override fun toScope(selectorUsage: SelectorUsage?): ExpressionScope<ParameterType> =
        ExpressionScope(argument, felles, language, selectorUsage, saksbehandlerValg)
}