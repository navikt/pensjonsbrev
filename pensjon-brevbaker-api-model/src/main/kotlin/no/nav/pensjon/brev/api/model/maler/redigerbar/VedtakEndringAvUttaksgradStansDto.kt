package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import java.time.LocalDate

data class VedtakEndringAvUttaksgradStansDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakEndringAvUttaksgradStansDto.SaksbehandlerValg, VedtakEndringAvUttaksgradStansDto.PesysData> {
    data class SaksbehandlerValg(
        val ufoeretrygdErInnvilgetEllerUfoeregradErOekt: Boolean,
        val pensjonsopptjeningenErEndret: Boolean
    ) : BrevbakerBrevdata

    data class PesysData(
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto
    ) : BrevbakerBrevdata

    data class Krav(
        val kravInitiertAv: KravInitiertAv,
        val virkDatoFom: LocalDate
    )

    data class AlderspensjonVedVirk(
        val skjermingstilleggInnvilget: Boolean,
        val regelverkType: AlderspensjonRegelverkType
    )
}