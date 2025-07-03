package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class VedtakOmEndringDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakOmEndringDto.SaksbehandlerValg, VedtakOmEndringDto.PesysData> {
    data class SaksbehandlerValg(
        val inntektskontrollDato: LocalDate,
        val virkFom: LocalDate,
        val alderspensjonPerMaanedFoerSkatt: Kroner,
        val skulleVaertRedusertFraDato: LocalDate,
    ) : BrevbakerBrevdata

    data class PesysData(val grunnbeloep: Kroner) : BrevbakerBrevdata
}