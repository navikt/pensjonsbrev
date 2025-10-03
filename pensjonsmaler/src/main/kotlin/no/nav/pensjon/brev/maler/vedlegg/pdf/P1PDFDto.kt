package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.vedlegg.createAttachmentPDF
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private const val RADER_PER_SIDE = 5

val p1Vedlegg = createAttachmentPDF<LangBokmalEnglish, P1Dto>(
    title = listOf(
        newText(
            Language.Bokmal to "P1 – Samlet melding om pensjonsvedtak",
            Language.English to "P1 – Summary of Pension Decisions"
        )
    )
) { data ->
    with(data) {

        side("p1-side1") {
            felt {
                // innehaver
                "Forenames[0]" to innehaver.fornavn
                "Surname[0]" to innehaver.etternavn
                "Surname_at_birth[0]" to innehaver.etternavnVedFoedsel
                "holder-foedselsdato" to formaterDato(innehaver.foedselsdato) // TODO hvor er den?
                "Street_N[0]" to innehaver.adresselinje
                "Town[0]" to innehaver.poststed?.value
                "Post_code[0]" to innehaver.postnummer?.value
                "Country_code[0]" to innehaver.landkode?.landkode
                // forsikrede
                "Forenames[1]" to forsikrede.fornavn
                "Surname[1]" to forsikrede.etternavn
                "Surname_at_birth[1]" to forsikrede.etternavnVedFoedsel
                "Date_of_birth[0]" to formaterDato(forsikrede.foedselsdato)
                "Street_N[1]" to forsikrede.adresselinje
                "Town[1]" to forsikrede.poststed?.value
                "Post_code[1]" to forsikrede.postnummer?.value
                "Country_code[1]" to forsikrede.landkode?.landkode

                "kravMottattDato" to formaterDato(kravMottattDato)
                "sakstype" to mapOf(
                    Language.Bokmal to sakstype.name,
                    Language.English to sakstype.name,
                ) // TODO denne er vel for enkel
            }
        }

        innvilgedePensjoner.chunked(RADER_PER_SIDE) { side ->
            side("p1-side2") {
                felt {
                    add(side.mapIndexed { index, pensjon -> innvilgetPensjon(index, pensjon) }
                        .reduce { a, b -> a + b })
                }
            }
        }

        avslaattePensjoner.chunked(RADER_PER_SIDE) { side ->
            side("p1-side3") {
                felt {
                    add(side.mapIndexed { index, pensjon -> avslaattPensjon(index, pensjon) }
                        .reduce { a, b -> a + b })
                }
            }
        }

        side("p1-side4") {
            felt {
                // utfyllende institusjon
                "Name[0]" to utfyllendeInstitusjon.navn
                "Street_N[0]" to utfyllendeInstitusjon.adresselinje
                "Town[0]" to utfyllendeInstitusjon.poststed.value
                "Post_code[0]" to utfyllendeInstitusjon.postnummer.value
                "Country_code[0]" to utfyllendeInstitusjon.landkode.landkode
                "Institution_ID[0]" to utfyllendeInstitusjon.institusjonsID
                "Office_fax_N[0]" to utfyllendeInstitusjon.faksnummer
                "Office_phone_N[0]" to utfyllendeInstitusjon.telefonnummer?.value
                "E-mail[0]" to utfyllendeInstitusjon.epost?.value
                "Date[0]" to formaterDato(utfyllendeInstitusjon.dato)
                "Signature[0]" to "" // TODO
            }
        }
    }
}

private fun formaterDato(dato: LocalDate?): Map<LanguageCode, String?> = mapOf(
    LanguageCode.BOKMAL to dato?.formater(Language.Bokmal),
    LanguageCode.ENGLISH to dato?.formater(Language.English)
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
        "Institution_awarding_the_pension[$radnummer]" to pensjon.institusjon.mapNotNull { it.institusjonsnavn }.joinToString(", "),
        "Type_of_pension[$radnummer]" to "[${pensjon.pensjonstype?.nummer.toString()}]",
        "Date_of_first_payment[$radnummer]" to formaterDato(pensjon.datoFoersteUtbetaling),
        "Gross_amount[$radnummer]" to pensjon.bruttobeloep,
        "Pension_has_been_awarded[$radnummer]" to pensjon.grunnlagInnvilget?.nummer?.let { "[$it]"},
        "Pension_has_been_reduced[$radnummer]" to pensjon.reduksjonsgrunnlag?.nummer?.let { "[$it]"},
        "Review_period[${radnummer*2}]" to pensjon.vurderingsperiode, // TODO skal egentlig være start og slutt for vurderingsperiode...
        "Review_period[${(radnummer*2)+1}]" to pensjon.vurderingsperiode,
        "Where_to_adress_the_request[$radnummer]" to pensjon.adresseNyVurdering.joinToString("\n") { it.formater() },
    )


private fun avslaattPensjon(radnummer: Int, pensjon: P1Dto.AvslaattPensjon) = mapOf(
    "Institution_awarding_the_pension[$radnummer]" to pensjon.institusjon,
    "Type_of_pension[$radnummer]" to pensjon.pensjonstype?.nummer?.let { "[$it]" },
    // Ja Reasons_fro er riktig
    "Reasons_fro_the_rejection[$radnummer]" to pensjon.avslagsbegrunnelse?.nummer?.let { "[$it]" },
    "Review_period[${radnummer*2}]" to pensjon.vurderingsperiode, // TODO skal egentlig være start og slutt for vurderingsperiode...
    "Review_period[${(radnummer*2)+1}]" to pensjon.vurderingsperiode,
    "Where_to_adress_the_request[$radnummer]" to pensjon.adresseNyVurdering.joinToString("\n") { it.formater() },
)

fun LanguageCode.locale(): Locale =
    when (this) {
        LanguageCode.BOKMAL -> Locale.forLanguageTag("no")
        LanguageCode.NYNORSK -> Locale.forLanguageTag("no")
        LanguageCode.ENGLISH -> Locale.UK
    }