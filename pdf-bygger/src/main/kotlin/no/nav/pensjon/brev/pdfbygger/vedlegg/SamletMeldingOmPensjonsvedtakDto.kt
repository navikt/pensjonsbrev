package no.nav.pensjon.brev.pdfbygger.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import java.time.LocalDate

data class SamletMeldingOmPensjonsvedtakDto(
    val innehaver: P1Person,
    val forsikrede: P1Person,
    val sakstype: Sakstype,
    val kravMottattDato: String,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val institusjon: Institusjon,
) : BrevbakerBrevdata {

    data class P1Person(
        val fornavn: String,
        val etternavn: String,
        val etternavnVedFoedsel: String?,
        val foedselsdato: String?,
        val adresselinje: String,
        val poststed: String,
        val postnummer: String,
        val landkode: String,
    ) {
        init {
            require(landkode.length == 2)
        }
    }

    data class InnvilgetPensjon(
        val institusjon: String,
        val pensjonstype: Sakstype,
        val datoFoersteUtbetaling: String,
        val bruttobeloep: String, //TODO type ordentleg?
        val grunnlagInnvilget: GrunnlagInnvilget,
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
        val vurderingsperiode: String,
        val adresseNyVurdering: Adresse,
    )

    data class AvslaattPensjon(
        val institusjon: String,
        val pensjonstype: Sakstype,
        val avslagsbegrunnelse: Avslagsbegrunnelse,
        val vurderingsperiode: String,
        val adresseNyVurdering: Adresse,
    )

    enum class GrunnlagInnvilget(val nummer: Int, val fullTekst: String) {
        IHenholdTilNasjonalLovgivning(4, "I henhold til nasjonal lovgivning"),
        ProRata(
            5,
            "Som pensjon hvor perioder fra andre medlemsland er tatt med i beregningen (europeisk pro rata-utregning)"
        ),
        MindreEnnEttAar(
            6,
            "Som pensjon hvor perioder på mindre enn ett år er tatt med i beregningen som om de er fullført i henhold til lovgivningen til dette \n" +
                    "medlemslandet"
        )
    }

    enum class Reduksjonsgrunnlag(val nummer: Int) {
        PaaGrunnAvAndreYtelserEllerAnnenInntekt(7),
        PaaGrunnAvOverlappendeGodskrevnePerioder(8)
    }

    enum class Avslagsbegrunnelse(val nummer: Int) {
        IngenOpptjeningsperioder(4),
        OpptjeningsperiodePaaMindreEnnEttAar(5),
        KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt(6),
        VilkaarOmUfoerhetErIkkeOppfylt(7),
        InntektstakErOverskredet(8),
        PensjonsalderErIkkeNaadd(9),
        AndreAarsaker(10)
    }

    data class Adresse(
        val adresselinje1: String,
        val adresselinje2: String?,
        val adresselinje3: String?,
        val landkode: String,
        val postnummer: String,
        val poststed: String,
    ) {
        init {
            require(landkode.length == 2)
        }
    }

    data class Institusjon(
        val navn: String,
        val adresselinje: String,
        val poststed: String,
        val postnummer: String,
        val landkode: String,
        val institusjonsID: String,
        val faksnummer: String?,
        val telefonnummer: String?,
        val epost: String,
        val dato: LocalDate,
        val underskrift: String,
    )
}