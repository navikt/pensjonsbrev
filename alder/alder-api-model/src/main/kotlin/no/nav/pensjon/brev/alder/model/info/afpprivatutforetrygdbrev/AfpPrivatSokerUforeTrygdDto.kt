package no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.VedleggData

data class AfpPrivatSokerUforeTrygdDto (
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdataMedSaksbehandlerValg<AfpPrivatSokerUforeTrygdDto.PesysData> {

    data class PesysData(
        val vedleggDto: AFpPrivatSokerUforeTrygdVedleggDto
    ) : FagsystemBrevdata
}
data class AFpPrivatSokerUforeTrygdVedleggDto (
    val uforeTrygdTil_ATT: Boolean,
    val kap19: Boolean,
) : VedleggData