package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.AttachmentTitle
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import kotlin.collections.forEach

internal fun LatexAppendable.vedlegg(attachments: List<AttachmentTitle>) {
    appendNewCmd("feltclosingvedlegg") {
        if (attachments.isNotEmpty()) {
            appendCmd("begin", "attachmentList")

            attachments.forEach { attachment ->
                append("""\item """, escape = false)
                renderTextAsPlain(attachment.title)
            }
            appendCmd("end", "attachmentList")
        }
    }
}

internal fun LatexAppendable.renderAttachment(attachment: LetterMarkup.Attachment) {
    appendCmd("startvedlegg") {
        arg { renderTextAsPlain(attachment.title) }
        arg { if (attachment.includeSakspart) append("includesakinfo") }
    }
    renderBlocks(attachment.blocks)
    appendCmd("sluttvedlegg")
}
