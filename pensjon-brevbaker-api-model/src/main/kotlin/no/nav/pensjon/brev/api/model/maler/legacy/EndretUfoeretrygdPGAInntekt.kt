package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndretUfoeretrygdPGAInntektDto(
    val pe: PE,
    val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
    val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
    val gammeltBelop: Kroner,
    val nyttBelop: Kroner,
    val virkningFom: LocalDate,
    val virkningFomAr: Int,
    val forventetInntekt: Kroner,
    val utbetalingsgrad: Int,
    val uforegrad: Int,
    val ettArForVirkningstidspunkt: Int,
    val vilFylle67IlaVirkningFomAr: Boolean,
    val virkningFomErForsteDagIAaret: Boolean,
    val ektefelletilleggInnvilget: Boolean,
    val gjenlevendetilleggInnvilget: Boolean,
    val instoppholdType: String,
    val beregningUforeTotalNetto: Kroner,
    val avkortningInntektsgrense: Kroner,
    val avkortningInntektstak: Kroner,
    val beregningBelopRedusert: Boolean,
    val avkortningsbelopPerAr: Kroner,
    val beregningGrunnbelop: Kroner,
    val kravarsaksType: String,
    val nettoRestAr: Kroner,
    val nettoAkk: Kroner,
    val uforetrygdNetto: Kroner,
    val beregningBelopOkt: Boolean,
    val barnetilleggRegelverkType: String,

    // Barnetillegg fellesbarn
    val gammeltBelopBTFB: Kroner,
    val nyttBelopBTFB: Kroner,
    val innvilgetBTFB: Boolean,
    val inntektBruktIAvkortningAvBTFB: Kroner,
    val periodisertInntektBTFB: Boolean,
    val periodisertFribelopBTFB: Boolean,
    val brukersInntektBruktIAvkortningAvBTFB: Kroner,
    val inntektAnnenForelderBTFB: Kroner,
    val belopFratrukketAnnenForeldersInntektBTFB: Kroner,
    val justeringsbelopPerArBTFB: Kroner,
    val nettoBTFB: Kroner,
    val fribelopBTFB: Kroner,
    val avkortningInntektstakBTFB: Kroner,
    val arligAvkortningsbelopBTFB: Kroner,
    val justeringsbelopPerArUtenMinusBTFB: Kroner,

    // Barnetillegg saerkullsbarn
    val gammeltBelopBTSB: Kroner,
    val nyttBelopBTSB: Kroner,
    val innvilgetBTSB: Boolean,
    val inntektBruktIAvkortningAvBTSB: Kroner,
    val periodisertInntektBTSB: Boolean,
    val periodisertFribelopBTSB: Boolean,
    val nettoBTSB: Kroner,
    val justeringsbelopPerArBTSB: Kroner,
    val fribelopBTSB: Kroner,
    val avkortningInntektstakBTSB: Kroner,
    val arligAvkortningsbelopBTSB: Kroner,
    val justeringsbelopPerArUtenMinusBTSB: Kroner,
    val brukerBorINorge: Boolean,
) : BrevbakerBrevdata
