package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.brev.BrevLandmodell
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LanguageCode.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private const val RADER_PER_SIDE = 5

/**
 * Fyller ut det utfyllbare PDF-skjemaet P1 ("Samlet melding om pensjonsvedtak") med data fra [P1RedigerbarDto].
 *
 * ## Kobling mellom kode og PDF-ressurser
 * P1 består av flere separate PDF-filer (én per side), som ligger som AcroForm-skjemaer i
 * `src/main/resources/vedlegg/`:
 * - `P1-side1-BOKMAL.pdf` / `P1-side1-ENGLISH.pdf`
 * - `P1-side2-BOKMAL.pdf` / `P1-side2-ENGLISH.pdf`
 * - `P1-side3-BOKMAL.pdf` / `P1-side3-ENGLISH.pdf`
 * - `P1-side4-BOKMAL.pdf` / `P1-side4-ENGLISH.pdf`
 *
 * `side("P1-sideX") { ... }` under refererer til disse filene: [SideAppender.lesInnPDF] slår opp filnavnet
 * som `/vedlegg/P1-sideX-<SPRÅK>.pdf` (se `HentEllerOpprettPdfHandler.leggVedPDFVedlegg`), og laster inn riktig
 * språkvariant av PDF-en basert på brevets språk.
 *
 * Nøklene som brukes i `felt { "Feltnavn" to verdi }` (f.eks. `"Forenames[0]"`, `"Surname[1]"`,
 * `"Post_code[0]"`) er **ikke frie tekststrenger** – de må være identiske med `partialName` til de
 * utfyllbare AcroForm-feltene som er definert inne i de tilhørende PDF-filene over. [SideAppender] fyller
 * verdiene inn ved å iterere over `document.documentCatalog.acroForm.fieldIterator` og matche på nøyaktig
 * dette feltnavnet (se `SideAppender.fillFields`), eventuelt prefikset med `page_<index>_` når flere sider
 * slås sammen til ett dokument (`SideAppender.addPageFieldPrefix`/`pagePrefix`).
 *
 * Feltnavnene stammer fra det opprinnelige EU/EØS-skjemaet P1 (felles nordisk/europeisk pensjonsskjema),
 * og er derfor på engelsk selv i den norske PDF-filen (f.eks. `Forenames`, `Surname`, `Street_N`,
 * `Post_code`, `Country_code`, `Date_of_birth`, `Institution_awarding_the_pension`).
 * Radene i tabellene på side 2 og 3 ([innvilgetPensjon]/[avslaattPensjon]) bruker indekserte feltnavn
 * (`[radnummer]`) fordi PDF-en har ett sett med felt per rad, gjentatt [RADER_PER_SIDE] ganger per side.
 *
 * ### Ved endringer
 * Hvis feltnavn i denne filen endres, må de tilsvarende AcroForm-feltene i PDF-ressursene endres likt
 * (og omvendt) – ellers vil verdien stille forbli utfylt med tom streng, siden [SideAppender.fillFields]
 * kun matcher på eksakt feltnavn og ikke feiler dersom feltet mangler. Bruk et PDF-verktøy som kan vise/
 * redigere skjemafelt (f.eks. Adobe Acrobat "Prepare Form" eller et PDF-inspeksjonsverktøy som lister ut
 * `AcroForm`-feltene) for å verifisere feltnavn i PDF-ressursene før du endrer nøklene under.
 */
object P1pdfV2Dto {
    fun create(data: P1RedigerbarDto, felles: BrevbakerFelles): PDFVedlegg = PDFVedlegg().apply {
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
                                BOKMAL to mapSakstype(sakstype, P1Spraak.BOKMAL) + " til Nav",
                                ENGLISH to mapSakstype(sakstype, P1Spraak.ENGLISH) + " with Nav",
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
                    "Date[0]" to formaterDato(LocalDate.now())
                    "Signature[0]" to felles.signerendeSaksbehandlere?.saksbehandler
                }
            }
        }
    }

    private enum class P1Spraak { BOKMAL, ENGLISH }

    private fun mapSakstype(sakstype: Sakstype, language: P1Spraak): String? = when (sakstype.kode) {
        "AFP" -> "AFP"
        "AFP_PRIVAT" -> when(language) {
            P1Spraak.BOKMAL -> "AFP i privat sektor"
            P1Spraak.ENGLISH -> "contractual pension (AFP) in the private sector"
        }
        "ALDER" -> when(language) {
            P1Spraak.BOKMAL -> "alderspensjon"
            P1Spraak.ENGLISH -> "retirement pension"
        }
        "BARNEP" -> when(language) {
            P1Spraak.BOKMAL -> "barnepensjon"
            P1Spraak.ENGLISH -> "children’s pension"
        }
        "FAM_PL" -> when(language) {
            P1Spraak.BOKMAL -> "ytelse til tidligere familiepleier"
            P1Spraak.ENGLISH -> "previous family carers benefits"
        }
        "GJENLEV" -> when(language) {
            P1Spraak.BOKMAL -> "gjenlevendepensjon"
            P1Spraak.ENGLISH -> "survivor's pension"
        }
        "UFOREP" -> when(language) {
            P1Spraak.BOKMAL -> "uføretrygd"
            P1Spraak.ENGLISH -> "disability benefit"
        }

        else -> null
    }

    fun String.formaterLandkode(languageCode: LanguageCode): String? =
        // this her er egentlig Landkode (som string), bare ikke modellert som det
        takeIf { it.isNotEmpty() }?.let { BrevLandmodell.Landkode(this).let { BrevLandmodell.Landkoder.formaterLandnavn(it, languageCode) } }

    fun formaterDato(dato: LocalDate?): Map<LanguageCode, String?> = mapOf(
        BOKMAL to dato?.formater(BOKMAL),
        ENGLISH to dato?.formater(ENGLISH)
    )

    fun LocalDate.formater(language: LanguageCode): String? =
        dateFormatter(language).format(this)

    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    fun dateFormatter(languageCode: LanguageCode): DateTimeFormatter =
        dateFormatter.withLocale(languageCode.locale())

    fun joinAndSeparateByNotNullOrBlank(separator: String, vararg value: String?) =
        value.filterNotNull()
            .filter { it.isNotBlank() }
            .joinToString(separator)

    fun innvilgetPensjon(radnummer: Int, innvilgelse: P1RedigerbarDto.InnvilgetPensjon): Map<String, Any?> {
        return mapOf(
            "Institution_awarding_the_pension[$radnummer]" to innvilgelse.institusjon?.let { formatInstitusjon(it) },
            "Pensjonstype[$radnummer]" to innvilgelse.pensjonstype?.nummer?.toString()?.let { "[$it]" },
            "Date_of_first_payment[$radnummer]" to formaterDato(innvilgelse.datoFoersteUtbetaling),
            "Gross_amount[$radnummer]" to innvilgelse.utbetalt,
            "PensjonInnvilget[$radnummer]" to innvilgelse.grunnlagInnvilget?.nummer?.let { "[$it]" },
            "PensjonRedusert[$radnummer]" to innvilgelse.reduksjonsgrunnlag?.nummer?.let { "[$it]" },
            "Review_period[${radnummer * 2}]" to innvilgelse.vurderingsperiode,
            "Where_to_adress_the_request[$radnummer]" to innvilgelse.adresseNyVurdering,
        )
    }

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

            institusjon.datoForVedtak?.let { dato ->
                val formattertDato = dato.format(dateFormatter.withLocale(languageCode.locale()))
                datoForVedtaketTekst(languageCode, formattertDato)
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

}