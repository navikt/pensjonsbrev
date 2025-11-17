package no.nav.pensjon.brev.alder.model.stans

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.EksportForbudKode
import no.nav.pensjon.brev.alder.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.SaksbehandlerValgBrevdata
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
    ) : SaksbehandlerValgBrevdata

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
        val informasjonOmMedlemskap: InformasjonOmMedlemskap? = null,
        val dineRettigheterOgMulighetTilAaKlage: DineRettigheterOgMulighetTilAaKlageDto,
        ) : FagsystemBrevdata
}