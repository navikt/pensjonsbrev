package no.nav.pensjon.brev.maler.vedlegg.pdf

import no.nav.brev.brevbaker.PDFVedlegg
import no.nav.brev.brevbaker.Side
import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


private const val RADER_PER_SIDE = 5

fun P1Dto.somVedlegg(): PDFVedlegg {
    var gjeldendeSide = 0

    val side1 = Side(
        ++gjeldendeSide, 1, felt = mapOf(
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
        )
    )

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
        Side(sidenummer = (index + 1) + gjeldendeSide, originalSide = 3, felt)
    }
    gjeldendeSide += avslaattePensjoner.size

    val side4 = Side(
        ++gjeldendeSide, 4, felt = mapOf(
            // utfyllende institusjon
            "institution-navn" to utfyllendeInstitusjon.navn,
            "institution-adresselinje" to utfyllendeInstitusjon.adresselinje,
            "institution-poststed" to utfyllendeInstitusjon.poststed.value,
            "institution-postnummer" to utfyllendeInstitusjon.postnummer.value,
            "institution-landkode" to utfyllendeInstitusjon.landkode.landkode,
            "institution-institusjonsID" to utfyllendeInstitusjon.institusjonsID,
            "institution-faksnummer" to utfyllendeInstitusjon.faksnummer,
            "institution-telefonnummer" to utfyllendeInstitusjon.telefonnummer?.value,
            "institution-epost" to utfyllendeInstitusjon.epost?.value,
            "institution-dato" to utfyllendeInstitusjon.dato.formater(),
            "institution-underskrift" to "",
        )
    )

    return PDFVedlegg(
        name = filnavn,
        tittel = tittel,
        (innvilgedePensjoner + avslaattePensjoner + side1 + side4).sortedBy { it.sidenummer },
    )
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