package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.model.SakstypeNavn
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.vedlegg.createAttachmentPDF
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import org.slf4j.LoggerFactory
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
) { data, felles ->
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
                            BOKMAL to SakstypeNavn.apply(sakstype, Language.Bokmal) + " til Nav",
                            ENGLISH to SakstypeNavn.apply(sakstype, Language.English) + " with Nav",
                        )
            }
        }

        innvilgedePensjoner.chunked(RADER_PER_SIDE) { side ->
            side("P1-side2") {
                felt {
                    add(side.mapIndexed { index, pensjon -> innvilgetPensjon(index, pensjon) }
                        .reduce { a, b -> a + b })
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
                    add(side.mapIndexed { index, pensjon -> avslaattPensjon(index, pensjon) }
                        .reduce { a, b -> a + b })
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
        BOKMAL to country.getDisplayCountry(Language.Bokmal.locale()),
        ENGLISH to country.getDisplayCountry(Language.English.locale())
    )
}

private fun formaterDato(dato: LocalDate?): Map<LanguageCode, String?> = mapOf(
    BOKMAL to dato?.formater(BOKMAL),
    ENGLISH to dato?.formater(ENGLISH)
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

val vurderingsperiodeSeksUker = mapOf(
    BOKMAL to "6 uker fra samlet melding om pensjons-vedtak er mottatt.",
    ENGLISH to "6 weeks from Summary of Pension Decisions is received.",
)

val adresseNyVurderingNorge = mapOf(
    BOKMAL to "The Norwegian Labour and Welfare Administration\nPostboks 6600 Etterstad\n0607 Oslo\nNorge",
    ENGLISH to "Nav familie- og pensjonsytelser\nPostboks 6600 Etterstad\n0607 Oslo\nNorway",
)

val seTidligereVedtakFraLand: Map<LanguageCode, String> = mapOf(
    BOKMAL to "Se tidligere vedtak fra gjeldende land",
    ENGLISH to "See previous decisions from the relevant country",
)

private fun innvilgetPensjon(radnummer: Int, innvilgelse: P1Dto.InnvilgetPensjon): Map<String, Any?> {
    val erNorskRad = innvilgelse.erNorskRad ?: false
    return mapOf(
        "Institution_awarding_the_pension[$radnummer]" to formatInstitusjon(
            innvilgelse.institusjon,
            innvilgelse.vedtaksdato,
            erNorskRad
        ),
        "Pensjonstype[$radnummer]" to innvilgelse.pensjonstype?.nummer?.toString()?.let { "[$it]" },
        "Date_of_first_payment[$radnummer]" to formaterDato(innvilgelse.datoFoersteUtbetaling),
        "Gross_amount[$radnummer]" to formaterValuta(
            innvilgelse.bruttobeloepDesimal,
            innvilgelse.valuta,
            innvilgelse.utbetalingsHyppighet
        ),
        "PensjonInnvilget[$radnummer]" to innvilgelse.grunnlagInnvilget?.nummer?.let { "[$it]" },
        "PensjonRedusert[$radnummer]" to innvilgelse.reduksjonsgrunnlag?.nummer?.let { "[$it]" },
        "Review_period[${radnummer * 2}]" to vurderingsperiode(erNorskRad, innvilgelse.vurderingsperiode),
        "Where_to_adress_the_request[$radnummer]" to
                adresseNyVurdering(erNorskRad, innvilgelse.adresseNyVurdering),
    )
}

private fun vurderingsperiode(
    erNorskRad: Boolean,
    vurderingsperiode: String?
): Any? = if (erNorskRad) {
    vurderingsperiodeSeksUker
} else vurderingsperiode ?: seTidligereVedtakFraLand

private fun formatInstitusjon(
    institusjoner: List<P1Dto.Institusjon>,
    vedtaksdato: String?,
    erNorskRad: Boolean,
): Map<LanguageCode, String?> =
    mapOf(
        BOKMAL to formatInstitusjon(institusjoner, vedtaksdato, BOKMAL, erNorskRad),
        ENGLISH to formatInstitusjon(institusjoner, vedtaksdato, ENGLISH, erNorskRad)
    )

private fun formatInstitusjon(
    institusjoner: List<P1Dto.Institusjon>,
    vedtaksdato: String?,
    languageCode: LanguageCode,
    erNorskRad: Boolean,
): String =
    institusjoner.joinToString(System.lineSeparator()) { institusjon ->
        val bokmaal = languageCode == BOKMAL
        joinAndSeparateByNotNull(
            separator = System.lineSeparator(),
            institusjon.land?.formaterLandkode(languageCode)
                ?.let { if (bokmaal) "Land: $it" else "Country: $it" },

            if (erNorskRad) {
                if (bokmaal) "Nav" else "The Norwegian Labour and Welfare Administration"
            } else {
                institusjon.institusjonsnavn
            },

            institusjon.pin?.takeIf { it.isNotBlank() }?.let { "PIN: $it" },

            institusjon.saksnummer?.takeIf { it.isNotBlank() }
                ?.let { if (bokmaal) "Saksnummer: $it" else "Case number: $it" },

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
): String = if (languageCode == BOKMAL) {
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
            BOKMAL to "$beloepDesimal $valuta\n" +
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
            ENGLISH to "$valuta ${beloepDesimal}\n" +
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


private fun avslaattPensjon(radnummer: Int, avslag: P1Dto.AvslaattPensjon): Map<String, Any?> {
    val erNorskAvslag = avslag.erNorskRad()
    return mapOf(
        "Institution_rejecting_the_pension[$radnummer]" to
                avslag.institusjoner?.let {
                    formatInstitusjon(it, avslag.vedtaksdato, erNorskAvslag)
                },

        "Pensjonstype[$radnummer]" to avslag.pensjonstype?.nummer?.let { "[$it]" },

        "GrunnlagAvslag[$radnummer]" to avslag.avslagsbegrunnelse?.nummer?.let { "[$it]" },

        "Review_period[${radnummer * 2}]" to
                vurderingsperiode(erNorskAvslag, avslag.vurderingsperiode),

        "Where_to_adress_the_request[$radnummer]" to
                adresseNyVurdering(erNorskAvslag, avslag.adresseNyVurdering),
    )
}

private fun adresseNyVurdering(erNorge: Boolean, adresser: List<P1Dto.Adresse>): Map<LanguageCode, String?> =
    if (erNorge) {
        adresseNyVurderingNorge
    } else if (adresser.isNotEmpty()) {
        adresser.formater()
    } else {
        seTidligereVedtakFraLand
    }

fun P1Dto.AvslaattPensjon.erNorskRad(): Boolean {
    val norskInst = institusjoner?.all { it.land?.trim()?.uppercase() == "NO" }?: false
    val norskAdresse = adresseNyVurdering.all { it.landkode?.landkode?.trim()?.uppercase() == "NO" }
    return (norskInst && norskAdresse)
            || norskInst && adresseNyVurdering.isEmpty()
            || norskAdresse && (institusjoner?.isEmpty() ?: false)
}

private fun List<P1Dto.Adresse>.formater(): Map<LanguageCode, String?> =
    mapOf(
        BOKMAL to this.joinToString(System.lineSeparator()) {
            it.formater(BOKMAL)
        },
        ENGLISH to this.joinToString(System.lineSeparator()) {
            it.formater(ENGLISH)
        },
    )

fun LanguageCode.locale(): Locale =
    when (this) {
        BOKMAL -> Locale.forLanguageTag("no")
        NYNORSK -> Locale.forLanguageTag("no")
        ENGLISH -> Locale.UK
    }