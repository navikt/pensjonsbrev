package no.nav.pensjon.brev.dto

data class PDFCompilationOutput(val buildLog: String? = null, val pdf: ByteArray? = null)
