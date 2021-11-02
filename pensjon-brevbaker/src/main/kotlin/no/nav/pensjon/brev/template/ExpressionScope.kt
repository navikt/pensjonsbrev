package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.Felles

data class ExpressionScope<Argument: Any, Lang: Language>(val argument: Argument, val felles: Felles, val language: Lang)

fun <LetterData: Any> Letter<LetterData>.toScope() = ExpressionScope(argument, felles, language)