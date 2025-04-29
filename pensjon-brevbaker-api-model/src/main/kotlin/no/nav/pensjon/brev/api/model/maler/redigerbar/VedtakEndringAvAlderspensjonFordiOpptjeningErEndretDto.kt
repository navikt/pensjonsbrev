package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.PesysData> {
    data class PesysData(
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk
    ) : BrevbakerBrevdata

    data class Krav(
        val virkDatoFom: LocalDate,
    )

    data class AlderspensjonVedVirk(
        val skjermingstilleggInnvilget: Boolean,
        val totalPensjon: Kroner,
        val skjermingstillegg: Kroner?
    )
}