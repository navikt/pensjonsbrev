package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata
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
) : VedleggBrevdata {
    data class AvviksResultat(
        val skulleFaatt: Kroner,
        val fikk: Kroner,
        val avvik: Kroner,
        val harFaattForMye: Boolean,
    )

    data class Barnetillegg(
        val felles: Fellesbarn?,
        val saerkull: Saerkullsbarn?,
        val personinntekt: InntektOgFratrekk,
        val mindreEnn40AarTrygdetid: Boolean,
        val totaltResultat: AvviksResultat,
    ) {
        data class Fellesbarn(
            val sivilstand: BorMedSivilstand,
            val grunnbelop: Kroner,
            val fribeloep: Kroner,
            val resultat: AvviksResultat,
            val personinntektAnnenForelder: InntektOgFratrekk,
            val harSamletInntektOverInntektstak: Boolean,
            val samletInntekt: Kroner,
            val inntektstakSamletInntekt: Kroner,
        )

        data class Saerkullsbarn(
            val fribeloep: Kroner,
            val resultat: AvviksResultat,
            val harSamletInntektOverInntektstak: Boolean,
            val samletInntekt: Kroner,
            val inntektstakSamletInntekt: Kroner,
        )
    }

    data class InntektOgFratrekk(
        val inntekt: Inntekt,
        val fratrekk: Fratrekk,
    ) {
        data class Inntekt(val inntekter: List<InntektLinje>, val sum: Kroner) {
            data class InntektLinje(val type: InntektType, val registerKilde: Kilde, val beloep: Kroner) {
                @Suppress("unused")
                enum class InntektType { NAERINGSINNTEKT, UTLANDSINNTEKT, ARBEIDSINNTEKT, UFOERETRYGD, ANDRE_PENSJONER_OG_YTELSER, FORVENTET_PENSJON_FRA_UTLANDET }
                @Suppress("unused")
                enum class Kilde { INNMELDT_AV_ARBEIDSGIVER, OPPGITT_AV_SKATTEETATEN, OPPGITT_AV_BRUKER, NAV }
            }
        }

        data class Fratrekk(val fratrekk: List<FratrekkLinje>, val sum: Kroner) {
            data class FratrekkLinje(val type: InntektType, val aarsak: Aarsak, val beloep: Kroner) {
                @Suppress("unused")
                enum class InntektType { NAERINGSINNTEKT, UTLANDSINNTEKT, ARBEIDSINNTEKT, INNTEKT, FRATREKKBAR_INNTEKT, FORVENTET_PENSJON_FRA_UTLANDET, ANDRE_PENSJONER_OG_YTELSER }
                @Suppress("unused")
                enum class Aarsak { FOER_INNVILGET_UFOERETRYGD, ETTER_OPPHOERT_UFOERETRYGD, ERSTATNING_INNTEKTSTAP_ERSTATNINGSOPPGJOER, ETTERSLEP_AVSLUTTET_ARBEID_ELLER_VIRKSOMHET, ANNET, ETTERBETALING_FRA_NAV, INNTEKT_INNTIL_1G }
            }
        }

    }
}