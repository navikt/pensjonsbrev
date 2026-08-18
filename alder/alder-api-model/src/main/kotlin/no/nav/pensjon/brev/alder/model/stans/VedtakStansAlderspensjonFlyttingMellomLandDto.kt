package no.nav.pensjon.brev.alder.model.stans

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.EksportForbudKode
import no.nav.pensjon.brev.alder.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

@Suppress("unused")
data class VedtakStansAlderspensjonFlyttingMellomLandDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<VedtakStansAlderspensjonFlyttingMellomLandDto.PesysData> {

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