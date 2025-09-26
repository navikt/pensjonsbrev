package no.nav.pensjon.brev.api.model.maler.auto

import no.nav.pensjon.brev.api.model.maler.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BorMedSivilstand
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Institusjon
import no.nav.pensjon.brev.api.model.maler.SivilstandAvdoed
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonAvdodAutoDto (
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val beregnetPensjonPerManed: BeregnetPensjonPerManed,
    val avdodInformasjon: AvdodInformasjon,
    val institusjonsoppholdVedVirk: Institusjon,
    val institusjonsoppholdGjeldende: Institusjon,
    val sivilstand: BorMedSivilstand,
    val virkFom: LocalDate,
    val harBarnUnder18: Boolean,
    val etterBetaling: Boolean,
    ): BrevbakerBrevdata {

    data class AlderspensjonVedVirk (
        val harEndretPensjon: Boolean,
        val totalPensjon: Kroner,
        val regelverkType: AlderspensjonRegelverkType,
        val uttaksgrad: Int,
        val minstenivaIndividuellInnvilget: Boolean,
    )

    data class BeregnetPensjonPerManed(
        val antallBeregningsperioderPensjon: Int,
        val erPerioderMedUttak: Boolean,
        val garantiPensjon: Kroner,

    )

    data class AvdodInformasjon (
        val sivilstandAvdoed: SivilstandAvdoed,
        val ektefelletilleggOpphort: Boolean,
        val gjenlevendesAlder: Int,
        val avdodNavn: String,
    )
}