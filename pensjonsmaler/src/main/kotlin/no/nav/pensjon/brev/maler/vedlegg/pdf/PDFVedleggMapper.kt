package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.brev.brevbaker.EmptyPDFVedleggData
import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

fun PDFVedleggData.tilPDFVedlegg() = when (this) {
    is P1Dto -> somVedlegg()
    is EmptyPDFVedleggData -> PDFVedlegg(type = tittel, listOf())
    else -> throw NotImplementedError("Ikke implementert støtte for ${javaClass.simpleName}")
}