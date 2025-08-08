package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import no.nav.pensjon.brevbaker.api.model.Side
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import no.nav.pensjon.brevbaker.api.model.VedleggType
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

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
    val kravMottattDato: LocalDate,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val utfyllendeInstitusjon: Institusjon, // I praksis Nav eller Nav-enheten
) : BrevbakerBrevdata, PDFVedleggData {
    override fun tilPDFVedlegg() = somVedlegg()

    data class P1Person(
        val fornavn: String,
        val etternavn: String,
        val etternavnVedFoedsel: String,
        val foedselsdato: LocalDate?,
        val adresselinje: String,
        val poststed: Poststed,
        val postnummer: Postnummer,
        val landkode: Landkode,
    )

    data class InnvilgetPensjon(
        val institusjon: String,
        val pensjonstype: Pensjonstype,
        val datoFoersteUtbetaling: LocalDate,
        val bruttobeloep: Penger,
        val grunnlagInnvilget: GrunnlagInnvilget,
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
        val vurderingsperiode: Period,
        val adresseNyVurdering: Adresse,
    )

    data class AvslaattPensjon(
        val institusjon: String,
        val pensjonstype: Pensjonstype,
        val avslagsbegrunnelse: Avslagsbegrunnelse,
        val vurderingsperiode: Period,
        val adresseNyVurdering: Adresse,
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
        val institusjonsID: String,
        val faksnummer: String?,
        val telefonnummer: Telefonnummer?,
        val epost: Epost,
        val dato: LocalDate,
        val underskrift: String,
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
}

data class Penger(val verdi: Int, val valuta: Valuta)

@JvmInline
value class Valuta(val valuta: String) {
    init {
        require(valuta.length == 3)
    }
}

private const val RADER_PER_SIDE = 5

fun P1Dto.somVedlegg(): PDFVedlegg {
    var gjeldendeSide = 0

    val side1 = Side(++gjeldendeSide, 1, felt = mapOf(
            // innehaver
            "holder-fornavn" to innehaver.fornavn,
            "holder-etternavn" to innehaver.etternavn,
            "holder-etternavnVedFoedsel" to innehaver.etternavnVedFoedsel,
            "holder-foedselsdato" to innehaver.foedselsdato?.formater(),
            "holder-adresselinje" to innehaver.adresselinje,
            "holder-poststed" to innehaver.poststed.value,
            "holder-postnummer" to innehaver.postnummer.value,
            "holder-landkode" to innehaver.landkode.landkode,
            // forsikrede
            "insured-fornavn" to forsikrede.fornavn,
            "insured-etternavn" to forsikrede.etternavn,
            "insured-etternavnVedFoedsel" to forsikrede.etternavnVedFoedsel,
            "insured-foedselsdato" to forsikrede.foedselsdato?.formater(),
            "insured-adresselinje" to forsikrede.adresselinje,
            "insured-poststed" to forsikrede.poststed.value,
            "insured-postnummer" to forsikrede.postnummer.value,
            "insured-landkode" to forsikrede.landkode.landkode,

            "kravMottattDato" to kravMottattDato.formater(),
            "sakstype" to sakstype.name // TODO denne er vel for enkel
        ))

    val innvilgedePensjoner: List<Side> = (0..<innvilgedePensjoner.tilAntallSider()).map { index ->
        val felt = index.tilRaderPerSide().mapNotNull { radnummer ->
            innvilgedePensjoner.getOrNull(radnummer)
                ?.let { innvilgetPensjon(Radnummer((radnummer % RADER_PER_SIDE) + 1), it) }
                ?: emptyMap()
        }.reduce { acc, map -> acc + map }
        Side(sidenummer = (index + 1) + gjeldendeSide, originalSide = 2, felt)
    }
    gjeldendeSide += innvilgedePensjoner.size

    val avslaattePensjoner = (0..<avslaattePensjoner.tilAntallSider()).map { index ->
        val felt = index.tilRaderPerSide().mapNotNull { radnummer ->
            avslaattePensjoner.getOrNull(radnummer)
                ?.let { avslaattPensjon(Radnummer((radnummer % RADER_PER_SIDE) + 1),it) }
                ?: emptyMap()
        }.reduce { acc, map -> acc + map }
        Side(sidenummer = (index + 1) +gjeldendeSide, originalSide = 3, felt)
    }
    gjeldendeSide += avslaattePensjoner.size

    val side4 = Side(++gjeldendeSide, 4, felt = mapOf(
        // utfyllende institusjon
        "institution-navn" to utfyllendeInstitusjon.navn,
        "institution-adresselinje" to utfyllendeInstitusjon.adresselinje,
        "institution-poststed" to utfyllendeInstitusjon.poststed.value,
        "institution-postnummer" to utfyllendeInstitusjon.postnummer.value,
        "institution-landkode" to utfyllendeInstitusjon.landkode.landkode,
        "institution-institusjonsID" to utfyllendeInstitusjon.institusjonsID,
        "institution-faksnummer" to utfyllendeInstitusjon.faksnummer,
        "institution-telefonnummer" to utfyllendeInstitusjon.telefonnummer?.value,
        "institution-epost" to utfyllendeInstitusjon.epost.value,
        "institution-dato" to utfyllendeInstitusjon.dato.formater(),
        "institution-underskrift" to utfyllendeInstitusjon.underskrift,
    ))

    return PDFVedlegg(type = VedleggType("P1", "P1"), (innvilgedePensjoner + avslaattePensjoner + side1 + side4).sortedBy { it.sidenummer },)
}

private fun List<*>.tilAntallSider() = Math.ceilDiv(this.size, RADER_PER_SIDE)

private fun Int.tilRaderPerSide(): IntRange = (this * RADER_PER_SIDE)..(this * RADER_PER_SIDE) + 4

private fun innvilgetPensjon(radnummer: Radnummer, pensjon: P1Dto.InnvilgetPensjon) =
    mapOf(
        "${radnummer.rad}-institusjon" to pensjon.institusjon,
        "${radnummer.rad}-pensjonstype" to pensjon.pensjonstype.nummer.toString(),
        "${radnummer.rad}-datoFoersteUtbetaling" to pensjon.datoFoersteUtbetaling.formater(),
        "${radnummer.rad}-bruttobeloep" to pensjon.bruttobeloep.let { it.verdi.toString() + " " + it.valuta.valuta },
        "${radnummer.rad}-grunnlagInnvilget" to pensjon.grunnlagInnvilget.nummer.toString(),
        "${radnummer.rad}-reduksjonsgrunnlag" to pensjon.reduksjonsgrunnlag?.nummer.toString(),
        "${radnummer.rad}-vurderingsperiode" to pensjon.vurderingsperiode.formater(),
        "${radnummer.rad}-adresseNyVurdering" to pensjon.adresseNyVurdering.formater(),
    )


private fun avslaattPensjon(radnummer: Radnummer, pensjon: P1Dto.AvslaattPensjon) =
    mapOf(
        "${radnummer.rad}-institusjon" to pensjon.institusjon,
        "${radnummer.rad}-pensjonstype" to pensjon.pensjonstype.nummer.toString(),
        "${radnummer.rad}-avslagsbegrunnelse" to pensjon.avslagsbegrunnelse.nummer.toString(),
        "${radnummer.rad}-vurderingsperiode" to pensjon.vurderingsperiode.formater(),
        "${radnummer.rad}-adresseNyVurdering" to pensjon.adresseNyVurdering.formater(),
    )

private fun LocalDate.formater(): String? =
    dateFormatter(LanguageCode.ENGLISH, FormatStyle.LONG).format(this) // TODO: Denne bør vel liggje ein annan plass

private fun Period.formater() = this.toString() // TODO: Formater periode ordentleg

fun dateFormatter(languageCode: LanguageCode, formatStyle: FormatStyle): DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(languageCode.locale())

private fun P1Dto.Adresse.formater() =
    listOfNotNull(adresselinje1, adresselinje2, adresselinje3).joinToString(System.lineSeparator()) +
            System.lineSeparator() + "${postnummer.value} ${poststed.value}" + System.lineSeparator() + landkode.landkode

fun LanguageCode.locale(): Locale =
    when (this) {
        LanguageCode.BOKMAL -> Locale.forLanguageTag("no")
        LanguageCode.NYNORSK -> Locale.forLanguageTag("no")
        LanguageCode.ENGLISH -> Locale.UK
    }


@JvmInline
private value class Radnummer(val rad: Int)