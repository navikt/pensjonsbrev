package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.Felles

open class ExpressionScope<Argument : Any, Lang : Language>(val argument: Argument, val felles: Felles, val language: Lang)

fun <LetterData : Any> Letter<LetterData>.toScope() = ExpressionScope(argument, felles, language)
fun <LetterData : Any, AttachmentData : Any> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData, *>) =
    ExpressionScope(data.eval(letterScope), letterScope.felles, letterScope.language)
