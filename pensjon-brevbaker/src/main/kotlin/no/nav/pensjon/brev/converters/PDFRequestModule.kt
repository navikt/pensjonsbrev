package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestImpl

@OptIn(InterneDataklasser::class)
object PDFRequestModule : SimpleModule() {
    private fun readResolve(): Any = PDFRequestModule

    init {
        addInterfaceDeserializer<PDFRequest, PDFRequestImpl>()
    }
}