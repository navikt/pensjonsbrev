package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.EksportForbudKode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

@Suppress("unused")
data class VedtakStansAlderspensjonFlyttingMellomLandDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakStansAlderspensjonFlyttingMellomLandDto.SaksbehandlerValg, VedtakStansAlderspensjonFlyttingMellomLandDto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("Hvis reduksjon tilbake i tid")
        val feilutbetaling: Boolean
    ) : BrevbakerBrevdata

    data class PesysData(
        val brukersBostedsland: String?,  // v3.Person
        val eksportForbudKode: EksportForbudKode?, // Hentes fra enten v1.InngangOgEksportVurdering, eller hvis avdød finnes - hentes fra v1.InngangOgEksportVurderingAvdod
        val garantipensjonInnvilget: Boolean,  // AlderspensjonVedVirk - v4.Alderspensjon
        val harAvdod: Boolean, // Tom avdodFnr i v1.Avdod? v1.Avdod brukes ikke til malen i doksys per i dag.
        val harEksportForbud: Boolean, // Hvis en eksportForbudKode finnes - hentes fra v1.InngangOgEksportVurdering, eller hvis avdød finnes - hentes fra v1.InngangOgEksportVurderingAvdod
        val kravVirkDatoFom: LocalDate,  // v3.Krav
        val minst20ArTrygdetid: Boolean, // hentes fra v1.InngangOgEksportVurdering, eller hvis avdød finnes - hentes fra v1.InngangOgEksportVurderingAvdod
        val regelverkType: AlderspensjonRegelverkType,  // AlderspensjonVedVirk - v4.Alderspensjon
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
        val informasjonOmMedlemskapOgHelserettigheterDto: InformasjonOmMedlemskapOgHelserettigheterDto
        ) : BrevbakerBrevdata
}
