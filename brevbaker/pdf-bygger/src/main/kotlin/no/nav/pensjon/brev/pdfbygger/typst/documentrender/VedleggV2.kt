package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import no.nav.pensjon.brev.pdfbygger.typst.TypstCodeScope
import no.nav.pensjon.brev.pdfbygger.typst.typstStringEscape
import no.nav.brev.brevbaker.markup.Attachment


internal fun TypstCodeScope.renderAttachmentV2(attachment: Attachment, sectionNumber: Int) {
    val title = attachment.title1.renderToPlainStringV2()

    appendCodeFunction("startAttachment") {
        args {
            rawArg("\"${title.typstStringEscape()}\"")
            rawArg("input")
            rawArg("languageSettings")
            namedArg("sectionNumber", sectionNumber)
            namedArg("showCaseDetails", attachment.inkluderSaksinformasjon)
        }
    }

    renderBlocksV2(attachment.blocks)

    appendCodeFunction("endAttachment") {
        args {
            namedArg("sectionNumber", sectionNumber)
        }
    }
}
