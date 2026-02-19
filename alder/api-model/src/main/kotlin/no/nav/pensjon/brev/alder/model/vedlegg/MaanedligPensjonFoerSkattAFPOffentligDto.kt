package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate

@Suppress("unused")
data class MaanedligPensjonFoerSkattAFPOffentligDto(
    val afpStatGjeldende: AFPStatGjeldende,
    val beregnetPensjonPerManedGjeldende: AFPStatBeregning,
    val beregnetPensjonPerManedVedVirk: AFPStatBeregning,
    val beregnetPensjonPerManed: AFPStatBeregningListe,
    val tilleggspensjonAFPStatGjeldende: TilleggspensjonAFPStatGjeldende,
    val kravVirkDatoFom: LocalDate,
) : VedleggData {
    data class AFPStatGjeldende(
        val erInntektsavkortet: Boolean,
        val er70ProsentRegelAvkortet: Boolean,
    )

    data class AFPStatBeregning(
        val fullTrygdetid: Boolean,
        val brukerErFlyktning: Boolean,
        val grunnbelop: Kroner,
        val grunnpensjon: Kroner?,
        val grunnpensjonForAvkort: Kroner?,
        val tilleggspensjon: Kroner?,
        val tilleggspensjonForAvkort: Kroner?,
        val sartillegg: Kroner?,
        val sartilleggForAvkort: Kroner?,
        val afpTillegg: Kroner?,
        val afpTilleggForAvkort: Kroner?,
        val minstenivaIndividuell: Kroner?,
        val minstenivaIndividuellForAvkort: Kroner?,
        val totalPensjon: Kroner?,
        val totalPensjonForAvkort: Kroner?,
        val fasteUtgifterVedInstiitusjonsopphold: Kroner?,
        val fasteUtgifterVedInstiitusjonsoppholdForAvkort: Kroner?,
        val familietillegg: Kroner?,
        val familietilleggForAvkort: Kroner?,
        val virkDatoFom: LocalDate,
        val virkDatoTom: LocalDate?,
        val fremtidigInntekt: Kroner?,
    )

    data class TilleggspensjonAFPStatGjeldende(
        val erRedusert: Boolean,
    )

    data class AFPStatBeregningListe(
        val antallBeregningsperioder: Int,
        val afpStatBeregningListe: List<AFPStatBeregning>,
    )
}
