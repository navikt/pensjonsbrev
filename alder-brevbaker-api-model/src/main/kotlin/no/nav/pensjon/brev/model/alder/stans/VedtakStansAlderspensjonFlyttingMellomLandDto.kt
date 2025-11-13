package no.nav.pensjon.brev.model.alder.stans

import no.nav.pensjon.brev.api.model.maler.PesysBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.EksportForbudKode
import no.nav.pensjon.brev.model.alder.InformasjonOmMedlemskap
import no.nav.pensjon.brev.model.alder.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
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
        ) : PesysBrevdata
}