package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonBeregnetEtter
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class MaanedligPensjonFoerSkattDto(
    val gjeldendeBeregnetPensjonPerManed: GjeldendeBeregnetPensjon,
    val alderspensjonGjeldende: AlderspensjonGjeldende,
    val institusjonsoppholdGjeldende: InstitusjonsoppholdGjeldende?,
    val epsGjeldende: EPSgjeldende?,
    val erBeregnetSomEnsligEllerEnke: Boolean, // TODO flytt forretningslogikk inn i pesys (script_vedleggBeregnGiftLeverAdskilt)
    val erBeregnetSomEnsligPartner: Boolean, // TODO flytt forretningslogikk inn i pesys (script_vedleggBeregnPartnerLeverAdskilt)
    val bruker: Bruker,
) : BrevbakerBrevdata {

    data class EPSgjeldende(
        val mottarPensjon: Boolean,
        val borSammenMedBruker: Boolean,
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
        val andelKap19: Int,
        val andelKap20: Int,
        val grunnpensjonSats: Int,
    )
    data class GjeldendeBeregnetPensjon(
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
        val barnetilleggFB: Kroner?,
        val inntektspensjon: Kroner?,
        val garantipensjon: Kroner?,
        val garantitillegg: Kroner?,
        val gjenlevendetillegg: Kroner?,
        val totalPensjon: Kroner,
    )

    data class Bruker(
        val foedselsDato: LocalDate,
        val sivilstand: MetaforceSivilstand
    )
}