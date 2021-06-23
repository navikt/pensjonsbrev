package no.nav.pensjon.brev.pdfbygger

data class PDFCompilationOutput(val buildLog: String? = null, val pdf: ByteArray? = null)
