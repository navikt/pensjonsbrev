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
    ) : BrevbakerBrevdata

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val afpPrivatResultatFellesKontoret: Boolean?,  // v1.afpPrivat
        val avdod: Avdod?,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val faktiskBostedsland: String?,  // v3.Person
        val inngangOgEksportVurdering: InngangOgEksportVurdering?,
        val inngangOgEksportVurderingAvdod: InngangOgEksportVurderingAvdod?,
        val kravVirkDatoFom: LocalDate,
        val regelverkType: AlderspensjonRegelverkType,
        val sakstype: Sakstype,
        val sivilstand: MetaforceSivilstand,
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
        val harAvdod: Boolean,  // -> har avdodFnr
    )

    data class BeregnetPensjonPerManedVedVirk(
        val gjenlevendetilleggKap19: Kroner?,
    )

    data class InngangOgEksportVurdering(
        val eksportForbud: Boolean,
        val minst20ArTrygdetid: Boolean,
        val eksportTrygdeavtaleEOS: Boolean,
        val eksportTrygdeavtaleAvtaleland: Boolean,
    )

    data class InngangOgEksportVurderingAvdod(
        val minst20ArTrygdetidKap20: Boolean,  // minst20ArTrygdetidKap20Avdod
        val minst20ArBotidKap19: Boolean  // minst20ArBotidKap19Avdod
    )
}
