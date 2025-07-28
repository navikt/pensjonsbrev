package no.nav.pensjon.brev.pdfbygger.vedlegg

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import java.time.LocalDate
import java.time.Period
import java.time.format.FormatStyle

internal object P1VedleggAppender {

    private const val RADER_PER_SIDE = 5

    internal fun lesInnP1(unwrapped: SamletMeldingOmPensjonsvedtakDto, spraak: LanguageCode): PDDocument {
        val target = PDDocument()
        val merger = PDFMergerUtility()
        val innvilgedePensjoner = unwrapped.innvilgedePensjoner
        val avslaattePensjoner = unwrapped.avslaattePensjoner
        val antallSide2 = Math.ceilDiv(innvilgedePensjoner.size, RADER_PER_SIDE)
        val antallSide3 = Math.ceilDiv(avslaattePensjoner.size, RADER_PER_SIDE)
        val totaltAntallSider = 1 + antallSide2 + antallSide3 + 1

        merger.leggTilSide(target, settOppSide1(unwrapped, totaltAntallSider, spraak))
        settOppSide2(
            merger,
            target,
            innvilgedePensjoner,
            antallSide2 = antallSide2,
            totaltAntallSider = totaltAntallSider,
            spraak
        )
        settOppSide3(
            avslaattePensjoner,
            merger,
            target,
            startSide3 = 1 + antallSide2,
            antallSide3 = antallSide3,
            totaltAntallSider = totaltAntallSider,
            spraak
        )
        settOppSide4(
            unwrapped.utfyllendeInstitusjon,
            merger,
            target,
            totaltAntallSider = totaltAntallSider,
            spraak
        )

        return target
    }

