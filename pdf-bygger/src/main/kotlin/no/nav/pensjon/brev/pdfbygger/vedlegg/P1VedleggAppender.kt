package no.nav.pensjon.brev.pdfbygger.vedlegg

import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    private const val RADER_PER_SIDE = 5

    internal fun lesInnP1(data: Map<String, Any>): PDDocument {
        val unwrapped = data["data"] as Map<*, *>

        val target = PDDocument()
        val merger = PDFMergerUtility()
        val innvilgedePensjoner = unwrapped["innvilgedePensjoner"] as List<Map<String, Any>>
        val avslaattePensjoner = unwrapped["avslaattePensjoner"] as List<Map<String, Any>>
        val antallSide2 = Math.ceilDiv(innvilgedePensjoner.size, RADER_PER_SIDE)
        val antallSide3 = Math.ceilDiv(avslaattePensjoner.size, RADER_PER_SIDE)
        val totaltAntallSider = 1 + antallSide2 + antallSide3 + 1

        merger.leggTilSide(target, settOppSide1(unwrapped, totaltAntallSider))
        settOppSide2(
            merger,
            target,
            innvilgedePensjoner,
            antallSide2 = antallSide2,
            totaltAntallSider = totaltAntallSider
        )
        settOppSide3(
            avslaattePensjoner,
            merger,
            target,
            startSide3 = 1 + antallSide2,
            antallSide3 = antallSide3,
            totaltAntallSider = totaltAntallSider
        )
        settOppSide4(
            unwrapped["institusjon"] as Map<String, Any>,
            merger,
            target,
            totaltAntallSider = totaltAntallSider
        )

        return target
    }

    private fun settOppSide1(unwrapped: Map<*, *>, totaltAntallSider: Int): PDDocument {
        val innehaver = (unwrapped["innehaver"] as Map<String, Any?>)
            .let { map -> feltSide1Innehaver.associate { "holder-$it" to map[it]?.toString() } }
        val forsikrede = (unwrapped["forsikrede"] as Map<String, Any?>)
            .let { map -> feltSide1Forsikrede.associate { "insured-$it" to map[it]?.toString() } }
        return lesInnPDF("/P1-side1.pdf").also {
            it.setValues(
                innehaver
                    .plus(forsikrede)
                    .plus("kravMottattDato" to unwrapped["kravMottattDato"].toString())
                    .plus("sakstype" to unwrapped["sakstype"].toString())
                    .plus("page" to "1/$totaltAntallSider")
            )
        }
    }

    private fun settOppSide2(
        merger: PDFMergerUtility,
        target: PDDocument,
        innvilgedePensjoner: List<Map<String, Any>>,
        antallSide2: Int,
        totaltAntallSider: Int,
    ) = (0..<antallSide2).map { index ->
        ((index * RADER_PER_SIDE)..(index * RADER_PER_SIDE) + 4).map { radnummer ->
            innvilgedePensjoner.getOrNull(radnummer)
                .letOrEmpty { flettInnInnvilgetPensjon((radnummer % RADER_PER_SIDE) + 1, it) }
        }.let { flettefelt ->
            lesInnPDF("/P1-side2.pdf").also {
                it.setValues(
                    flettefelt.flatten().plus("page" to "${1 + index + 1}/$totaltAntallSider")
                )
            }
        }
    }.forEach { merger.leggTilSide(target, it) }

    private fun flettInnInnvilgetPensjon(radnummer: Int, pensjon: Map<String, Any>) =
        feltSide2.associate { "$radnummer-$it" to pensjon[it].toString() }

    private fun settOppSide3(
        avslaattePensjoner: List<Map<String, Any>>,
        merger: PDFMergerUtility,
        target: PDDocument,
        startSide3: Int,
        antallSide3: Int,
        totaltAntallSider: Int,
    ) = (0..<antallSide3).map { index ->
        ((index * RADER_PER_SIDE)..(index * RADER_PER_SIDE) + 4).map { radnummer ->
            avslaattePensjoner.getOrNull(radnummer)
                .letOrEmpty { pensjon -> flettInnAvslaattPensjon((radnummer % RADER_PER_SIDE) + 1, pensjon) }
        }.let { flettefelt ->
            lesInnPDF("/P1-side3.pdf").also {
                it.setValues(flettefelt.flatten().plus("page" to "${startSide3 + index + 1}/$totaltAntallSider"))
            }
        }
    }.forEach { merger.leggTilSide(target, it) }

    private fun flettInnAvslaattPensjon(radnummer: Int, pensjon: Map<String, Any>) =
        feltSide3.associate { "$radnummer-$it" to pensjon[it].toString() }

    private fun settOppSide4(
        institution: Map<String, Any>,
        merger: PDFMergerUtility,
        target: PDDocument,
        totaltAntallSider: Int,
    ) =
        lesInnPDF("/P1-side4.pdf")
            .also {
                it.setValues(
                    feltSide4.associateWith { key -> institution[key]?.toString() }
                        .plus("page" to "${totaltAntallSider}/${totaltAntallSider}")
                )
            }.also { merger.leggTilSide(target, it) }

    internal fun lesInnP1Vedlegg() = lesInnPDF("/P1-vedlegg.pdf")

    private fun lesInnPDF(filnavn: String): PDDocument = PDDocument.load(javaClass.getResourceAsStream(filnavn))
}

internal fun List<Map<String, String>>.flatten() = this.flatMap { it.entries }.associate { it.key to it.value }

private fun Map<String, Any>?.letOrEmpty(block: (Map<String, Any>) -> Map<String, String>): Map<String, String> =
    this?.let { block(it) } ?: emptyMap()

private val feltSide1Innehaver = listOf(
    "fornavn",
    "etternavn",
    "etternavnVedFoedsel",
    "adresselinje",
    "landkode",
    "postnummer",
    "poststed",
)

private val feltSide1Forsikrede = listOf(
    "fornavn",
    "etternavn",
    "etternavnVedFoedsel",
    "foedselsdato",
    "adresselinje",
    "poststed",
    "postnummer",
    "landkode",
)

private val feltSide2 = listOf(
    "institusjon",
    "pensjonstype",
    "datoFoersteUtbetaling",
    "bruttobeloep",
    "grunnlagInnvilget",
    "reduksjonsgrunnlag",
    "vurderingsperiode",
    "adresseNyVurdering"
)

private val feltSide3 = listOf(
    "institusjon",
    "pensjonstype",
    "avslagsbegrunnelse",
    "vurderingsperiode",
    "adresseNyVurdering"
)

private val feltSide4 = listOf(
    "navn",
    "adresselinje",
    "poststed",
    "postnummer",
    "landkode",
    "institusjonsID",
    "faksnummer",
    "telefonnummer",
    "epost",
    "dato",
    "underskrift",
)