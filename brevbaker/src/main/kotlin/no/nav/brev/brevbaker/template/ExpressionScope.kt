package no.nav.pensjon.brev.template


internal fun <LetterData : Any> Letter<LetterData>.toScope() = ExpressionScope(argument, felles, language)
internal fun <LetterData : Any, AttachmentData : Any> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData>) =
    ExpressionScope(data.eval(letterScope), letterScope.felles, letterScope.language)
