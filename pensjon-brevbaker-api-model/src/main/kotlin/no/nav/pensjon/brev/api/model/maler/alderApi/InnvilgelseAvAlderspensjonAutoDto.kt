package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BorI
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class InnvilgelseAvAlderspensjonAutoDto(
    val afpPrivatResultatFellesKontoret: Boolean?,  // v1.afpPrivat
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val avtalelandNavn: String?,  // v1.Land
    val borI: BorI,
    val harFlereBeregningsperioder: Boolean,  // Har flere enn 1 beregningsperiode > v2.BeregnetPensjonPerManed / v1.BeregnetPensjonPerManedKap20
    val erEOSLand: Boolean,  // v1.Land
    val erForstegangsbehandletNorgeUtland: Boolean,  // v3.Krav
    val faktiskBostedsland: String?,  // v3.Person
    val fullTrygdetid: Boolean,  // v4.AlderspensjonPerManed
    val inngangOgEksportVurdering: InngangOgEksportVurdering,
    val kravVirkDatoFom: LocalDate,
    val norgeBehandlendeLand: Boolean,  // v3.Krav
    val regelverkType: AlderspensjonRegelverkType,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
    val opplysningerBruktIBeregningenAlderspensjon: OpplysningerBruktIBeregningenAlderDto?,
    val opplysningerBruktIBeregningenAlderspensjonAP2025: OpplysningerBruktIBeregningenAlderAP2025Dto?,
) : AutobrevData {

    // v5.Alderspensjon / v1.AlderspensjonKap20
    data class AlderspensjonVedVirk(
        val erEksportberegnet: Boolean,
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
        val eksportTrygdeavtaleAvtaleland: Boolean,
        val eksportTrygdeavtaleEOS: Boolean,
        val eksportBeregnetUtenGarantipensjon: Boolean,
        val harOppfyltVedSammenlegging: Boolean,  // If (oppfyltVedSammenleggingKap19 or oppfyltVedSammenleggingKap20 or oppfyltVedSammenleggingFemArKap19 or oppfyltVedSammenleggingFemArKap20) = true
    )
}
