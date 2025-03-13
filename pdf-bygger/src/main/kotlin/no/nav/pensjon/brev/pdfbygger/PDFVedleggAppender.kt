package no.nav.pensjon.brev.pdfbygger

import no.nav.pensjon.brev.pdfbygger.vedlegg.P1VedleggAppender.leggPaaP1
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggType
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm
import java.io.ByteArrayOutputStream

internal object PDFVedleggAppender {

    internal fun leggPaaVedlegg(pdfCompilationResponse: PDFCompilationResponse.Bytes, attachments: List<PDFVedlegg>) : PDFCompilationResponse.Bytes =
        attachments.map { leggPaaVedlegg(pdfCompilationResponse, it) }
            .firstOrNull()
            ?.let {
                // TODO: Vi bør sleppe denne om vi flyttar merginga til litt tidlegare i flyten
                val outputstream = ByteArrayOutputStream()
                it.save(outputstream)
                PDFCompilationResponse.Bytes(outputstream.toByteArray())
        } ?: pdfCompilationResponse

    private fun leggPaaVedlegg(pdfCompilationResponse: PDFCompilationResponse.Bytes, attachment: PDFVedlegg) : PDDocument =
        when (attachment.type) {
            PDFVedleggType.P1 -> leggPaaP1(pdfCompilationResponse, attachment.data)
        }
}

internal fun PDAcroForm.setValues(values: Map<String, String?>) = values.forEach { entry ->
    fields.firstOrNull { it.fullyQualifiedName == entry.key }?.setValue(entry.value)
}