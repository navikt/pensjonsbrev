package no.nav.pensjon.brev.pdfbygger

class PdfCompilationException(val compilationLog: String): Exception("Failed to compile PDF$compilationLog") {
}