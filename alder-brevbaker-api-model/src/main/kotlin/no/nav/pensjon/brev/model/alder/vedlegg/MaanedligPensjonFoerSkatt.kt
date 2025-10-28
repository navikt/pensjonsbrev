package no.nav.pensjon.brev.model.alder.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.MetaforceSivilstand
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed
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
) : VedleggData {

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
        val regelverkstype: AlderspensjonRegelverkType,
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

    data class Bruker(
        val foedselsDato: LocalDate,
        val sivilstand: MetaforceSivilstand
    )

    data class SaerskiltSatsGjeldende(
        val saerskiltSatsErBrukt: Boolean,
    )
}