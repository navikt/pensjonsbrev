package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto

@Suppress("unused")
data class VedtakOmFjerningAvOmsorgspoengDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakOmFjerningAvOmsorgspoengDto.SaksbehandlerValg, VedtakOmFjerningAvOmsorgspoengDto.PesysData> {
    data class SaksbehandlerValg(
        val aktuelleAar: String,
    ) : BrevbakerBrevdata

    data class PesysData(
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : BrevbakerBrevdata
}