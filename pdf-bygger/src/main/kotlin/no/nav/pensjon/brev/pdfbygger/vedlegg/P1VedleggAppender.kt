package no.nav.pensjon.brev.pdfbygger.vedlegg

import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    private const val RADER_PER_SIDE = 5

    internal fun leggPaaP1(data: Map<String, Any>): PDDocument {
        val unwrapped = data["data"] as Map<*, *>

        val target = PDDocument()
        val merger = PDFMergerUtility()
        val innvilgedePensjoner = unwrapped["innvilgedePensjoner"] as List<Map<String, Any>>
        val avslaattePensjoner = unwrapped["avslaattePensjoner"] as List<Map<String, Any>>
        val antallSide2 = Math.ceilDiv(innvilgedePensjoner.size, RADER_PER_SIDE)
        val antallSide3 = Math.ceilDiv(avslaattePensjoner.size, RADER_PER_SIDE)
        val totaltAntallSider = 1 + antallSide2 + antallSide3 + 1

        merger.appendDocument(target, settOppSide1(unwrapped, totaltAntallSider))
        settOppSide2(merger, target, innvilgedePensjoner, antallSide2, totaltAntallSider)
        settOppSide3(avslaattePensjoner, merger, target, startSide3 = 1+antallSide2, antallSide3 = antallSide3, totaltAntallSider = totaltAntallSider)
        settOppSide4(unwrapped["institution"] as Map<String, Any>, merger, target, totaltAntallSider = totaltAntallSider)

        return target
    }

    private fun settOppSide1(unwrapped: Map<*, *>, totaltAntallSider: Int): PDDocument {
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
        val insuredData = unwrapped["insuredPerson"] as Map<*, *>
        val insured = mapOf(
            "insured-surname" to insuredData["fornavn"].toString(),
            "insured-forename" to insuredData["etternavn"].toString(),
            "insured-surenameAtBirth" to insuredData["etternavnVedFoedsel"].toString(),
            "insured-dateOfBirth" to insuredData["dateOfBirth"]?.toString(),
            "insured-street" to insuredData["adresselinje"].toString(),
            "insured-town" to insuredData["poststed"].toString(),
            "insured-postcode" to insuredData["postnummer"].toString(),
            "insured-countryCode" to insuredData["landkode"].toString()
        )
        return lesInnPDF("/P1-side1.pdf").also { it.setValues(holder.plus(insured).plus("page" to "1/$totaltAntallSider")) }
    }

    private fun settOppSide2(
        merger: PDFMergerUtility,
        target: PDDocument,
        innvilgedePensjoner: List<Map<String, Any>>,
        antallSide2: Int,
        totaltAntallSider: Int,
    ) = (0..<antallSide2).map { index ->
        ((index* RADER_PER_SIDE)..(index* RADER_PER_SIDE) + 4).map { radnummer ->
            innvilgedePensjoner.getOrNull(radnummer)
                .letOrEmpty { pensjon -> flettInnInnvilgetPensjon((radnummer% RADER_PER_SIDE)+1, pensjon) }
        }.let { flettefelt ->
            lesInnPDF("/P1-side2.pdf").also {
                it.setValues(flettefelt.flatMap { it.entries }.associate { it.key to it.value }
                    .plus("page" to "${1 + index + 1}/$totaltAntallSider"))
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
        startSide3: Int,
        antallSide3: Int,
        totaltAntallSider: Int,
    ) = (0..<antallSide3).map { index ->
        ((index* RADER_PER_SIDE)..(index* RADER_PER_SIDE) + 4).map { radnummer ->
            avslaattePensjoner.getOrNull(radnummer)
                .letOrEmpty { pensjon -> flettInnAvslaattPensjon((radnummer% RADER_PER_SIDE)+1, pensjon) }
        }.let { flettefelt ->
            lesInnPDF("/P1-side3.pdf").also {
                it.setValues(
                    flettefelt.flatMap { it.entries }.associate { it.key to it.value }
                        .plus("page" to "${startSide3 + index + 1}/$totaltAntallSider")
                )
            }
        }
    }.forEach { merger.appendDocument(target, it) }

    private fun Map<String, Any>?.letOrEmpty(block: (Map<String, Any>) -> Map<String, String>): Map<String, String> = this?.let { block(it) } ?: emptyMap()

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
        totaltAntallSider: Int,
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
                        .plus("page" to "${totaltAntallSider}/${totaltAntallSider}")
                )
            }.also { merger.appendDocument(target, it) }

    internal fun leggPaaP1Vedlegg() = lesInnPDF("/P1-vedlegg.pdf")

    private fun lesInnPDF(filnavn: String): PDDocument = PDDocument.load(javaClass.getResourceAsStream(filnavn))
}