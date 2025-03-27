package no.nav.pensjon.brev.pdfbygger.vedlegg

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggType

object VedleggModule : SimpleModule() {
    private fun readResolve(): Any = VedleggModule

    init {
        addDeserializer(PDFVedlegg::class.java, vedleggDeserializer())
    }

    private fun vedleggDeserializer() =
        object : StdDeserializer<PDFVedlegg>(PDFVedlegg::class.java) {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): PDFVedlegg {
                val node = p.codec.readTree<JsonNode>(p)
                val pdfVedleggType = PDFVedleggType.valueOf(node.get("type").textValue())
                val data = when (pdfVedleggType) {
                    PDFVedleggType.P1 -> SamletMeldingOmPensjonsvedtakDto::class.java
                    PDFVedleggType.InformasjonOmP1 -> EmptyBrevdata::class.java
                }
                return p.codec.treeToValue(node.get("data"), data).let { PDFVedlegg(pdfVedleggType, it) }
            }
        }
}