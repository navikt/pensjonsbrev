package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class InnvilgelseAvAlderspensjonDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<InnvilgelseAvAlderspensjonDto.SaksbehandlerValg, InnvilgelseAvAlderspensjonDto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("Hvis virkningstidspunktet er senere enn ønsket uttakstidspunkt")
        val kravVirkDatoFomSenereEnnOensketUttakstidspunkt: Boolean,
        @DisplayText("Hvis gjenlevenderett er brukt i beregningen")
        val harGjenlevenderett: Boolean,
        @DisplayText("Hvis gjenlevendetillegg er større enn 0")
        val harGjenlevendetillegg: Boolean,
        @DisplayText("Hvis gjenlevendetilleggKap19 kommer til utbetaling")
        val harGjenlevendetilleggKap19: Boolean,
        @DisplayText("Hvis egen opptjening er best")
        val egenOpptjening: Boolean,
    ): BrevbakerBrevdata

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val avdodNavn: String?,  // v1.Avdod.navn
        val barnetilleggVedVirk: BarnetilleggVedVirk?,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val ektefelletilleggVedVirk: EktefelletilleggVedVirk?,
        val kravVirkDatoFom: LocalDate,
        val regelverkType: AlderspensjonRegelverkType,
        val sakstype: Sakstype,
        val sivilstand: MetaforceSivilstand,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ): BrevbakerBrevdata

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,  // NB! For AP2025 hentes uttaksgrad fra fra alderspensjonVedVirkKap20
        val totalPensjon: Kroner,
        val uforeKombinertMedAlder: Boolean,
        val innvilgetFor67: Boolean,
        val gjenlevendetilleggInnvilget: Boolean,
        val gjenlevendetilleggKap19Innvilget: Boolean,
        val gjenlevenderettAnvendt: Boolean,
        val privatAFPErBrukt: Boolean,
    )

    data class BarnetilleggVedVirk(
        val innvilgetBarnetilleggFellesbarn: Boolean,
        val innvilgetBarnetilleggSaerkullsbarn: Boolean,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val barnetilleggFellesbarn: Kroner?,
        val barnetilleggSaerkullsbarn: Kroner?,
        val ektefelletillegg: Kroner?,
        val gjenlevendetilleggKap19: Kroner?,
    )

    data class EktefelletilleggVedVirk(
        val innvilgetEktefelletillegg: Boolean,

    )
}
