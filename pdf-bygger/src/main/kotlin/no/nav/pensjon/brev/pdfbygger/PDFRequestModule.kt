package no.nav.pensjon.brev.pdfbygger

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.PDFRequest
import no.nav.pensjon.brev.PDFRequestImpl

@OptIn(InterneDataklasser::class)
object PDFRequestModule : SimpleModule() {
    private fun readResolve(): Any = PDFRequestModule

    init {
        addDeserializer(PDFRequest::class.java, PDFRequestDeserializer)
    }

    private object PDFRequestDeserializer : FellesDeserializer<PDFRequest, PDFRequestImpl>(PDFRequestImpl::class.java)
}