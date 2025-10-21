package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.model.SakstypeNavn
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.vedlegg.createAttachmentPDF
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

private const val RADER_PER_SIDE = 5

val p1Vedlegg = createAttachmentPDF<LangBokmalEnglish, P1Dto, SamletMeldingOmPensjonsvedtakDto.SaksbehandlerValg>(
    title = listOf(
        newText(
            Language.Bokmal to "P1 – Samlet melding om pensjonsvedtak",
            Language.English to "P1 – Summary of Pension Decisions"
        )
    )
) { data, felles, saksbehandlerValg ->
    with(data) {

        side("P1-side1") {
            felt {
                // innehaver
                "Forenames[0]" to innehaver.fornavn
                "Surname[0]" to innehaver.etternavn
                "Surname_at_birth[0]" to innehaver.etternavnVedFoedsel
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
                "Name_of_the_institution[0]" to
                        mapOf(
                            LanguageCode.BOKMAL to SakstypeNavn.apply(sakstype, Language.Bokmal) + " til Nav",
                            LanguageCode.ENGLISH to SakstypeNavn.apply(sakstype, Language.English) + " with Nav",
                        )
            }
        }

        innvilgedePensjoner.chunked(RADER_PER_SIDE) { side ->
            side("P1-side2") {
                felt {
                    if (!saksbehandlerValg.toemRaderFraEessi) {
                        add(side.mapIndexed { index, pensjon -> innvilgetPensjon(index, pensjon) }
                            .reduce { a, b -> a + b })
                    }
                }
            }
        }
        if (innvilgedePensjoner.isEmpty()) {
            side("P1-side2") {
                felt {

                }
            }
        }

        avslaattePensjoner.chunked(RADER_PER_SIDE) { side ->
            side("P1-side3") {
                felt {
                    if (!saksbehandlerValg.toemRaderFraEessi) {
                        add(side.mapIndexed { index, pensjon -> avslaattPensjon(index, pensjon) }
                            .reduce { a, b -> a + b })
                    }
                }
            }
        }
        if (avslaattePensjoner.isEmpty()) {
            side("P1-side3") {
                felt {

                }
            }
        }

        side("P1-side4") {
            felt {
                // utfyllende institusjon
                "Name[0]" to utfyllendeInstitusjon.navn
                "Street_N[0]" to utfyllendeInstitusjon.adresselinje
                "Town[0]" to utfyllendeInstitusjon.poststed.value
                "Post_code[0]" to utfyllendeInstitusjon.postnummer.value
                "Country_code[0]" to utfyllendeInstitusjon.landkode.landkode
                "Office_phone_N[0]" to utfyllendeInstitusjon.telefonnummer?.value
                "Date[0]" to formaterDato(utfyllendeInstitusjon.dato)
                "Signature[0]" to felles.signerendeSaksbehandlere?.saksbehandler
            }
        }
    }
}

private fun String.formaterLandkode(languageCode: LanguageCode): String? =
    this.formaterLandkode()[languageCode]

private fun String.formaterLandkode(): Map<LanguageCode, String?> {
    val country = Locale.of(this, this)
    return mapOf(
        LanguageCode.BOKMAL to country.getDisplayCountry(Language.Bokmal.locale()),
        LanguageCode.ENGLISH to country.getDisplayCountry(Language.English.locale())
    )
}

private fun formaterDato(dato: LocalDate?): Map<LanguageCode, String?> = mapOf(
    LanguageCode.BOKMAL to dato?.formater(LanguageCode.BOKMAL),
    LanguageCode.ENGLISH to dato?.formater(LanguageCode.ENGLISH)
)

private fun LocalDate.formater(language: LanguageCode): String? =
    dateFormatter(language).format(this)

private val logger = LoggerFactory.getLogger("P1PDFDto")

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

fun dateFormatter(languageCode: LanguageCode): DateTimeFormatter =
    dateFormatter.withLocale(languageCode.locale())

private fun P1Dto.Adresse.formater(languageCode: LanguageCode) =
    joinAndSeparateByNotNull(
        separator = System.lineSeparator(),
        adresselinje1,
        adresselinje2,
        adresselinje3,
        joinAndSeparateByNotNull(separator = " ", postnummer?.value, poststed?.value),
        landkode?.landkode?.formaterLandkode(languageCode)
    )

fun joinAndSeparateByNotNull(separator: String, vararg value: String?) =
    value.filterNotNull()
        .filter { it.isNotBlank() }
        .joinToString(separator)

private fun innvilgetPensjon(radnummer: Int, pensjon: P1Dto.InnvilgetPensjon) =
    mapOf(
        "Institution_awarding_the_pension[$radnummer]" to formatInstitusjon(pensjon.institusjon, pensjon.vedtaksdato),
        "Type_of_pension[$radnummer]" to "[${pensjon.pensjonstype?.nummer.toString()}]",
        "Date_of_first_payment[$radnummer]" to formaterDato(pensjon.datoFoersteUtbetaling),
        "Gross_amount[$radnummer]" to formaterValuta(
            pensjon.bruttobeloepDesimal,
            pensjon.valuta,
            pensjon.utbetalingsHyppighet
        ),
        "Pension_has_been_awarded[$radnummer]" to pensjon.grunnlagInnvilget?.nummer?.let { "[$it]" },
        "Pension_has_been_reduced[$radnummer]" to pensjon.reduksjonsgrunnlag?.nummer?.let { "[$it]" },
        "Review_period[${radnummer * 2}]" to mapOf(
            LanguageCode.BOKMAL to "6 uker fra samlet melding om pensjons-vedtak er mottatt.",
            LanguageCode.ENGLISH to "6 weeks from Summary of Pension Decisions is received.",
        ),
        "Where_to_adress_the_request[$radnummer]" to pensjon.adresseNyVurdering.formater(),
    )

