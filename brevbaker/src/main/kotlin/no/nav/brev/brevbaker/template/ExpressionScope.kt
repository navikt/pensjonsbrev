package no.nav.brev.brevbaker.template

import no.nav.pensjon.brev.api.model.maler.Vedlegg
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.IncludeAttachment
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.expression.SelectorUsage


internal fun <LetterData : Any> Letter<LetterData>.toScope(selectorUsage: SelectorUsage? = null) = ExpressionScope(argument, felles, language, selectorUsage)
internal fun <LetterData : Any, AttachmentData : Vedlegg> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData>) =
    ExpressionScope(data.eval(letterScope), letterScope.felles, letterScope.language)
