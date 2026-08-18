package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import java.time.LocalDate

data class VedtakEndringAvUttaksgradStansBrukerEllerVergeDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<VedtakEndringAvUttaksgradStansBrukerEllerVergeDto.PesysData> {
    data class PesysData(
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto
    ) : FagsystemBrevdata

    data class Krav(
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonVedVirk(
        val skjermingstilleggInnvilget: Boolean,
        val regelverkType: AlderspensjonRegelverkType
    )
}