    private fun settOppSide1(
        unwrapped: SamletMeldingOmPensjonsvedtakDto, totaltAntallSider: Int, spraak: LanguageCode,
    ): PDDocument {
        val innehaver = mapOf(
            "holder-fornavn" to unwrapped.innehaver.fornavn,
            "holder-etternavn" to unwrapped.innehaver.etternavn,
            "holder-etternavnVedFoedsel" to unwrapped.innehaver.etternavnVedFoedsel,
            "holder-foedselsdato" to unwrapped.innehaver.foedselsdato?.formater(),
            "holder-adresselinje" to unwrapped.innehaver.adresselinje,
            "holder-poststed" to unwrapped.innehaver.poststed.value,
            "holder-postnummer" to unwrapped.innehaver.postnummer.value,
            "holder-landkode" to unwrapped.innehaver.landkode.landkode,
        )
        val forsikrede = mapOf(
            "insured-fornavn" to unwrapped.forsikrede.fornavn,
            "insured-etternavn" to unwrapped.forsikrede.etternavn,
            "insured-etternavnVedFoedsel" to unwrapped.forsikrede.etternavnVedFoedsel,
            "insured-foedselsdato" to unwrapped.forsikrede.foedselsdato?.formater(),
            "insured-adresselinje" to unwrapped.forsikrede.adresselinje,
            "insured-poststed" to unwrapped.forsikrede.poststed.value,
            "insured-postnummer" to unwrapped.forsikrede.postnummer.value,
            "insured-landkode" to unwrapped.forsikrede.landkode.landkode,
        )


        return lesInnPDF("P1-side1.pdf", spraak).also {
            it.setValues(
                innehaver
                    .plus(forsikrede)
                    .plus("kravMottattDato" to unwrapped.kravMottattDato.formater())
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
        spraak: LanguageCode,
    ) = (0..<antallSide2).map { index ->
        ((index * RADER_PER_SIDE)..(index * RADER_PER_SIDE) + 4).map { radnummer ->
            innvilgedePensjoner.getOrNull(radnummer)
                ?.let { flettInnInnvilgetPensjon((radnummer % RADER_PER_SIDE) + 1, it) }
                ?: emptyMap()
        }.let { flettefelt ->
            lesInnPDF("P1-side2.pdf", spraak).also {
                it.setValues(
                    flettefelt.flatten().plus("page" to "${1 + index + 1}/$totaltAntallSider")
                )
            }
        }
    }.forEach { merger.leggTilSide(target, it) }

    private fun flettInnInnvilgetPensjon(radnummer: Int, pensjon: SamletMeldingOmPensjonsvedtakDto.InnvilgetPensjon) =
        mapOf(
            "$radnummer-institusjon" to pensjon.institusjon,
            "$radnummer-pensjonstype" to pensjon.pensjonstype.nummer,
            "$radnummer-datoFoersteUtbetaling" to pensjon.datoFoersteUtbetaling,
            "$radnummer-bruttobeloep" to pensjon.bruttobeloep.let { it.verdi.toString() + " " + it.valuta.valuta },
            "$radnummer-grunnlagInnvilget" to pensjon.grunnlagInnvilget.nummer,
            "$radnummer-reduksjonsgrunnlag" to pensjon.reduksjonsgrunnlag?.nummer,
            "$radnummer-vurderingsperiode" to pensjon.vurderingsperiode.formater(),
            "$radnummer-adresseNyVurdering" to pensjon.adresseNyVurdering.formater(),
        )

    private fun settOppSide3(
        avslaattePensjoner: List<SamletMeldingOmPensjonsvedtakDto.AvslaattPensjon>,
        merger: PDFMergerUtility,
        target: PDDocument,
        startSide3: Int,
        antallSide3: Int,
        totaltAntallSider: Int,
        spraak: LanguageCode,
    ) = (0..<antallSide3).map { index ->
        ((index * RADER_PER_SIDE)..(index * RADER_PER_SIDE) + 4).map { radnummer ->
            avslaattePensjoner.getOrNull(radnummer)
                ?.let { pensjon -> flettInnAvslaattPensjon((radnummer % RADER_PER_SIDE) + 1, pensjon) }
                ?: emptyMap()
        }.let { flettefelt ->
            lesInnPDF("P1-side3.pdf", spraak).also {
                it.setValues(flettefelt.flatten().plus("page" to "${startSide3 + index + 1}/$totaltAntallSider"))
            }
        }
    }.forEach { merger.leggTilSide(target, it) }

    private fun flettInnAvslaattPensjon(radnummer: Int, pensjon: SamletMeldingOmPensjonsvedtakDto.AvslaattPensjon) =
        mapOf(
            "$radnummer-institusjon" to pensjon.institusjon,
            "$radnummer-pensjonstype" to pensjon.pensjonstype.nummer,
            "$radnummer-avslagsbegrunnelse" to pensjon.avslagsbegrunnelse.nummer,
            "$radnummer-vurderingsperiode" to pensjon.vurderingsperiode.formater(),
            "$radnummer-adresseNyVurdering" to pensjon.adresseNyVurdering.formater(),
        )

    private fun settOppSide4(
        institution: SamletMeldingOmPensjonsvedtakDto.Institusjon,
        merger: PDFMergerUtility,
        target: PDDocument,
        totaltAntallSider: Int,
        spraak: LanguageCode,
    ) =
        lesInnPDF("P1-side4.pdf", spraak)
            .also {
                it.setValues(
                    mapOf(
                        "navn" to institution.navn,
                        "adresselinje" to institution.adresselinje,
                        "poststed" to institution.poststed.value,
                        "postnummer" to institution.postnummer.value,
                        "landkode" to institution.landkode.landkode,
                        "institusjonsID" to institution.institusjonsID,
                        "faksnummer" to institution.faksnummer,
                        "telefonnummer" to institution.telefonnummer?.value,
                        "epost" to institution.epost.value,
                        "dato" to institution.dato.formater(),
                        "underskrift" to institution.underskrift,
                        "page" to "${totaltAntallSider}/${totaltAntallSider}"
                    )
                )
            }.also { merger.leggTilSide(target, it) }

    internal fun lesInnP1Vedlegg(spraak: LanguageCode) = lesInnPDF("P1-vedlegg.pdf", spraak)

    private fun lesInnPDF(filnavn: String, spraak: LanguageCode): PDDocument =
        PDDocument.load(javaClass.getResourceAsStream("/vedlegg/P1/${spraak.name}/$filnavn"))

    private fun SamletMeldingOmPensjonsvedtakDto.Adresse.formater() =
        listOfNotNull(adresselinje1, adresselinje2, adresselinje3).joinToString(System.lineSeparator()) +
                System.lineSeparator() + "${postnummer.value} ${poststed.value}" + System.lineSeparator() + landkode.landkode
}

internal fun List<Map<String, Any?>>.flatten(): Map<String, String?> =
    this.flatMap { it.entries }.associate { it.key to it.value?.toString() }

private fun LocalDate.formater() = dateFormatter(Language.English, FormatStyle.LONG).format(this)

private fun Period.formater() = this.toString() // TODO: Formater periode ordentleg