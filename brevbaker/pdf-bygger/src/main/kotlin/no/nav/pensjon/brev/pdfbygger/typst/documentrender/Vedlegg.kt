package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

/**
 * Render an attachment using start/end pattern.
 *
 * Output format:
 * startAttachment("title", sectionNumber: N, showCaseDetails: true/false)
 * content blocks...
 * endAttachment(sectionNumber: N)
 *
 * This pattern avoids issues with Typst content/code block syntax.
 */
internal fun TypstCodeScope.renderAttachment(attachment: LetterMarkup.Attachment, sectionNumber: Int) {
    val title = attachment.title.renderToPlainString()

    // Start the attachment
    appendCodeFunction("startAttachment") {
        args {
            rawArg("\"${title.typstStringEscape()}\"")
            namedArg("sectionNumber", sectionNumber)
            namedArg("showCaseDetails", attachment.includeSakspart)
        }
    }

    // Render attachment content
    renderBlocks(attachment.blocks)

    // End the attachment
    appendCodeFunction("endAttachment") {
        args {
            namedArg("sectionNumber", sectionNumber)
        }
    }
}
