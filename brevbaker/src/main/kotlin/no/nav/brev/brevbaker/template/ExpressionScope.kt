package no.nav.pensjon.brev.template

fun <LetterData : Any> Letter<LetterData>.toScope() = ExpressionScope(argument, felles, language)

fun <LetterData : Any, AttachmentData : Any> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData>) =
    ExpressionScope(data.eval(letterScope), letterScope.felles, letterScope.language)
