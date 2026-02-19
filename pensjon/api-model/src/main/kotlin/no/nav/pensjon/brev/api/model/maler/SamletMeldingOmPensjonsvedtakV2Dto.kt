package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Telefonnummer
import java.time.LocalDate

data class SamletMeldingOmPensjonsvedtakV2Dto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, SamletMeldingOmPensjonsvedtakV2Dto.PesysData> {
    data class PesysData(
        val sakstype: Sakstype,
        val p1Vedlegg: P1RedigerbarDto?,
    ) : FagsystemBrevdata
}

data class P1RedigerbarDto(
    val innehaver: P1Person,
    val forsikrede: P1Person,
    val sakstype: Sakstype,
    val innvilgedePensjoner: List<InnvilgetPensjon>,
    val avslaattePensjoner: List<AvslaattPensjon>,
    val utfyllendeInstitusjon: UtfyllendeInstitusjon, // I praksis Nav eller Nav-enheten
) : PDFVedleggData {

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
        val institusjon: Institusjon?, // 3.1
        val pensjonstype: Pensjonstype?, //3.2
        val datoFoersteUtbetaling: LocalDate?, //3.3
        val utbetalt: String?, // 3.4
        val grunnlagInnvilget: GrunnlagInnvilget?, // 3.5
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?, // 3.6
        val vurderingsperiode: String?, // 3.7
        val adresseNyVurdering: String?, // 3.8
    )

    data class AvslaattPensjon(
        val institusjon: Institusjon?, // 4.1
        val pensjonstype: Pensjonstype?, // 4.2
        val avslagsbegrunnelse: Avslagsbegrunnelse?, // 4.3
        val vurderingsperiode: String?, // 4.4
        val adresseNyVurdering: String?, // 4.5
    )

    enum class Pensjonstype(val nummer: Int) {
        Alder(1),
        Ufoere(2),
        Etterlatte(3)
    }

    enum class GrunnlagInnvilget(val nummer: Int) {
        IHenholdTilNasjonalLovgivning(4),
        ProRata(5),
        MindreEnnEttAar(6)
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

    data class Institusjon(
        val institusjonsnavn: String?,
        val pin: String?,
        val saksnummer: String?,
        val vedtaksdato: String? = null,
        val datoForVedtak: LocalDate?,
        val land: String?,
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
        val dato: LocalDate? = null, // skal slettes
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