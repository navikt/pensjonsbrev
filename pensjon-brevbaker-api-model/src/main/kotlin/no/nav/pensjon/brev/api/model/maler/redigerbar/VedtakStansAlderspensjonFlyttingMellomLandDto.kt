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
        val brukersBostedsland: String?,
        val eksportForbudKode: EksportForbudKode?, // hentes fra enten v1.InngangOgEksportVurdering, eller hvis avdød finnes - hentes fra v1.InngangOgEksportVurderingAvdod
        val garantipensjonInnvilget: Boolean,
        val harAvdod: Boolean, //
        val harEksportForbud: Boolean,
        val kravVirkDatoFom: LocalDate,
        val minst20ArTrygdetid: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
        val informasjonOmMedlemskapOgHelserettigheterDto: InformasjonOmMedlemskapOgHelserettigheterDto
        ) : BrevbakerBrevdata
}
