package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class VedtakOmFjerningAvOmsorgsopptjeningDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakOmFjerningAvOmsorgsopptjeningDto.SaksbehandlerValg, VedtakOmFjerningAvOmsorgsopptjeningDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Aktuelle år")
        val aktuelleAar: String,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : BrevbakerBrevdata
}