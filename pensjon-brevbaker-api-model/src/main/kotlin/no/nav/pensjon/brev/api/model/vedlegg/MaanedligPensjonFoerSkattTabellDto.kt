package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonBeregnetEtter
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object MaanedligPensjonFoerSkattTabell {
    data class AlderspensjonPerManed(
        val virkDatoFom: LocalDate,
        val virkDatoTom: LocalDate?,
        val grunnpensjon: Kroner?,
        val tilleggspensjon: Kroner?,
        val saertillegg: Kroner?,
        val pensjonstillegg: Kroner?,
        val skjermingstillegg: Kroner?,
        val gjenlevendetilleggKap19: Kroner?,
        val minstenivaPensjonistPar: Kroner?,
        val minstenivaIndividuell: Kroner?,
        val fasteUtgifter: Kroner?,
        val familieTillegg: Kroner?,
        val fullTrygdetid: Boolean,
        val beregnetEtter: AlderspensjonBeregnetEtter?,
        val grunnbeloep: Kroner,
        val ektefelletillegg: Kroner?,
        val barnetilleggSB: Kroner?,
        val barnetilleggSBbrutto: Kroner?,
        val barnetilleggFB: Kroner?,
        val barnetilleggFBbrutto: Kroner?,
        val inntektspensjon: Kroner?,
        val inntektBruktIavkortningET: Kroner?,
        val inntektBruktIavkortningSB: Kroner?,
        val inntektBruktIavkortningFB: Kroner?,
        val fribelopET: Kroner?,
        val fribelopFB: Kroner?,
        val fribelopSB: Kroner?,
        val garantipensjon: Kroner?,
        val garantitillegg: Kroner?,
        val gjenlevendetillegg: Kroner?,
        val totalPensjon: Kroner,
        val flyktningstatusErBrukt: Boolean,
        val avdodFlyktningstatusErBrukt: Boolean,
        val brukersSivilstand: MetaforceSivilstand,
    )
}