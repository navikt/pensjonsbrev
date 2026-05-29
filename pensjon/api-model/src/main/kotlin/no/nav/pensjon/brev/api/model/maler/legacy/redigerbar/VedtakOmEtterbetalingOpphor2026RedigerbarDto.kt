package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

data class VedtakOmEtterbetalingOpphor2026RedigerbarDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakOmEtterbetalingOpphor2026RedigerbarDto.PesysData> {
    data class PesysData(
        val etterbetaling: BrevbakerType.Kroner,
        val hjemler: Set<String>,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
        ) : FagsystemBrevdata
}