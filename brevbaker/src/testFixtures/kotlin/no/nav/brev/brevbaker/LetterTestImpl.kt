package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brevbaker.api.model.Felles

data class LetterTestImpl<ParameterType : Any>(
    override val template: LetterTemplate<*, ParameterType>,
    override val argument: ParameterType,
    override val language: Language,
    override val felles: Felles,
) : Letter<ParameterType>