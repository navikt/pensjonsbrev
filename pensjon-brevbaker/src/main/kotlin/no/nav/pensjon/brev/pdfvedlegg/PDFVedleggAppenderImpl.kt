package no.nav.pensjon.brev.pdfvedlegg

import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.brev.brevbaker.PDFVedleggAppender
import no.nav.pensjon.brev.template.vedlegg.Felt
import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.apache.pdfbox.Loader
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSBase
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayOutputStream

internal object PDFVedleggAppenderImpl : PDFVedleggAppender {

    override fun leggPaaVedlegg(
        pdfCompilationOutput: PDFCompilationOutput,
        attachments: List<PDFVedlegg>,
        spraak: LanguageCode,
    ): PDFCompilationOutput {
        /* Ikke strengt nødvendig å returnere her, det vil fungere uten, men optimalisering.
        De aller, aller fleste brevene har ikke PDF-vedlegg, så de trenger ikke gå gjennom denne løypa
         */
        if (attachments.isEmpty()) {
            return pdfCompilationOutput
        }

        PDDocument().use { target ->
            val merger = PDFMergerUtility()

            Loader.loadPDF(pdfCompilationOutput.bytes).use {
                leggTilBlankPartallsideOgSaaLeggTilSide(it, target, merger)
            }

            attachments.forEach {
                VedleggAppender.lesInnVedlegg(it, spraak).use { vedlegg ->
                    FormFieldFiller(it.sider.flatMap { side -> side.felt }, spraak).fillFields(vedlegg)
                    leggTilBlankPartallsideOgSaaLeggTilSide(vedlegg, target, merger)
                }
            }
            return tilByteArray(target)
        }
    }

    private fun tilByteArray(target: PDDocument): PDFCompilationOutput {
        val outputStream = ByteArrayOutputStream()
        target.save(outputStream)
        return PDFCompilationOutput(outputStream.toByteArray())
    }
}

internal fun PDDocument.setValues(values: Map<String, String?>) = values.forEach { entry ->
    documentCatalog?.acroForm?.fields?.firstOrNull { it.fullyQualifiedName == entry.key }?.setValue(entry.value)
}

private fun leggTilBlankPartallsideOgSaaLeggTilSide(source: PDDocument, target: PDDocument, merger: PDFMergerUtility) {
    leggPaaBlankPartallsside(source, target)
    merger.leggTilSide(target, source)
}

private fun leggPaaBlankPartallsside(
    originaltDokument: PDDocument,
    target: PDDocument,
) {
    if (originaltDokument.pages.count % 2 == 1) {
        target.addPage(PDPage())
    }
}

internal fun PDFMergerUtility.leggTilSide(destionation: PDDocument, source: PDDocument) =
    appendDocument(destionation, source).also { source.close() }

private class FormFieldFiller(
    felter: List<Felt>,
    private val spraak: LanguageCode
) {
    private val relevanteFelter: Map<String, String> =
        felter.flatMap { it.felt.entries }
            .mapNotNull { (id, byLang) -> byLang?.get(spraak)?.let { id to it } }
            .toMap()

    private val visited = hashMapOf<COSBase, Boolean>()
    fun fillFields(document: PDDocument) {
        document.documentCatalog?.acroForm?.fields?.forEach { field ->
            field.cosObject?.let { fillDict(it) }
        }
    }

    private fun fillDict(dict: COSDictionary) {
        if (visited.contains(dict)) {
            return
        } else {
            visited.put(dict, false)
        }

        dict.getString(COSName.T)
            ?.let { relevanteFelter[it] }
            ?.also { setValue(dict, it) }

        dict.values.forEach { v ->
            when (v) {
                is COSDictionary -> fillDict(v)
                is COSArray -> fillArray(v)
            }
        }
    }

    private fun fillArray(arr: COSArray) {
        if (visited.contains(arr)) {
            return
        } else {
            visited.put(arr, false)
        }

        arr.forEach { v ->
            when (v) {
                is COSDictionary -> fillDict(v)
                is COSArray -> fillArray(v)
            }
        }
    }

    private fun setValue(dict: COSDictionary, value: String) {
        when (dict.getNameAsString(COSName.FT)) {
            "Tx", "Ch" -> {
                dict.setString(COSName.V, value)
                dict.setString(COSName.DV, value)
            }
            else -> { }
        }
    }
}
