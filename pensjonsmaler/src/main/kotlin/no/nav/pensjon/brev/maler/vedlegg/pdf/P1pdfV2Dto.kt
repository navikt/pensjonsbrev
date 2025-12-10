package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.pensjon.brev.api.model.maler.P1RedigerbarDto
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
object P1pdfV2Dto {
    val p1Vedlegg = createAttachmentPDF<LangBokmalEnglish, P1RedigerbarDto>(
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

    fun String.formaterLandkode(languageCode: LanguageCode): String? =
        this.formaterLandkode()[languageCode]

    fun String.formaterLandkode(): Map<LanguageCode, String?> {
        val country = Locale.of(this, this)
        return mapOf(
            BOKMAL to country.getDisplayCountry(Language.Bokmal.locale()),
            ENGLISH to country.getDisplayCountry(Language.English.locale())
        )
    }

    fun formaterDato(dato: LocalDate?): Map<LanguageCode, String?> = mapOf(
        BOKMAL to dato?.formater(BOKMAL),
        ENGLISH to dato?.formater(ENGLISH)
    )

    fun LocalDate.formater(language: LanguageCode): String? =
        dateFormatter(language).format(this)

    val logger = LoggerFactory.getLogger(P1pdfV2Dto::class.java)

    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    fun dateFormatter(languageCode: LanguageCode): DateTimeFormatter =
        dateFormatter.withLocale(languageCode.locale())

    fun joinAndSeparateByNotNullOrBlank(separator: String, vararg value: String?) =
        value.filterNotNull()
            .filter { it.isNotBlank() }
            .joinToString(separator)

    val vurderingsperiodeSeksUker = mapOf(
        BOKMAL to "6 uker fra samlet melding om pensjons-vedtak er mottatt.",
        ENGLISH to "6 weeks from Summary of Pension Decisions is received.",
    )

    val seTidligereVedtakFraLand: Map<LanguageCode, String> = mapOf(
        BOKMAL to "Se tidligere vedtak fra gjeldende land",
        ENGLISH to "See previous decisions from the relevant country",
    )

    fun innvilgetPensjon(radnummer: Int, innvilgelse: P1RedigerbarDto.InnvilgetPensjon): Map<String, Any?> {
        return mapOf(
            "Institution_awarding_the_pension[$radnummer]" to innvilgelse.institusjon?.let { formatInstitusjon(it) },
            "Pensjonstype[$radnummer]" to innvilgelse.pensjonstype?.nummer?.toString()?.let { "[$it]" },
            "Date_of_first_payment[$radnummer]" to innvilgelse.datoFoersteUtbetaling,
            "Gross_amount[$radnummer]" to innvilgelse.utbetalt,
            "PensjonInnvilget[$radnummer]" to innvilgelse.grunnlagInnvilget?.nummer?.let { "[$it]" },
            "PensjonRedusert[$radnummer]" to innvilgelse.reduksjonsgrunnlag?.nummer?.let { "[$it]" },
            "Review_period[${radnummer * 2}]" to innvilgelse.vurderingsperiode,
            "Where_to_adress_the_request[$radnummer]" to innvilgelse.adresseNyVurdering,
        )
    }

    fun vurderingsperiode(
        erNorskRad: Boolean,
        vurderingsperiode: String?
    ): Any? = if (erNorskRad) {
        vurderingsperiodeSeksUker
    } else vurderingsperiode ?: seTidligereVedtakFraLand

    fun formatInstitusjon(
        institusjon: P1RedigerbarDto.Institusjon,
    ): Map<LanguageCode, String?> =
        mapOf(
            BOKMAL to formatInstitusjon(institusjon, BOKMAL),
            ENGLISH to formatInstitusjon(institusjon, ENGLISH)
        )

    fun formatInstitusjon(
        institusjon: P1RedigerbarDto.Institusjon,
        languageCode: LanguageCode,
    ): String {
        val bokmaal = languageCode == BOKMAL
        return joinAndSeparateByNotNullOrBlank(
            separator = System.lineSeparator(),

            institusjon.land?.formaterLandkode(languageCode)
                ?.let { if (bokmaal) "Land: $it" else "Country: $it" },

            institusjon.institusjonsnavn,
            institusjon.pin?.let { "PIN: $it" },
            institusjon.saksnummer?.let { if (bokmaal) "Saksnummer: $it" else "Case number: $it" },

            institusjon.vedtaksdato?.let { dato ->
                try {
                    val formattertDato =
                        LocalDate.parse(dato).format(dateFormatter.withLocale(languageCode.locale()))
                    datoForVedtaketTekst(languageCode, formattertDato)
                } catch (e: Exception) {
                    logger.warn("Could not parse vedtaksdato: $dato", e)
                    datoForVedtaketTekst(languageCode, dato)
                }
            },
        )
    }

    fun datoForVedtaketTekst(
        languageCode: LanguageCode,
        formattertDato: String?
    ): String = if (languageCode == BOKMAL) {
        "Dato for vedtaket: $formattertDato"
    } else {
        "Date of the decision: $formattertDato"
    }

    fun avslaattPensjon(radnummer: Int, avslag: P1RedigerbarDto.AvslaattPensjon): Map<String, Any?> {
        return mapOf(
            "Institution_rejecting_the_pension[$radnummer]" to
                    avslag.institusjon?.let {
                        formatInstitusjon(it)
                    },

            "Pensjonstype[$radnummer]" to avslag.pensjonstype?.nummer?.let { "[$it]" },

            "GrunnlagAvslag[$radnummer]" to avslag.avslagsbegrunnelse?.nummer?.let { "[$it]" },

            "Review_period[${radnummer * 2}]" to avslag.vurderingsperiode,

            "Where_to_adress_the_request[$radnummer]" to avslag.adresseNyVurdering,
        )
    }

    fun LanguageCode.locale(): Locale =
        when (this) {
            BOKMAL -> Locale.forLanguageTag("no")
            NYNORSK -> Locale.forLanguageTag("no")
            ENGLISH -> Locale.UK
        }
}