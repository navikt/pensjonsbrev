package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class EndretUfoeretrygdPGAInntektDto2(
    val gammeltBelop: Int?,
    val nyttBelop: Int?,
    val virkningFom: LocalDate,
    val forventetInntekt: Int?,
    val utbetalingsgrad: Int?,
    val uforegrad: Int,
    val vilFylle67IlaVirkningFomAr: Boolean,
    val gjenlevendetillegg: Gjenlevendetillegg?,
    val instoppholdType: String?,
    val totalNetto: Int,
    val avkortningInntektsgrense: Int,
    val avkortningInntektstak: Int,
    val belopRedusert: Boolean,
    val avkortningsbelopPerAr: Int?,
    val grunnbelop: Int,
    val kravarsaksType: String,
    val totalNettoForAr: Int?,
    val nettoAkk: Int?,
    val nettoPerManed: Int,
    val belopOkt: Boolean,
    val regelverktypeBT: String?,
    val brukerBorINorge: Boolean,
    val ettArForVirkningstidspunkt: Int,
    val borMed: String?,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val endretBT: Boolean,

    // Trengs for vedlegg. Husk å fjerne når vedleggene er konvertert
    val pe: PE,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) : BrevbakerBrevdata


data class Gjenlevendetillegg (
    val belop: Int,
)

data class BarnetilleggFellesbarn (
    val endret: Boolean,
    val gammeltBelop: Int,
    val nyttBelop: Int,
    val inntektBruktIAvkortningAv: Int?,
    val periodisertInntekt: Boolean,
    val periodisertFribelop: Boolean,
    val brukersInntektBruktIAvkortningAv: Int?,
    val inntektAnnenForelder: Int?,
    val belopFratrukketAnnenForeldersInntekt: Int?,
    val justeringsbelopPerAr: Int?,
    val netto: Int,
    val fribelop: Int,
    val avkortningInntektstak: Int,
    val arligAvkortningsbelop: Int,
    val antallBarn: Int,
)

data class BarnetilleggSaerkullsbarn(
    val endret: Boolean,
    val gammeltBelop: Int,
    val nyttBelop: Int,
    val inntektBruktIAvkortningAv: Int?,
    val periodisertInntekt: Boolean,
    val periodisertFribelop: Boolean,
    val netto: Int,
    val justeringsbelopPerAr: Int?,
    val fribelop: Int,
    val avkortningInntektstak: Int,
    val arligAvkortningsbelop: Int,
    val antallBarn: Int,
)
