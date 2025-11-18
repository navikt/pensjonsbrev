package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Year

@Suppress("unused")
data class OmsorgEgenManuellDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : VedleggData, RedigerbarBrevdata<OmsorgEgenManuellDto.SaksbehandlerValg, OmsorgEgenManuellDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("År egenerklæring omsorgspoeng")
        val aarEgenerklaringOmsorgspoeng: Year,
        @DisplayText("År innvilget omsorgspoeng")
        val aarInnvilgetOmsorgspoeng: Year,
        ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val returadresse: ReturAdresse,
    ) : FagsystemBrevdata
}