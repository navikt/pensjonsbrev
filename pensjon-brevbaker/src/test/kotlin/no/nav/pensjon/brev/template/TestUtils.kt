package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

fun <AttachmentData : Any, Lang: LanguageSupport> createVedleggTestTemplate(
    template: AttachmentTemplate<Lang, AttachmentData>,
    attachmentData: Expression<AttachmentData>,
    languages: Lang,
) = createTemplate(
    name = "test-template",
    letterDataType = Unit::class,
    languages = languages,
    letterMetadata = LetterMetadata(
        "test mal",
        isSensitiv = false,
        distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
        brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
    ),
) {
    title {
        eval("Tittel".expr())
    }

    outline {}

    includeAttachment(template,attachmentData)
}
