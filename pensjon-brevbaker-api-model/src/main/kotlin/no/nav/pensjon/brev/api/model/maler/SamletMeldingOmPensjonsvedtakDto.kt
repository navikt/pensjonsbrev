package no.nav.pensjon.brev.api.model.maler

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brevbaker.api.model.LanguageCode.BOKMAL
import no.nav.pensjon.brevbaker.api.model.LanguageCode.ENGLISH
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate

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
    val utfyllendeInstitusjon: UtfyllendeInstitusjon, // I praksis Nav eller Nav-enheten
    val vedtaksdato: String? = null,
) : BrevbakerBrevdata, PDFVedleggData {
    override val tittel = mapOf(
        BOKMAL to "P1 – Samlet melding om pensjonsvedtak",
        ENGLISH to "P1 – Summary of Pension Decisions"
    )

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
        val bruttobeloep: Int?,
        val grunnlagInnvilget: GrunnlagInnvilget?,
        val reduksjonsgrunnlag: Reduksjonsgrunnlag?,
        val vurderingsperiode: String?,
        val adresseNyVurdering: List<Adresse>,
    )

    data class AvslaattPensjon(
        val institusjon: Institusjon?,
        val pensjonstype: Pensjonstype?,
        val avslagsbegrunnelse: Avslagsbegrunnelse?,
        val vurderingsperiode: String?,
        val adresseNyVurdering: List<Adresse>,
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
        val institusjonsid: String?,
        val institusjonsnavn: String?,
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

object InformasjonOmP1Dto : PDFVedleggData {
    override val tittel = mapOf(
        BOKMAL to "Informasjon om skjemaet P1 og hvordan det brukes",
        ENGLISH to "Information about the P1 form and its use"
    )
}


