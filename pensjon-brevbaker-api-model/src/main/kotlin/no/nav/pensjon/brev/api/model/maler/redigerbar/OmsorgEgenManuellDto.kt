package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse
import no.nav.pensjon.brevbaker.api.model.Year

@Suppress("unused")
data class OmsorgEgenManuellDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<OmsorgEgenManuellDto.SaksbehandlerValg, OmsorgEgenManuellDto.PesysData> {
    data class SaksbehandlerValg(
        val avdoedNavn: String,
        val aarEgenerklaringOmsorgspoeng: Year,
        val aarInnvilgetOmsorgspoeng: Year,
        ) : BrevbakerBrevdata

    data class PesysData(
        val returadresse: ReturAdresse,
    ) : BrevbakerBrevdata
}