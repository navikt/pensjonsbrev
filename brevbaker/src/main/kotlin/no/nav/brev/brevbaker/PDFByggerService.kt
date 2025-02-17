package no.nav.brev.brevbaker

import no.nav.pensjon.brev.PDFRequest

interface PDFByggerService {
    suspend fun producePDF(pdfRequest: PDFRequest): PDFCompilationOutput
}