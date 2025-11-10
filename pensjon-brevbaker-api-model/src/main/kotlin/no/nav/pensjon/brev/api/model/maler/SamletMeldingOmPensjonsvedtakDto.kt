package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate

data class SamletMeldingOmPensjonsvedtakDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, SamletMeldingOmPensjonsvedtakDto.PesysData> {
    data class PesysData(
        val sakstype: Sakstype,
        val vedlegg: P1Dto,
    ) : BrevbakerBrevdata
}

data class P1Dto(
    val innehaver: P1Person,
    val forsikrede: P1Person,
    val sakstype: Sakstype,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val utfyllendeInstitusjon: UtfyllendeInstitusjon, // I praksis Nav eller Nav-enheten
) : BrevbakerBrevdata, PDFVedleggData {

    data class P1Person(
        val fornavn: String?,
        val etternavn: String?,
        val etternavnVedFoedsel: String?,
        val foedselsdato: LocalDate?,
        val adresselinje: String?,
        val poststed: Poststed?,
        val postnummer: Postnummer?,
        val landkode: Landkode?,
    )

    data class InnvilgetPensjon(
        val institusjon: List<Institusjon>,
        val pensjonstype: Pensjonstype?,
        val datoFoersteUtbetaling: LocalDate?,
        val bruttobeloepDesimal: String?,
        val valuta: String?,
        val utbetalingsHyppighet: Utbetalingshyppighet?,
        val vedtaksdato: String?,
        val grunnlagInnvilget: GrunnlagInnvilget?,
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
        val vurderingsperiode: String?,
        val adresseNyVurdering: List<Adresse>,
    )

    data class AvslaattPensjon(
        val institusjoner: List<Institusjon>?,
        val pensjonstype: Pensjonstype?,
        val avslagsbegrunnelse: Avslagsbegrunnelse?,
        val vurderingsperiode: String?,
        val vedtaksdato: String?,
        val adresseNyVurdering: List<Adresse>,
    )

    enum class Pensjonstype(val nummer: Int) {
        Alder(1),
        Ufoere(2),
        Etterlatte(3)
    }

    enum class GrunnlagInnvilget(val nummer: Int) {
        IHenholdTilNasjonalLovgivning(4),
        ProRata(
            5
        ),
        MindreEnnEttAar(
            6
        )
    }

    enum class Reduksjonsgrunnlag(val nummer: Int) {
        PaaGrunnAvAndreYtelserEllerAnnenInntekt(7),
        PaaGrunnAvOverlappendeGodskrevnePerioder(8)
    }

    enum class Avslagsbegrunnelse(val nummer: Int) {
        IngenOpptjeningsperioder(4),
        OpptjeningsperiodePaaMindreEnnEttAar(5),
        KravTilKvalifiseringsperiodeEllerAndreKvalifiseringskravErIkkeOppfylt(
            6
        ),
        VilkaarOmUfoerhetErIkkeOppfylt(7),
        InntektstakErOverskredet(8),
        PensjonsalderErIkkeNaadd(9),
        AndreAarsaker(10)
    }

    enum class Utbetalingshyppighet {
        Aarlig,
        Kvartalsvis,
        Maaned12PerAar,
        Maaned13PerAar,
        Maaned14PerAar,
        Ukentlig,
        UkjentSeVedtak,
    }

    data class Adresse(
        val adresselinje1: String?,
        val adresselinje2: String?,
        val adresselinje3: String?,
        val landkode: Landkode?,
        val postnummer: Postnummer?,
        val poststed: Poststed?,
    )

    data class Institusjon(
        val institusjonsid: String?,
        val institusjonsnavn: String?,
        val pin: String?,
        val saksnummer: String?,
        val land: String?
    )

    data class UtfyllendeInstitusjon(
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
}