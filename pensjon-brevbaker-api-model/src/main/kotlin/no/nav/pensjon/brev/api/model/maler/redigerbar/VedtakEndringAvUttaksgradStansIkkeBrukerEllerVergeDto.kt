package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.SaksbehandlerValg, VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.PesysData> {
    data class SaksbehandlerValg(
        val aarsak: Aarsak
    ) : SaksbehandlerValgBrevdata {
        enum class Aarsak {
            @DisplayText("Uføretrygd er innvilget")
            ufoeretrygdErInnvilget,
            @DisplayText("Uføregrad er økt")
            ufoeregradErOekt,
            @DisplayText("Pensjonsopptjeningen er endret")
            pensjonsopptjeningenErEndret,
        }
    }

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