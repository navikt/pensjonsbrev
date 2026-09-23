package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class OpplysningerBruktIBeregningUTDto(
    val barnetilleggGjeldende: BarnetilleggGjeldende?,
    val beregnetUTPerManedGjeldende: BeregnetUTPerManedGjeldende,
    val grunnbeloep: Kroner,
    val fraOgMedDatoErNesteAar: Boolean,
    val inntektEtterUfoereGjeldende_beloepIEU: Kroner?,
    val inntektFoerUfoereGjeldende: InntektFoerUfoereGjeldende,
    val inntektsAvkortingGjeldende: InntektsAvkortingGjeldende,
    val minsteytelseGjeldende_sats: Double?,
    val sivilstand: Sivilstand,
    val borMedSivilstand: BorMedSivilstand?,
    val trygdetidsdetaljerGjeldende: TrygdetidsdetaljerGjeldende,
    val ufoeretrygdGjeldende: UfoeretrygdGjeldende,
    val ungUfoerGjeldende_erUnder20Aar: Boolean?,
    val yrkesskadeGjeldende: YrkesskadeGjeldende?,
    val harKravaarsakEndringInntekt: Boolean,
) : VedleggData {
    data class YrkesskadeGjeldende(
        val beregningsgrunnlagBeloepAar: Kroner,
        val inntektVedSkadetidspunkt: Kroner,
        val skadetidspunkt: LocalDate,
        val yrkesskadegrad: Int,
    ) : VedleggData

    data class BarnetilleggGjeldende(
        val saerkullsbarn: Saerkullsbarn?,
        val fellesbarn: Fellesbarn?,
        val foedselsdatoPaaBarnTilleggetGjelder: List<LocalDate>,
    ) : VedleggData {
        data class Saerkullsbarn(
            val avkortningsbeloepAar: Kroner,
            val beloepNetto: Kroner,
            val beloepBrutto: Kroner,
            val beloepAarNetto: Kroner,
            val beloepAarBrutto: Kroner,
            val erRedusertMotinntekt: Boolean,
            val fribeloep: Kroner,
            val fribeloepErPeriodisert: Boolean,
            val inntektErPeriodisert: Boolean,
            val harFlereBarn: Boolean,
            val inntektBruktIAvkortning: Kroner,
            val inntektOverFribeloep: Kroner,
            val inntektstak: Kroner,
            val justeringsbeloepAar: Kroner,
        ) : VedleggData

        data class Fellesbarn(
            val avkortningsbeloepAar: Kroner,
            val beloepNetto: Kroner,
            val beloepBrutto: Kroner,
            val beloepAarNetto: Kroner,
            val beloepAarBrutto: Kroner,
            val beloepFratrukketAnnenForeldersInntekt: Kroner,
            val erRedusertMotinntekt: Boolean,
            val fribeloep: Kroner,
            val fribeloepErPeriodisert: Boolean,
            val inntektErPeriodisert: Boolean,
            val harFlereBarn: Boolean,
            val inntektAnnenForelder: Kroner,
            val samletInntektBruktIAvkortning: Kroner,
            val inntektOverFribeloep: Kroner,
            val inntektstak: Kroner,
            val justeringsbeloepAar: Kroner,
            val borMedSivilstand: BorMedSivilstand,
        ) : VedleggData
    }

    data class TrygdetidsdetaljerGjeldende(
        val anvendtTT: Int,
        val beregningsmetode: Beregningsmetode,
        val faktiskTTEOS: Int?,
        val faktiskTTNordiskKonv: Int?,
        val faktiskTTNorge: Int?,
        val framtidigTTNorsk: Int?,
        val nevnerTTEOS: Int?,
        val nevnerTTNordiskKonv: Int?,
        val samletTTNordiskKonv: Int?,
        val tellerTTEOS: Int?,
        val tellerTTNordiskKonv: Int?,
        val utenforEOSogNorden: UtenforEOSogNorden?,
    ) : VedleggData {

        data class UtenforEOSogNorden(
            val faktiskTTBilateral: Int,
            val tellerProRata: Int,
            val nevnerProRata: Int,
        ) : VedleggData
    }

    data class BeregnetUTPerManedGjeldende(
        val brukerErFlyktning: Boolean,
        val grunnbeloep: Kroner,
        val virkDatoFom: LocalDate,
    ) : VedleggData

    data class UfoeretrygdGjeldende(
        val beloepsgrense: Kroner,
        val beregningsgrunnlagBeloepAar: Kroner,
        val erKonvertert: Boolean,
        val kompensasjonsgrad: Double,
        val ufoeregrad: Int,
        val ufoeretidspunkt: LocalDate,
    ) : VedleggData

    data class InntektsAvkortingGjeldende(
        val forventetInntektAar: Kroner,
        val inntektsgrenseAar: Kroner,
        val inntektstak: Kroner,
    ) : VedleggData

    data class InntektFoerUfoereGjeldende(
        val erSannsynligEndret: Boolean,
        val ifuInntekt: Kroner,
    ) : VedleggData
}
