package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata


@Suppress("unused")
data class VedtakOmInnvilgelseAvOmsorgspoengDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, VedtakOmInnvilgelseAvOmsorgspoengDto.PesysData> {

    data class PesysData(
        val brukerNavn: String,
        val omsorgsopptjeningsaar: String,
        val orienteringOmSaksbehandlingstidDto: OrienteringOmSaksbehandlingstidDto
    ) : BrevbakerBrevdata
}