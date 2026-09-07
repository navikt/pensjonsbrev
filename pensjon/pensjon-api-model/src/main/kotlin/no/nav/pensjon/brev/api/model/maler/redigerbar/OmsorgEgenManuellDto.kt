package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse

@Suppress("unused")
data class OmsorgEgenManuellDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : VedleggData, RedigerbarBrevdata<SaksbehandlervalgIDSL, OmsorgEgenManuellDto.PesysData> {

    data class PesysData(
        val returadresse: ReturAdresse,
    ) : FagsystemBrevdata
}