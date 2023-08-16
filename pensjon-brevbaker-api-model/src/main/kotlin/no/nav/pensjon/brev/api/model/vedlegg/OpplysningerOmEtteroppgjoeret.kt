package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

data class OpplysningerOmEtteroppgjoeretDto(
    val periode: Year,
    val harGjenlevendeTillegg: Boolean,
    val ufoeretrygd: AvviksResultat,
    val barnetillegg: Barnetillegg?,
    val harFaattForMye: Boolean,
    val totaltAvvik: Kroner,
    val pensjonsgivendeInntektBruktIBeregningen: Kroner,
    val pensjonsgivendeInntekt: InntektOgFratrekk,
) {
    data class AvviksResultat(
        val skulleFaatt: Kroner,
        val fikk: Kroner,
        val avvik: Kroner,
    )

    data class Barnetillegg(
        val felles: Fellesbarn?,
        val saerkull: Saerkullsbarn?,
        val personinntekt: InntektOgFratrekk,
        val mindreEnn40AarTrygdetid: Boolean,
    ) {
        data class Fellesbarn(
            val sivilstand: BorMedSivilstand,
            val grunnbelop: Kroner,
            val fribeloep: Kroner,
            val isFribeloepRedusert: Boolean,
            val resultat: AvviksResultat,
            val personinntektAnnenForelder: InntektOgFratrekk,
            val harSamletInntektOverInntektstak: Boolean,
            val inntektstakSamletInntekt: Kroner,
        )

        data class Saerkullsbarn(
            val fribeloep: Kroner,
            val isFribeloepRedusert: Boolean,
            val resultat: AvviksResultat,
        )

    }

    data class InntektOgFratrekk(
        val inntekt: Inntekt,
        val fratrekk: Fratrekk,
    ) {
        data class Inntekt(val inntekter: List<InntektLinje>, val sum: Kroner) {
            data class InntektLinje(val type: InntektType, val registerKilde: Kilde, val beloep: Kroner) {
                enum class InntektType { NAERINGSINNTEKT, UTLANDSINNTEKT, ARBEIDSINNTEKT, UFOERETRYGD, ANDRE_PENSJONER_OG_YTELSER, FORVENTET_PENSJON_FRA_UTLANDET }
                enum class Kilde { INNMELDT_AV_ARBEIDSGIVER, OPPGITT_AV_SKATTEETATEN, OPPGITT_AV_BRUKER, NAV }
            }
        }

        data class Fratrekk(val fratrekk: List<FratrekkLinje>, val sum: Kroner) {
            data class FratrekkLinje(val type: InntektType, val aarsak: Aarsak, val beloep: Kroner) {
                enum class InntektType { NAERINGSINNTEKT, UTLANDSINNTEKT, ARBEIDSINNTEKT, INNTEKT, FRATREKKBAR_INNTEKT, FORVENTET_PENSJON_FRA_UTLANDET, ANDRE_PENSJONER_OG_YTELSER }
                enum class Aarsak { FOER_INNVILGET_UFOERETRYGD, ETTER_OPPHOERT_UFOERETRYGD, ERSTATNING_INNTEKTSTAP_ERSTATNINGSOPPGJOER, ETTERSLEP_AVSLUTTET_ARBEID_ELLER_VIRKSOMHET, ANNET, ETTERBETALING_FRA_NAV, INNTEKT_INNTIL_1G }
            }
        }

    }
}