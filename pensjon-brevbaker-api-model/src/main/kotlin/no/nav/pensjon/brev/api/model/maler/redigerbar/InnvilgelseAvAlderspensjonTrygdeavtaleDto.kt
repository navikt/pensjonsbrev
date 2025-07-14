package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class InnvilgelseAvAlderspensjonTrygdeavtaleDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<InnvilgelseAvAlderspensjonTrygdeavtaleDto.SaksbehandlerValg, InnvilgelseAvAlderspensjonTrygdeavtaleDto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("Tittel - Ny beregning av innvilget alderspensjon. Ingen endring av uttaksgraden")
        val nyBeregningAvInnvilgetAP: Boolean,
        @DisplayText("Tittel - Innvilgelse av alderspensjon eller økt uttaksgrad")
        val innvilgelseAPellerOektUttaksgrad: Boolean,
        @DisplayText("Slutthandling medfører: Innvilgelse av alderspensjon eller økt uttaksgrad")
        val medfoererInnvilgelseAvAPellerOektUttaksgrad: Boolean,
        @DisplayText("Slutthandling medfører: Økning i pensjonen")
        val oekningIPensjonen: Boolean,
        @DisplayText("Slutthandling medfører: Reduksjon i pensjonen")
        val reduksjonIPensjonen: Boolean,
        @DisplayText("Hvis supplerende stønad")
        val supplerendeStoenad: Boolean,
        @DisplayText("Hvis etterbetaling")
        val etterbetaling: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(

        val afpPrivatResultatFellesKontoret: Boolean?,  // v1.afpPrivat
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val avtalelandNavn: String?,  // v1.Land
        val borIAvtaleland: Boolean,  // v3.Person
        val borINorge: Boolean,  // v3.Person
        val erEOSLand: Boolean,  // v1.Land
        val erMellombehandling: Boolean,  // v3.Krav
        val erSluttbehandlingNorgeUtland: Boolean,  // v3.Krav
        val fullTrygdtid: Boolean,  // v4.AlderspensjonPerManed
        val harFlereBeregningsperioder: Boolean,  // Har flere enn 1 beregningsperiode > v2.BeregnetPensjonPerManed / v1.BeregnetPensjonPerManedKap20
        val inngangOgEksportVurdering: InngangOgEksportVurdering?,
        val kravVirkDatoFom: LocalDate,
        val regelverkType: AlderspensjonRegelverkType,
        val sakstype: Sakstype,
        val vedtakEtterbetaling: Boolean,  // v1.Vedtak
        val vedtaksresultatUtland: VedtaksresultatUtland?,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        //val opplysningerBruktIBeregningenAlderspensjon: OpplysningerBruktIBeregningenAlderDto?,
        //val opplysningerBruktIBeregningenAlderspensjonAP2025: OpplysningerBruktIBeregningenAlderAP2025Dto?,
        //val opplysningerOmAvdodBruktIBeregning: OpplysningerOmAvdoedBruktIBeregningDto?
    ) : BrevbakerBrevdata

    // v5.Alderspensjon / v1.AlderspensjonKap20
    data class AlderspensjonVedVirk(
        val garantipensjonInnvilget: Boolean,
        val garantitilleggInnvilget: Boolean,
        val gjenlevenderettAnvendt: Boolean,
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
        //val eksportForbud: Boolean,
        val eksportTrygdeavtaleAvtaleland: Boolean,
        val eksportTrygdeavtaleEOS: Boolean,
        val harOppfyltVedSammenlegging: Boolean,  // If (oppfyltVedSammenleggingKap19 or oppfyltVedSammenleggingKap20 or oppfyltVedSammenleggingFemArKap19 or oppfyltVedSammenleggingFemArKap20) = true
    )

    // v1.VedtaksresultatUtland
    data class VedtaksresultatUtland(
        val antallLandVilkarsprovd: Int,
        val landNavn: String?,  // Ett eller flere land som sendes i en string, komma separert
    )
}
