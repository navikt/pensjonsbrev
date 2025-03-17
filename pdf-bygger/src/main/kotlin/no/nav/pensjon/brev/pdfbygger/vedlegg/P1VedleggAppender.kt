package no.nav.pensjon.brev.pdfbygger.vedlegg

import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    internal fun leggPaaP1(data: Map<String, Any>): PDDocument {
        val unwrapped = data["data"] as Map<*, *>

        val target = PDDocument()
        val merger = PDFMergerUtility()
        merger.appendDocument(target, settOppSide1(unwrapped))
        settOppSide2(merger, target, unwrapped["innvilgedePensjoner"] as List<Map<String, Any>>)
        settOppSide3(unwrapped["avslaattePensjoner"] as List<Map<String, Any>>, merger, target)
        settOppSide4(unwrapped["institution"] as Map<String, Any>, merger, target)

        return target
    }

    private fun settOppSide1(unwrapped: Map<*, *>): PDDocument {
        val holderData = unwrapped["holder"] as Map<*, *>
        val holder = mapOf(
            "fornavn" to holderData["fornavn"].toString(),
            "etternavn" to holderData["etternavn"].toString(),
            "etternavn_foedsel" to holderData["etternavnVedFoedsel"]?.toString(),
            "gateadresse" to holderData["adresselinje"].toString(),
            "landkode" to holderData["landkode"].toString(),
            "postkode" to holderData["postnummer"].toString(),
            "by" to holderData["poststed"].toString()
        )
        return lesInnPDF("/P1-side1.pdf").also { it.setValues(holder) }
    }

    private fun settOppSide2(
        merger: PDFMergerUtility,
        target: PDDocument,
        innvilgedePensjoner: List<Map<String, Any>>,
    ) = (0..<Math.ceilDiv(innvilgedePensjoner.size, 5)).map { it * 5 }.map { index ->
        (index..index + 4).map { radnummer ->
            innvilgedePensjoner.getOrNull(radnummer)
                ?.let { pensjon -> flettInnInnvilgetPensjon(radnummer + 1, pensjon) }
                ?: emptyMap()
        }.let { flettefelt ->
            lesInnPDF("/P1-side2.pdf").also {
                it.setValues(flettefelt.flatMap { it.entries }.associate { it.key to it.value })
            }
        }
    }.forEach { merger.appendDocument(target, it) }

    private fun flettInnInnvilgetPensjon(radnummer: Int, pensjon: Map<String, Any>) = listOf(
        "institusjon",
        "type",
        "datoFoersteUtbetaling",
        "bruttobeloep",
        "grunnlagInnvilget",
        "reduksjonsgrunnlag",
        "vurderingsperiode",
        "adresseNyVurdering"
    ).associate { "$radnummer-$it" to pensjon[it].toString() }

    private fun settOppSide3(
        avslaattePensjoner: List<Map<String, Any>>,
        merger: PDFMergerUtility,
        target: PDDocument,
    ) = (0..<Math.ceilDiv(avslaattePensjoner.size, 5)).map { it * 5 }.map { index ->
        (index..index + 4).map { radnummer ->
            avslaattePensjoner.getOrNull(radnummer)
                ?.let { pensjon -> flettInnAvslaattPensjon(radnummer + 1, pensjon) }
                ?: emptyMap()
        }.let { flettefelt ->
            lesInnPDF("/P1-side3.pdf").also {
                it.setValues(flettefelt.flatMap { it.entries }.associate { it.key to it.value })
            }
        }
    }.forEach { merger.appendDocument(target, it) }

    private fun flettInnAvslaattPensjon(radnummer: Int, pensjon: Map<String, Any>) = listOf(
        "institusjon",
        "type",
        "avslagsbegrunnelse",
        "vurderingsperiode",
        "adresseNyVurdering"
    ).associate { "$radnummer-$it" to pensjon[it].toString() }


    private fun settOppSide4(
        institution: Map<String, Any>,
        merger: PDFMergerUtility,
        target: PDDocument,
    ) =
        lesInnPDF("/P1-side4.pdf")
            .also {
                it.setValues(
                    listOf(
                        "name",
                        "street",
                        "town",
                        "postcode",
                        "countryCode",
                        "institutionID",
                        "officeFax",
                        "officePhone",
                        "email",
                        "date",
                        "signature",
                    ).associateWith { key -> institution[key]?.toString() }
                )
            }.also { merger.appendDocument(target, it) }

    internal fun leggPaaP1Vedlegg() = lesInnPDF("/P1-vedlegg.pdf")

    private fun lesInnPDF(filnavn: String): PDDocument = PDDocument.load(javaClass.getResourceAsStream(filnavn))
}