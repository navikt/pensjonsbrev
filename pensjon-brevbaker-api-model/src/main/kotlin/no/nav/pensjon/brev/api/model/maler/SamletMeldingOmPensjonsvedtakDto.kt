package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.Landkode
import no.nav.brev.brevbaker.vedlegg.PDFVedlegg
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.vedlegg.Vedleggtyper
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.to

data class SamletMeldingOmPensjonsvedtakDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, SamletMeldingOmPensjonsvedtakDto.PesysData> {
    data class PesysData(
        val sakstype: Sakstype,
        val vedlegg: P1Dto,
    ) : BrevbakerBrevdata
}

data class P1Dto(
    val innehaver: P1Person,
    val forsikrede: P1Person,
    val sakstype: Sakstype,
    val kravMottattDato: LocalDate?,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val utfyllendeInstitusjon: Institusjon, // I praksis Nav eller Nav-enheten
) : BrevbakerBrevdata, PDFVedleggData {
    override val tittel = Vedleggtyper.P1.tittel

    data class P1Person(
        val fornavn: String,
        val etternavn: String,
        val etternavnVedFoedsel: String,
        val foedselsdato: LocalDate?,
        val adresselinje: String?,
        val poststed: Poststed?,
        val postnummer: Postnummer?,
        val landkode: Landkode?,
    )

    data class InnvilgetPensjon(
        val institusjon: String,
        val pensjonstype: Pensjonstype,
        val datoFoersteUtbetaling: LocalDate,
        val bruttobeloep: Int,
        val grunnlagInnvilget: GrunnlagInnvilget?,
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
        val vurderingsperiode: String,
        val adresseNyVurdering: Adresse?,
    )

    data class AvslaattPensjon(
        val institusjon: String,
        val pensjonstype: Pensjonstype,
        val avslagsbegrunnelse: Avslagsbegrunnelse,
        val vurderingsperiode: String,
        val adresseNyVurdering: Adresse?,
    )

    enum class Pensjonstype(val nummer: Int, val fullTekst: String) {
        Alder(1, "Old-age"),
        Ufoere(2, "Invalidity"),
        Etterlatte(3, "Survivor")
    }

    enum class GrunnlagInnvilget(val nummer: Int, val fullTekst: String) {
        IHenholdTilNasjonalLovgivning(4, "according to national legislation"),
        ProRata(
            5,
            "as a pension in which periods from another Member State have been\n" +
                    "taken into account (European pro rata calculation)"
        ),
        MindreEnnEttAar(
            6,
            "as a pension in which periods of less than one year have been taken\n" +
                    "into account as if they had been completed under the legislation of\n" +
                    "this Member State"
        )
    }

    enum class Reduksjonsgrunnlag(val nummer: Int, val fullTekst: String) {
        PaaGrunnAvAndreYtelserEllerAnnenInntekt(7, "in view of another benefit or income"),
        PaaGrunnAvOverlappendeGodskrevnePerioder(8, "in view of overlapping of credited periods")
    }

    enum class Avslagsbegrunnelse(val nummer: Int, val fullTekst: String) {
        IngenOpptjeningsperioder(4, "No insurance periods"),
        OpptjeningsperiodePaaMindreEnnEttAar(5, "Insurance periods less than one year"),
        KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt(
            6,
            "qualifying period not completed or eligibility requirements not met"
        ),
        VilkaarOmUfoerhetErIkkeOppfylt(7, "no partial disability or invalidity was found"),
        InntektstakErOverskredet(8, "income ceiling is exceeded"),
        PensjonsalderErIkkeNaadd(9, "pension age not yet reached"),
        AndreAarsaker(10, "other reasons")
    }

    data class Adresse(
        val adresselinje1: String,
        val adresselinje2: String?,
        val adresselinje3: String?,
        val landkode: Landkode,
        val postnummer: Postnummer,
        val poststed: Poststed,
    )

    data class Institusjon(
        val navn: String,
        val adresselinje: String,
        val poststed: Poststed,
        val postnummer: Postnummer,
        val landkode: Landkode,
        val institusjonsID: String?,
        val faksnummer: String?,
        val telefonnummer: Telefonnummer?,
        val epost: Epost?,
        val dato: LocalDate,
    )

    @JvmInline
    value class Postnummer(val value: String) {
        init {
            require(value.length < 30) { "Postnumre er jo ikke kjempelange. $value er ${value.length} lang." }
        }
    }

    @JvmInline
    value class Poststed(val value: String) {
        init {
            require(value.length < 300) { "Poststed er ikke kjempelange. $value er ${value.length} lang." }
        }
    }

    @JvmInline
    value class Epost(val value: String) {
        init {
            require(value.contains("@")) { "Epost må inneholde @" }
            require(value.contains(".")) { "Epost må inneholde ." }
            require(value.substringBefore("@").isNotEmpty()) { "Epost må ha verdi før @" }
            require(value.substringAfter("@").isNotEmpty()) { "Epost må ha verdi etter @" }
            require(value.substringBefore(".").isNotEmpty()) { "Epost må ha verdi før ." }
            require(value.substringAfter("@").isNotEmpty()) { "Epost må ha verdi etter ." }
        }
    }

    override fun somPDFVedlegg() = PDFVedlegg.create {
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
                    LanguageCode.BOKMAL to sakstype.name,
                    LanguageCode.ENGLISH to sakstype.name,
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

    private fun formaterDato(dato: LocalDate?): Map<LanguageCode, String?> = mapOf(
        LanguageCode.BOKMAL to dato?.formater(LanguageCode.BOKMAL),
        LanguageCode.ENGLISH to dato?.formater(LanguageCode.ENGLISH)
    )

    private fun LocalDate.formater(language: LanguageCode): String? =
        when (language) {
            LanguageCode.BOKMAL -> dateFormatter(LanguageCode.BOKMAL, FormatStyle.LONG).format(this)
            LanguageCode.ENGLISH -> dateFormatter(LanguageCode.ENGLISH, FormatStyle.LONG).format(this)
            else -> null
        } // TODO: Denne bør vel liggje ein annan plass

    fun dateFormatter(languageCode: LanguageCode, formatStyle: FormatStyle): DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(languageCode.locale())

    private fun Adresse.formater() =
        listOfNotNull(adresselinje1, adresselinje2, adresselinje3).joinToString(System.lineSeparator()) +
                System.lineSeparator() + "${postnummer.value} ${poststed.value}" + System.lineSeparator() + landkode.landkode

    private fun innvilgetPensjon(radnummer: Int, pensjon: InnvilgetPensjon) =
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


    private fun avslaattPensjon(radnummer: Int, pensjon: AvslaattPensjon) = mapOf(
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

}

private const val RADER_PER_SIDE = 5

object InformasjonOmP1Dto : PDFVedleggData {
    override val tittel = Vedleggtyper.InformasjonOmP1.tittel
    override fun somPDFVedlegg() = PDFVedlegg.create {
        side("InformasjonOmP1.pdf") {
        }
    }
}


