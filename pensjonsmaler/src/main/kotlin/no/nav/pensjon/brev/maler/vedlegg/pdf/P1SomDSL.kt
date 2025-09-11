package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.vedlegg.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private const val RADER_PER_SIDE = 5

fun P1Dto.somDSL() = PDFVedlegg.create {
    side("P1-1.pdf") {
        felt {
            // innehaver
            "holder-fornavn" to innehaver.fornavn
            "holder-etternavn" to innehaver.etternavn
            "holder-etternavnVedFoedsel" to innehaver.etternavnVedFoedsel
            "holder-foedselsdato" to formaterDato(innehaver.foedselsdato)
            "holder-adresselinje" to innehaver.adresselinje
            "holder-poststed" to innehaver.poststed?.value
            "holder-postnummer" to innehaver.postnummer?.value
            "holder-landkode" to innehaver.landkode?.landkode
            // forsikrede
            "insured-fornavn" to forsikrede.fornavn
            "insured-etternavn" to forsikrede.etternavn
            "insured-etternavnVedFoedsel" to forsikrede.etternavnVedFoedsel
            "insured-foedselsdato" to formaterDato(forsikrede.foedselsdato)
            "insured-adresselinje" to forsikrede.adresselinje
            "insured-poststed" to forsikrede.poststed?.value
            "insured-postnummer" to forsikrede.postnummer?.value
            "insured-landkode" to forsikrede.landkode?.landkode

            "kravMottattDato" to formaterDato(kravMottattDato)
            "sakstype" to mapOf(
                Language.Bokmal to sakstype.name,
                Language.English to sakstype.name,
            ) // TODO denne er vel for enkel
        }
    }

    innvilgedePensjoner.chunked(RADER_PER_SIDE) { side ->
        side("P1-2.pdf") {
            felt {
                add(side.mapIndexed { index, pensjon -> innvilgetPensjon(index + 1, pensjon) }.reduce { a, b -> a + b })
            }
        }
    }

    avslaattePensjoner.chunked(RADER_PER_SIDE) { side ->
        side("P1-3.pdf") {
            felt {
                add(side.mapIndexed { index, pensjon -> avslaattPensjon(index + 1, pensjon) }.reduce { a, b -> a + b })
            }
        }
    }

    side("P1-4.pdf") {
        felt {
            // utfyllende institusjon
            "institution-navn" to utfyllendeInstitusjon.navn
            "institution-adresselinje" to utfyllendeInstitusjon.adresselinje
            "institution-poststed" to utfyllendeInstitusjon.poststed.value
            "institution-postnummer" to utfyllendeInstitusjon.postnummer.value
            "institution-landkode" to utfyllendeInstitusjon.landkode.landkode
            "institution-institusjonsID" to utfyllendeInstitusjon.institusjonsID
            "institution-faksnummer" to utfyllendeInstitusjon.faksnummer
            "institution-telefonnummer" to utfyllendeInstitusjon.telefonnummer?.value
            "institution-epost" to utfyllendeInstitusjon.epost?.value
            "institution-dato" to formaterDato(utfyllendeInstitusjon.dato)
            "institution-underskrift" to ""
        }
    }

}

private fun formaterDato(dato: LocalDate?): Map<Language, String?> = mapOf(
    Language.Bokmal to dato?.formater(Language.Bokmal),
    Language.English to dato?.formater(Language.English)
)

private fun LocalDate.formater(language: Language): String? =
    when (language) {
        Language.Bokmal -> dateFormatter(LanguageCode.BOKMAL, FormatStyle.LONG).format(this)
        Language.English -> dateFormatter(LanguageCode.ENGLISH, FormatStyle.LONG).format(this)
        else -> null
    } // TODO: Denne bør vel liggje ein annan plass

fun dateFormatter(languageCode: LanguageCode, formatStyle: FormatStyle): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(languageCode.locale())

private fun P1Dto.Adresse.formater() =
    listOfNotNull(adresselinje1, adresselinje2, adresselinje3).joinToString(System.lineSeparator()) +
            System.lineSeparator() + "${postnummer.value} ${poststed.value}" + System.lineSeparator() + landkode.landkode

private fun innvilgetPensjon(radnummer: Int, pensjon: P1Dto.InnvilgetPensjon) =
    mapOf(
        "${radnummer}-institusjon" to pensjon.institusjon,
        "${radnummer}-pensjonstype" to pensjon.pensjonstype.nummer.toString(),
        "${radnummer}-datoFoersteUtbetaling" to formaterDato(pensjon.datoFoersteUtbetaling),
        "${radnummer}-bruttobeloep" to pensjon.bruttobeloep,
        "${radnummer}-grunnlagInnvilget" to pensjon.grunnlagInnvilget?.nummer?.toString(),
        "${radnummer}-reduksjonsgrunnlag" to pensjon.reduksjonsgrunnlag?.nummer?.toString(),
        "${radnummer}-vurderingsperiode" to pensjon.vurderingsperiode,
        "${radnummer}-adresseNyVurdering" to pensjon.adresseNyVurdering?.formater(),
    )


private fun avslaattPensjon(radnummer: Int, pensjon: P1Dto.AvslaattPensjon) = mapOf(
    "${radnummer}-institusjon" to pensjon.institusjon,
    "${radnummer}-pensjonstype" to pensjon.pensjonstype.nummer.toString(),
    "${radnummer}-avslagsbegrunnelse" to pensjon.avslagsbegrunnelse.nummer.toString(),
    "${radnummer}-vurderingsperiode" to pensjon.vurderingsperiode,
    "${radnummer}-adresseNyVurdering" to pensjon.adresseNyVurdering?.formater(),
)

fun LanguageCode.locale(): Locale =
    when (this) {
        LanguageCode.BOKMAL -> Locale.forLanguageTag("no")
        LanguageCode.NYNORSK -> Locale.forLanguageTag("no")
        LanguageCode.ENGLISH -> Locale.UK
    }