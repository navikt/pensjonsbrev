package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
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
        @DisplayText("Hvis etterbetaling av pensjon")
        val etterbetaling: Boolean?,
        @DisplayText("Bruk vanlig skattetrekk")
        val vanligSkattetrekk: Boolean?,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val afpPrivatResultatFellesKontoret: Boolean?,  // v1.afpPrivat
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val harFlereBeregningsperioder: Boolean,  // Har flere enn 1 beregningsperiode > v2.BeregnetPensjonPerManed / v1.BeregnetPensjonPerManedKap20
        val avdodFnr: Foedselsnummer?,  // v1.Avdod
        val avdodNavn: String?,  // v1.Avdod
        val avtalelandNavn: String?,  // v1.Avdod
        val borIAvtaleland: Boolean,  // v3.Person
        val borINorge: Boolean,  // v3.Person
        val erEOSLand: Boolean,  // v1.Land
        val erForstegangsbehandletNorgeUtland: Boolean,  // v3.Krav
        val faktiskBostedsland: String?,  // v3.Person
        val fullTrygdtid: Boolean,  // v4.AlderspensjonPerManed
        val gjenlevendetilleggKap19: Kroner?,  // v4.AlderspensjonPerManed
        val inngangOgEksportVurdering: InngangOgEksportVurdering,
        val kravVirkDatoFom: LocalDate,
        val norgeBehandlendeLand: Boolean,  // v3.Krav
        val regelverkType: AlderspensjonRegelverkType,
        val sakstype: Sakstype,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val opplysningerBruktIBeregningenAlderspensjon: OpplysningerBruktIBeregningenAlderDto?,
        val opplysningerBruktIBeregningenAlderspensjonAP2025: OpplysningerBruktIBeregningenAlderAP2025Dto?,
        val opplysningerOmAvdodBruktIBeregning: OpplysningerOmAvdoedBruktIBeregningDto?
    ) : FagsystemBrevdata
    // v5.Alderspensjon / v1.AlderspensjonKap20
    data class AlderspensjonVedVirk(
        val erEksportberegnet: Boolean,
        val garantipensjonInnvilget: Boolean,
        val garantitilleggInnvilget: Boolean,
        val gjenlevenderettAnvendt: Boolean,
        val gjenlevendetilleggInnvilget: Boolean,
        val gjenlevendetilleggKap19Innvilget: Boolean,
        val godkjentYrkesskade: Boolean,
        val innvilgetFor67: Boolean,
        val pensjonstilleggInnvilget: Boolean,
        val privatAFPErBrukt: Boolean,
        val skjermingstilleggInnvilget: Boolean,
        val totalPensjon: Kroner,
        val uforeKombinertMedAlder: Boolean,
        val uttaksgrad: Int,
    )
    // v1.InngangOgEksportVurdering / v1.InngangOgEksportVurderingAvdod
    data class InngangOgEksportVurdering(
        val eksportForbud: Boolean,
        val eksportTrygdeavtaleAvtaleland: Boolean,
        val eksportTrygdeavtaleEOS: Boolean,
        val harOppfyltVedSammenlegging: Boolean,  // If (oppfyltVedSammenleggingKap19 or oppfyltVedSammenleggingKap20 or oppfyltVedSammenleggingFemArKap19 or oppfyltVedSammenleggingFemArKap20) = true
        val minst20ArBotidKap19Avdod: Boolean,  // // hentes fra v1.InngangOgEksportVurderingAvdod
        val minst20ArTrygdetid: Boolean,
        val minst20ArTrygdetidKap20Avdod: Boolean,  // hentes fra v1.InngangOgEksportVurderingAvdod
    )
}
