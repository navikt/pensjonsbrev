package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFCompilationOutputImpl
import no.nav.pensjon.brev.pdfbygger.FellesDeserializer

@OptIn(InterneDataklasser::class)
object PDFCompilationOutputModule : SimpleModule() {
    private fun readResolve(): Any = PDFCompilationOutputModule

    init {
        addDeserializer(PDFCompilationOutput::class.java, PDFCompilationOutputDeserializer)
    }

    private object PDFCompilationOutputDeserializer :
        FellesDeserializer<PDFCompilationOutput, PDFCompilationOutputImpl>(PDFCompilationOutputImpl::class.java)
}