package no.nav.pensjon.brev.alder.model.sivilstand

import no.nav.pensjon.brev.alder.model.SivilstandAvdoed
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class VedtakOmregningAFPTilEnsligPensjonistAutoDto(
    val kravVirkDatoFom: LocalDate,
    val avdoed: Avdoed,
    val erEndret: Boolean,
    val harBarnUnder18: Boolean,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val etterbetaling: Boolean,
    val antallBeregningsperioder: Int,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
    val maanedligPensjonFoerSkattAFPOffentligDto: MaanedligPensjonFoerSkattAFPOffentligDto,
) : AutobrevData {
    data class Avdoed(
        val navn: String,
        val sivilstand: SivilstandAvdoed,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val totalPensjon: Kroner,
        val saertillegg: Boolean,
        val minstenivaaIndividuelt: Boolean,
    )
}
