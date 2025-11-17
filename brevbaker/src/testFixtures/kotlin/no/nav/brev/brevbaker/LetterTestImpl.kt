package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brevbaker.api.model.Felles

data class LetterTestImpl<ParameterType : BrevbakerBrevdata>(
    override val template: LetterTemplate<*, ParameterType>,
    override val argument: ParameterType,
    override val language: Language,
    override val felles: Felles,
) : Letter<ParameterType>