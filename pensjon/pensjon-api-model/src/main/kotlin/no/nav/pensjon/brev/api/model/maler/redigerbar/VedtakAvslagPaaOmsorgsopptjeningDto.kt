package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

@Suppress("unused")
data class VedtakAvslagPaaOmsorgsopptjeningDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<VedtakAvslagPaaOmsorgsopptjeningDto.PesysData> {

    data class PesysData(
        val navEnhet: String,
        val omsorgGodskrevetAar: List<Year>,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : FagsystemBrevdata
}