private fun formatInstitusjon(
    institusjoner: List<P1Dto.Institusjon>,
    vedtaksdato: String?
): Map<LanguageCode, String?> =
    mapOf(
        LanguageCode.BOKMAL to formatInstitusjon(institusjoner, vedtaksdato, LanguageCode.BOKMAL),
        LanguageCode.ENGLISH to formatInstitusjon(institusjoner, vedtaksdato, LanguageCode.ENGLISH)
    )

private fun formatInstitusjon(
    institusjoner: List<P1Dto.Institusjon>,
    vedtaksdato: String?,
    languageCode: LanguageCode
): String =
    institusjoner.joinToString(System.lineSeparator()) { institusjon ->
        joinAndSeparateByNotNull(
            System.lineSeparator(),
            institusjon.institusjonsnavn,
            institusjon.pin?.takeIf { it.isNotBlank() }?.let { "PIN: $it" },
            institusjon.saksnummer?.takeIf { it.isNotBlank() }?.let {
                if (languageCode == LanguageCode.BOKMAL) {
                    "Saksnummer: $it"
                } else {
                    "Case number: $it"
                }
            },
            vedtaksdato?.let { dato ->
                try {
                    val formattertDato = LocalDate.parse(dato).format(dateFormatter.withLocale(languageCode.locale()))
                    datoForVedtaketTekst(languageCode, formattertDato)
                } catch (e: Exception) {
                    logger.warn("Could not parse vedtaksdato: $dato", e)
                    datoForVedtaketTekst(languageCode, dato)
                }
            },
        )
    }

private fun datoForVedtaketTekst(
    languageCode: LanguageCode,
    formattertDato: String?
): String = if (languageCode == LanguageCode.BOKMAL) {
    "Dato for vedtaket: $formattertDato"
} else {
    "Date of the decision: $formattertDato"
}

private fun formaterValuta(
    beloepDesimal: String?,
    valuta: String?,
    utbetalingsHyppighet: P1Dto.Utbetalingshyppighet?
): Map<LanguageCode, String>? {
    return if (beloepDesimal != null && valuta != null) {
        return mapOf(
            LanguageCode.BOKMAL to "$beloepDesimal $valuta\n" +
                    when (utbetalingsHyppighet) {
                        P1Dto.Utbetalingshyppighet.Aarlig -> "Årlig"
                        P1Dto.Utbetalingshyppighet.Kvartalsvis -> "Kvartalvis"
                        P1Dto.Utbetalingshyppighet.Maaned12PerAar -> "Månedlig (12 per år)"
                        P1Dto.Utbetalingshyppighet.Maaned13PerAar -> "Månedlig (13 per år)"
                        P1Dto.Utbetalingshyppighet.Maaned14PerAar -> "Månedlig (14 per år)"
                        P1Dto.Utbetalingshyppighet.Ukentlig -> "Ukentlig"
                        P1Dto.Utbetalingshyppighet.UkjentSeVedtak -> "Ukjent, se vedtak"
                        null -> ""
                    },
            LanguageCode.ENGLISH to "$valuta ${beloepDesimal}\n" +
                    when (utbetalingsHyppighet) {
                        P1Dto.Utbetalingshyppighet.Aarlig -> "Yearly"
                        P1Dto.Utbetalingshyppighet.Kvartalsvis -> "Quarterly"
                        P1Dto.Utbetalingshyppighet.Maaned12PerAar -> "Monthly (12/year)"
                        P1Dto.Utbetalingshyppighet.Maaned13PerAar -> "Monthly (13/year)"
                        P1Dto.Utbetalingshyppighet.Maaned14PerAar -> "Monthly (14/year)"
                        P1Dto.Utbetalingshyppighet.Ukentlig -> "Weekly"
                        P1Dto.Utbetalingshyppighet.UkjentSeVedtak -> "Unknown, see decision"
                        null -> ""
                    },
        )
    } else null
}


private fun avslaattPensjon(radnummer: Int, pensjon: P1Dto.AvslaattPensjon) = mapOf(
    "Institution_rejecting_the_pension[$radnummer]" to
            (pensjon.institusjoner ?: pensjon.institusjon?.let { listOf(it) })?.let {
        formatInstitusjon(
            it,
            pensjon.vedtaksdato
        )
    },
    "Type_of_pension[$radnummer]" to pensjon.pensjonstype?.nummer?.let { "[$it]" },
    "Reasons_fro_the_rejection[$radnummer]" to pensjon.avslagsbegrunnelse?.nummer?.let { "[$it]" },
    "Review_period[${radnummer * 2}]" to pensjon.vurderingsperiode,
    "Where_to_adress_the_request[$radnummer]" to pensjon.adresseNyVurdering.formater(),
)

private fun List<P1Dto.Adresse>.formater(): Map<LanguageCode, String?> =
    mapOf(
        LanguageCode.BOKMAL to this.joinToString(System.lineSeparator()) {
            it.formater(LanguageCode.BOKMAL)
        },
        LanguageCode.ENGLISH to this.joinToString(System.lineSeparator()) {
            it.formater(LanguageCode.ENGLISH)
        },
    )

fun LanguageCode.locale(): Locale =
    when (this) {
        LanguageCode.BOKMAL -> Locale.forLanguageTag("no")
        LanguageCode.NYNORSK -> Locale.forLanguageTag("no")
        LanguageCode.ENGLISH -> Locale.UK
    }