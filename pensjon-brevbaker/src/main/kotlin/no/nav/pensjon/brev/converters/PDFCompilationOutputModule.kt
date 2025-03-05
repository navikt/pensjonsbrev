package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFCompilationOutputImpl

@OptIn(InterneDataklasser::class)
object PDFCompilationOutputModule : SimpleModule() {
    private fun readResolve(): Any = PDFCompilationOutputModule

    init {
        addInterfaceDeserializer<PDFCompilationOutput, PDFCompilationOutputImpl>()
    }
}