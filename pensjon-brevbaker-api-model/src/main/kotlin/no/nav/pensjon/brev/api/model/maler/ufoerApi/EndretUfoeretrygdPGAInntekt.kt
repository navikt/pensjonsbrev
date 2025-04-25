package no.nav.pensjon.brev.api.model.maler.ufoerApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

@Suppress("unused")
data class EndretUfoeretrygdPGAInntektDto(


    val gammeltBelop: Int?,
    val nyttBelop: Int?,
    val virkningFom: LocalDate,
    val forventetInntekt: Int?,
    val utbetalingsgrad: Int?,
    val uforegrad: Int,
    val vilFylle67IlaVirkningFomAr: Boolean,
    // Todo: fjerne?
    val innvilgetET: Boolean,
    val innvilgetGJT: Boolean,
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

    // Barnetillegg fellesbarn
    val innvilgetBTFB: Boolean,
    val gammeltBelopBTFB: Int?,
    val nyttBelopBTFB: Int?,
    val inntektBruktIAvkortningAvBTFB: Int?,
    val periodisertInntektBTFB: Boolean,
    val periodisertFribelopBTFB: Boolean,
    val brukersInntektBruktIAvkortningAvBTFB: Int?,
    val inntektAnnenForelderBTFB: Int?,
    val belopFratrukketAnnenForeldersInntektBTFB: Int?,
    val justeringsbelopPerArBTFB: Int?,
    val nettoBTFB: Int?,
    val fribelopBTFB: Int?,
    val avkortningInntektstakBTFB: Int?,
    val arligAvkortningsbelopBTFB: Int?,
    val antallBarnBTFB: Int,

    // Barnetillegg saerkullsbarn
    val innvilgetBTSB: Boolean,
    val gammeltBelopBTSB: Int?,
    val nyttBelopBTSB: Int?,
    val inntektBruktIAvkortningAvBTSB: Int?,
    val periodisertInntektBTSB: Boolean,
    val periodisertFribelopBTSB: Boolean,
    val nettoBTSB: Int?,
    val justeringsbelopPerArBTSB: Int?,
    val fribelopBTSB: Int?,
    val avkortningInntektstakBTSB: Int?,
    val arligAvkortningsbelopBTSB: Int?,
    val antallBarnBTSB: Int,

    // Trengs for vedlegg. Husk å fjerne når vedleggene er konvertert
    val pe: PE,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
) : BrevbakerBrevdata

