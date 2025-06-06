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
        @DisplayText("Virkningstidspunktet er senere enn ønsket uttakstidspunkt")
        val kravVirkDatoFomSenereEnnOensketUttakstidspunkt: Boolean,
        @DisplayText("Gjenlevenderett er brukt i beregningen")
        val harGjenlevenderett: Boolean,
        @DisplayText("Gjenlevendetillegg er større enn 0")
        val harGjenlevendetillegg: Boolean,
        @DisplayText("GjenlevendetilleggKap19 kommer til utbetaling")
        val harGjenlevendetilleggKap19: Boolean,
        @DisplayText("Egen opptjening er best")
        val egenOpptjening: Boolean,
        @DisplayText("Supplerende stønad")
        val supplerendeStoenad: Boolean,
        @DisplayText("Bruker ikke skal betale kildeskatt")
        val ikkeKildeskatt: Boolean,
        @DisplayText("Bruker skal betale kildeskatt")
        val kildeskatt: Boolean,
        @DisplayText("Etterbetaling")
        val etterbetaling: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val afpPrivatResultatFellesKontoret: Boolean?,  // v1.afpPrivat
        val avdod: Avdod?,
        val avtalelandNavn: String?,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val borIAvtaleland: Boolean,  // v3.Person
        val borINorge: Boolean,  // v3.Person
        val erEOSLand: Boolean,  // v1.Land
        val erForstegangsbehandletNorgeUtland: Boolean,  // v3.Krav
        val faktiskBostedsland: String?,  // v3.Person
        val inngangOgEksportVurdering: InngangOgEksportVurdering?,
        val inngangOgEksportVurderingAvdod: InngangOgEksportVurderingAvdod?,
        val kravVirkDatoFom: LocalDate,
        val norgeBehandlendeLand: Boolean,  // v3.Krav
        val regelverkType: AlderspensjonRegelverkType,
        val sakstype: Sakstype,
        val sivilstand: MetaforceSivilstand,
        val vedtakEtterbetaling: Boolean,  // v1.Vedtak
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : BrevbakerBrevdata

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,  // NB! For AP2025 hentes uttaksgrad fra fra alderspensjonVedVirkKap20
        val totalPensjon: Kroner,
        val uforeKombinertMedAlder: Boolean,
        val innvilgetFor67: Boolean,
        val gjenlevendetilleggInnvilget: Boolean,
        val gjenlevendetilleggKap19Innvilget: Boolean,
        val gjenlevenderettAnvendt: Boolean,
        val privatAFPErBrukt: Boolean,
        val erEksportberegnet: Boolean,
        val pensjonstilleggInnvilget: Boolean,
        val godkjentYrkesskade: Boolean,
        val garantipensjonInnvilget: Boolean,
        val skjermingstilleggInnvilget: Boolean,
        val garantitilleeggInnvilget: Boolean,
    )

    data class Avdod(
        val avdodFnr: Int,
        val avdodNavn: String,
        val harAvdod: Boolean,  // -> "true" når avdodFnr finnes
    )

    data class BeregnetPensjonPerManedVedVirk(  // v4.AlderspensjonPerManed
        val gjenlevendetilleggKap19: Kroner?,
        val fullTrygdtid: Boolean,
    )

    data class InngangOgEksportVurdering(
        val eksportForbud: Boolean,
        val minst20ArTrygdetid: Boolean,
        val eksportTrygdeavtaleEOS: Boolean,
        val eksportTrygdeavtaleAvtaleland: Boolean,
        val harOppfyltVedSammenlegging: Boolean,  // If (oppfyltVedSammenleggingKap19 or oppfyltVedSammenleggingKap20 or oppfyltVedSammenleggingFemArKap19 or oppfyltVedSammenleggingFemArKap20) = true
    )

    data class InngangOgEksportVurderingAvdod(
        val minst20ArTrygdetidKap20: Boolean,  // minst20ArTrygdetidKap20Avdod
        val minst20ArBotidKap19: Boolean  // minst20ArBotidKap19Avdod
    )
}
