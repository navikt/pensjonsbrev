package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import java.time.LocalDate

data class EndretUfoeretrygdPGAInntektDto2(


    val uforetrygd: Uforetrygd,
    val btfbEndret: Boolean,
    val btsbEndret: Boolean,
    val barnetilleggFellesbarn: BarnetilleggFellesbarn?,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn?,
    val gjenlevendetillegg: Gjenlevendetillegg?,
    val forventetInntekt: Int?,
    val virkningFom: LocalDate,
    val totalNetto: Int,
    val datoForNormertPensjonsalder: LocalDate,
    val sokerMottarApIlaAret: Boolean,
    val settingAvInntektForNesteAar: Boolean,

) : BrevbakerBrevdata

data class Uforetrygd(
    val netto: Int,
    val endringsbelop: Int,
    val uforegrad: Int,
    val inntektstak: Int,
    val inntektsgrense: Int,
    )

data class Gjenlevendetillegg (
    val belop: Int,
)

data class BarnetilleggFellesbarn (
    val netto: Int,
    val endringsbelop: Int,
    val inntektBruktIAvkortning: Int?,
    val fribelop: Int,
    val inntektstak: Int,
    val antallBarn: Int,
)

data class BarnetilleggSaerkullsbarn(

    val netto: Int,
    val endringsbelop: Int,
    val inntektBruktIAvkortning: Int?,
    val fribelop: Int,
    val inntektstak: Int,
    val antallBarn: Int,
)
