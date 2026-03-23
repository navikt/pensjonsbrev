package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstAppendable
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

/**
 * Render an attachment.
 *
 * Output format:
 * attachment("title", sectionNumber: N, showCaseDetails: true/false)[
 *   content blocks...
 * ]
 */
internal fun TypstAppendable.renderAttachment(attachment: LetterMarkup.Attachment, sectionNumber: Int) {
    val title = attachment.title.renderToPlainString()

    appendCodeFunction("attachment") {
        args {
            // Title as first positional argument (as a string)
            rawArg("\"${title.typstStringEscape()}\"")
            namedArg("sectionNumber", sectionNumber)
            namedArg("showCaseDetails", attachment.includeSakspart)
        }
        content {
            renderBlocks(attachment.blocks)
        }
    }
}


