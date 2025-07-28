package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import java.math.BigDecimal
import java.time.LocalDate

data class SamletMeldingOmPensjonsvedtakDto(
    val innehaver: P1Person,
    val forsikrede: P1Person,
    val sakstype: Sakstype,
    val kravMottattDato: LocalDate,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val utfyllendeInstitusjon: Institusjon, // I praksis Nav eller Nav-enheten
) : BrevbakerBrevdata {

    data class P1Person(
        val fornavn: String,
        val etternavn: String,
        val etternavnVedFoedsel: String,
        val foedselsdato: LocalDate?,
        val adresselinje: String,
        val poststed: String,
        val postnummer: String,
        val landkode: Landkode,
    )

    data class InnvilgetPensjon(
        val institusjon: String,
        val pensjonstype: Pensjonstype,
        val datoFoersteUtbetaling: LocalDate,
        val bruttobeloep: Penger,
        val grunnlagInnvilget: GrunnlagInnvilget,
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
        val vurderingsperiode: String,
        val adresseNyVurdering: Adresse,
    )

    data class AvslaattPensjon(
        val institusjon: String,
        val pensjonstype: Pensjonstype,
        val avslagsbegrunnelse: Avslagsbegrunnelse,
        val vurderingsperiode: String,
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
        KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt(6, "qualifying period not completed or eligibility requirements not met"),
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
        val postnummer: String,
        val poststed: String,
    )

    data class Institusjon(
        val navn: String,
        val adresselinje: String,
        val poststed: String,
        val postnummer: String,
        val landkode: Landkode,
        val institusjonsID: String,
        val faksnummer: String?,
        val telefonnummer: String?,
        val epost: String,
        val dato: LocalDate,
        val underskrift: String,
    )
}

data class Penger(val verdi: BigDecimal, val valuta: Valuta)

@JvmInline
value class Valuta(val valuta: String) {
    init {
        require(valuta.length == 3)
    }

    companion object {
        val NOK = Valuta("NOK")
    }
}