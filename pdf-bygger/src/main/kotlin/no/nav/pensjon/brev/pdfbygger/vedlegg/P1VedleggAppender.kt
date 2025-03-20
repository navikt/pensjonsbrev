package no.nav.pensjon.brev.pdfbygger.vedlegg

import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument

internal object P1VedleggAppender {

    private const val RADER_PER_SIDE = 5

    internal fun lesInnP1(unwrapped: SamletMeldingOmPensjonsvedtakDto): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val innvilgedePensjoner = unwrapped.innvilgedePensjoner
        val avslaattePensjoner = unwrapped.avslaattePensjoner
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
            unwrapped.institusjon,
            merger,
            target,
            totaltAntallSider = totaltAntallSider
        )

        return target
    }

    private fun settOppSide1(unwrapped: SamletMeldingOmPensjonsvedtakDto, totaltAntallSider: Int): PDDocument {
        val innehaver = mapOf(
            "holder-fornavn" to unwrapped.innehaver.fornavn,
            "holder-etternavn" to unwrapped.innehaver.etternavn,
            "holder-etternavnVedFoedsel" to unwrapped.innehaver.etternavnVedFoedsel,
            "holder-foedselsdato" to unwrapped.innehaver.foedselsdato,
            "holder-adresselinje" to unwrapped.innehaver.adresselinje,
            "holder-poststed" to unwrapped.innehaver.poststed,
            "holder-postnummer" to unwrapped.innehaver.postnummer,
            "holder-landkode" to unwrapped.innehaver.landkode,
            "holder-poststed" to unwrapped.innehaver.poststed
        )
        val forsikrede = mapOf(
            "insured-fornavn" to unwrapped.forsikrede.fornavn,
            "insured-etternavn" to unwrapped.forsikrede.etternavn,
            "insured-etternavnVedFoedsel" to unwrapped.forsikrede.etternavnVedFoedsel,
            "insured-foedselsdato" to unwrapped.forsikrede.foedselsdato,
            "insured-adresselinje" to unwrapped.forsikrede.adresselinje,
            "insured-poststed" to unwrapped.forsikrede.poststed,
            "insured-postnummer" to unwrapped.forsikrede.postnummer,
            "insured-landkode" to unwrapped.forsikrede.landkode,
            "insured-poststed" to unwrapped.forsikrede.poststed
        )


        return lesInnPDF("/P1-side1.pdf").also {
            it.setValues(
                innehaver
                    .plus(forsikrede)
                    .plus("kravMottattDato" to unwrapped.kravMottattDato)
                    .plus("sakstype" to unwrapped.sakstype.name)
                    .plus("page" to "1/$totaltAntallSider")
            )
        }
    }

    private fun settOppSide2(
        merger: PDFMergerUtility,
        target: PDDocument,
        innvilgedePensjoner: List<SamletMeldingOmPensjonsvedtakDto.InnvilgetPensjon>,
        antallSide2: Int,
        totaltAntallSider: Int,
    ) = (0..<antallSide2).map { index ->
        ((index * RADER_PER_SIDE)..(index * RADER_PER_SIDE) + 4).map { radnummer ->
            innvilgedePensjoner.getOrNull(radnummer)
                ?.let { flettInnInnvilgetPensjon((radnummer % RADER_PER_SIDE) + 1, it) }
                ?: emptyMap()
        }.let { flettefelt ->
            lesInnPDF("/P1-side2.pdf").also {
                it.setValues(
                    flettefelt.flatten().plus("page" to "${1 + index + 1}/$totaltAntallSider")
                )
            }
        }
    }.forEach { merger.leggTilSide(target, it) }

    private fun flettInnInnvilgetPensjon(radnummer: Int, pensjon: SamletMeldingOmPensjonsvedtakDto.InnvilgetPensjon) =
        mapOf(
            "$radnummer-institusjon" to pensjon.institusjon,
            "$radnummer-pensjonstype" to pensjon.pensjonstype.name,
            "$radnummer-datoFoersteUtbetaling" to pensjon.datoFoersteUtbetaling,
            "$radnummer-bruttobeloep" to pensjon.bruttobeloep,
            "$radnummer-grunnlagInnvilget" to pensjon.grunnlagInnvilget,
            "$radnummer-reduksjonsgrunnlag" to pensjon.reduksjonsgrunnlag,
            "$radnummer-vurderingsperiode" to pensjon.vurderingsperiode,
            "$radnummer-adresseNyVurdering" to pensjon.adresseNyVurdering,
        )

    private fun settOppSide3(
        avslaattePensjoner: List<SamletMeldingOmPensjonsvedtakDto.AvslaattPensjon>,
        merger: PDFMergerUtility,
        target: PDDocument,
        startSide3: Int,
        antallSide3: Int,
        totaltAntallSider: Int,
    ) = (0..<antallSide3).map { index ->
        ((index * RADER_PER_SIDE)..(index * RADER_PER_SIDE) + 4).map { radnummer ->
            avslaattePensjoner.getOrNull(radnummer)
                ?.let { pensjon -> flettInnAvslaattPensjon((radnummer % RADER_PER_SIDE) + 1, pensjon) }
                ?: emptyMap()
        }.let { flettefelt ->
            lesInnPDF("/P1-side3.pdf").also {
                it.setValues(flettefelt.flatten().plus("page" to "${startSide3 + index + 1}/$totaltAntallSider"))
            }
        }
    }.forEach { merger.leggTilSide(target, it) }

    private fun flettInnAvslaattPensjon(radnummer: Int, pensjon: SamletMeldingOmPensjonsvedtakDto.AvslaattPensjon) =
        mapOf(
            "$radnummer-institusjon" to pensjon.institusjon,
            "$radnummer-pensjonstype" to pensjon.pensjonstype.name,
            "$radnummer-avslagsbegrunnelse" to pensjon.avslagsbegrunnelse,
            "$radnummer-vurderingsperiode" to pensjon.vurderingsperiode,
            "$radnummer-adresseNyVurdering" to pensjon.adresseNyVurdering,
        )

    private fun settOppSide4(
        institution: SamletMeldingOmPensjonsvedtakDto.Institusjon,
        merger: PDFMergerUtility,
        target: PDDocument,
        totaltAntallSider: Int,
    ) =
        lesInnPDF("/P1-side4.pdf")
            .also {
                it.setValues(
                    mapOf(
                        "navn" to institution.navn,
                        "adresselinje" to institution.adresselinje,
                        "poststed" to institution.poststed,
                        "postnummer" to institution.postnummer,
                        "landkode" to institution.landkode,
                        "institusjonsID" to institution.institusjonsID,
                        "faksnummer" to institution.faksnummer,
                        "telefonnummer" to institution.telefonnummer,
                        "epost" to institution.epost,
                        "dato" to institution.epost,
                        "underskrift" to institution.underskrift,
                        "page" to "${totaltAntallSider}/${totaltAntallSider}"
                    )
                )
            }.also { merger.leggTilSide(target, it) }

    internal fun lesInnP1Vedlegg() = lesInnPDF("/P1-vedlegg.pdf")

    private fun lesInnPDF(filnavn: String): PDDocument = PDDocument.load(javaClass.getResourceAsStream(filnavn))
}

internal fun List<Map<String, Any?>>.flatten(): Map<String, String?> =
    this.flatMap { it.entries }.associate { it.key to it.value?.toString() }
