package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import java.time.LocalDate

data class VedtakEndringAvUttaksgradStansBrukerEllerVergeDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, VedtakEndringAvUttaksgradStansBrukerEllerVergeDto.PesysData> {
    data class PesysData(
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto
    ) : BrevbakerBrevdata

    data class Krav(
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonVedVirk(
        val skjermingstilleggInnvilget: Boolean,
        val regelverkType: AlderspensjonRegelverkType
    )
}