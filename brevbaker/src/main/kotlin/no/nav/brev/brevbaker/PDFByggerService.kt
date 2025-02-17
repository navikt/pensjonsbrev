package no.nav.brev.brevbaker

import no.nav.pensjon.brev.PDFRequest

interface PDFByggerService {
    suspend fun producePDF(pdfRequest: PDFRequest, url: String = PATH): PDFCompilationOutput
}

private const val PATH = "/produserBrev"