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
        val eksportForbudKode: EksportForbudKode?,
        val eksportForbudKodeAvdoed: EksportForbudKode?,
        val garantipensjonInnvilget: Boolean,
        val harAvdoed: Boolean,
        val kravVirkDatoFom: LocalDate,
        val minst20ArTrygdetid: Boolean,
        val minst20AarTrygdetidKap20Avdoed: Boolean,
        val regelverkType: AlderspensjonRegelverkType,
        val dineRettigheterOgMulighetTilAaKlage: DineRettigheterOgMulighetTilAaKlageDto,
        val informasjonOmMedlemskapOgHelserettigheter: InformasjonOmMedlemskapOgHelserettigheterDto?
        ) : BrevbakerBrevdata
}
