package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata


internal fun <LetterData : Any> Letter<LetterData>.toScope() = ExpressionScope(argument, felles, language)
internal fun <LetterData : Any, AttachmentData : VedleggBrevdata> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData>) =
    ExpressionScope(data.eval(letterScope), letterScope.felles, letterScope.language)
