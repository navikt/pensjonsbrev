package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.pensjon.brevbaker.api.model.LetterMarkup


internal fun TypstCodeScope.renderAttachment(attachment: LetterMarkup.Attachment, sectionNumber: Int) {
    val title = attachment.title.renderToPlainString()

    appendCodeFunction("startAttachment") {
        args {
            rawArg("\"${title.typstStringEscape()}\"")
            rawArg("input")
            rawArg("languageSettings")
            namedArg("sectionNumber", sectionNumber)
            namedArg("showCaseDetails", attachment.includeSakspart)
        }
    }

    renderBlocks(attachment.blocks)

    appendCodeFunction("endAttachment") {
        args {
            namedArg("sectionNumber", sectionNumber)
        }
    }
}
