package no.nav.pensjon.brev.pdfbygger.model

//TODO: tex-filen som skal kompileres (letter.tex) bør være adskilt fra resten av filene slik at den garantert eksisterer
data class PdfCompilationInput(val files: Map<String, /*base64*/ String>)
