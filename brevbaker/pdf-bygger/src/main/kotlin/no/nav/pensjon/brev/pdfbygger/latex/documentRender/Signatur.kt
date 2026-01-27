package no.nav.pensjon.brev.pdfbygger.latex.documentRender

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

internal fun LatexAppendable.signatur(
    signatur: LetterMarkup.Signatur,
    brevtype: LetterMetadata.Brevtype,
) {
    appendNewCmd("feltnavenhet", signatur.navAvsenderEnhet)

    val saksbehandlerNavn = signatur.saksbehandlerNavn
        ?.also { appendNewCmd("feltsaksbehandlernavn", it) }

    val attestantNavn = signatur.attesterendeSaksbehandlerNavn
        ?.takeIf { brevtype == LetterMetadata.Brevtype.VEDTAKSBREV }
        ?.also { appendNewCmd("feltattestantnavn", it) }

    appendNewCmd("closingbehandlet") {
        when {
            saksbehandlerNavn != null && attestantNavn != null -> {
                appendCmd("closingdoublesignature")
            }
            saksbehandlerNavn != null -> {
                appendCmd("closingsinglesignature")
            }
            brevtype == LetterMetadata.Brevtype.VEDTAKSBREV -> {
                appendCmd("closingautosignaturevedtaksbrev")
            }
            else -> {
                appendCmd("closingautosignatureinfobrev")
            }
        }
    }
}
