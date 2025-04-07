package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonBeregnetEtter
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class MaanedligPensjonFoerSkattDto(
    val beregnetPensjonPerManedGjeldende: AlderspensjonPerManed,
    val alderspensjonGjeldende: AlderspensjonGjeldende,
    val institusjonsoppholdGjeldende: InstitusjonsoppholdGjeldende?,
    val epsGjeldende: EPSgjeldende?,
    val ektefelletilleggGjeldende: EktefelletilleggGjeldende?,
    val tilleggspensjonGjeldende: TilleggspensjonGjeldende?,
    val saerskiltSatsGjeldende: SaerskiltSatsGjeldende?,
    val barnetilleggGjeldende: BarnetilleggGjeldende?,
    val bruker: Bruker,
    val krav: Krav,
    val alderspensjonPerManed: List<AlderspensjonPerManed>,
) : BrevbakerBrevdata {

    data class Krav(
        val virkDatoFom: LocalDate,
    )

    data class BarnetilleggGjeldende(
        val innvilgetBarnetilleggFB: Boolean,
        val innvilgetBarnetilleggSB: Boolean,
    )

    data class EktefelletilleggGjeldende(
        val innvilgetEktefelletillegg: Boolean,
    )

    data class EPSgjeldende(
        val mottarPensjon: Boolean,
        val borSammenMedBruker: Boolean,
        val harInntektOver2G: Boolean,
    )

    data class InstitusjonsoppholdGjeldende(
        val ensligPaInst: Boolean,
        val aldersEllerSykehjem: Boolean,
        val epsPaInstitusjon : Boolean,
        val fengsel : Boolean,
        val helseinstitusjon : Boolean,
    )

    data class AlderspensjonGjeldende(
        val regelverkstype: AlderspensjonRegelverkstype,
        val erEksportberegnet: Boolean,
        val andelKap19: Int,
        val andelKap20: Int,
        val grunnpensjonSats: Int,
        val gjenlevendetilleggKap19Innvilget: Boolean,
        val gjenlevendetilleggInnvilget: Boolean,
    )

    data class TilleggspensjonGjeldende(
        val erRedusert: Boolean,
        val kombinertMedAvdod: Boolean,
        val pgaYrkesskade: Boolean,
        val pgaUngUfore: Boolean,
        val pgaUngUforeAvdod: Boolean,
        val pgaYrkesskadeAvdod: Boolean,
    )

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

    data class Bruker(
        val foedselsDato: LocalDate,
        val sivilstand: MetaforceSivilstand
    )

    data class SaerskiltSatsGjeldende(
        val saerskiltSatsErBrukt: Boolean,
    )
}