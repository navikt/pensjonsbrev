package no.nav.brev.brevbaker.template

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.IncludeAttachment

internal fun <LetterData : Any, AttachmentData : VedleggData> IncludeAttachment<*, AttachmentData>.toScope(letterScope: ExpressionScope<LetterData>) =
    ExpressionScope(
        data.eval(letterScope),
        letterScope.felles,
        letterScope.language,
        saksbehandlerValg = letterScope.saksbehandlerValg
    )
