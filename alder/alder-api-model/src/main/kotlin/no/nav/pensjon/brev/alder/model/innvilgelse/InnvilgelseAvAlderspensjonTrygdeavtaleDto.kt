package no.nav.pensjon.brev.alder.model.innvilgelse

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate


@Suppress("unused")
data class InnvilgelseAvAlderspensjonTrygdeavtaleDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : BrevdataMedSaksbehandlerValg<InnvilgelseAvAlderspensjonTrygdeavtaleDto.PesysData> {

    data class PesysData(

        val afpPrivatResultatFellesKontoret: Boolean?, // v1.afpPrivat
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val avtalelandNavn: String?, // v1.Land
        val borIAvtaleland: Boolean, // v3.Person
        val borINorge: Boolean, // v3.Person
        val erEOSLand: Boolean, // v1.Land
        val erMellombehandling: Boolean, // v3.Krav
        val erSluttbehandlingNorgeUtland: Boolean, // v3.Krav
        val beloepEndring: BeloepEndring?,
        val fullTrygdtid: Boolean, // v4.AlderspensjonPerManed
        val harFlereBeregningsperioder: Boolean, // Har flere enn 1 beregningsperiode > v2.BeregnetPensjonPerManed / v1.BeregnetPensjonPerManedKap20
        val inngangOgEksportVurdering: InngangOgEksportVurdering?,
        val kravVirkDatoFom: LocalDate,
        val regelverkType: AlderspensjonRegelverkType,
        val sakstype: Sakstype,
        val vedtaksresultatUtland: VedtaksresultatUtland?,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val opplysningerBruktIBeregningenAlderspensjon: OpplysningerBruktIBeregningenAlderDto?,
        val opplysningerBruktIBeregningenAlderspensjonAP2025: OpplysningerBruktIBeregningenAlderAP2025Dto?,
        val opplysningerOmAvdodBruktIBeregning: OpplysningerOmAvdoedBruktIBeregningDto?
    ) : FagsystemBrevdata

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
        val harOppfyltVedSammenlegging: Boolean, // If (oppfyltVedSammenleggingKap19 or oppfyltVedSammenleggingKap20 or oppfyltVedSammenleggingFemArKap19 or oppfyltVedSammenleggingFemArKap20) = true
    )

   // v1.VedtaksresultatUtland
    data class VedtaksresultatUtland(
        val antallLandVilkarsprovd: Int,
        val landNavn: List<String>,
    )
}
