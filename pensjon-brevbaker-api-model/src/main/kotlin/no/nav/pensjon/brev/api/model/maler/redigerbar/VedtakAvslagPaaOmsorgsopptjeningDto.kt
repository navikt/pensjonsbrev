package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class VedtakAvslagPaaOmsorgsopptjeningDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakAvslagPaaOmsorgsopptjeningDto.SaksbehandlerValg, VedtakAvslagPaaOmsorgsopptjeningDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Omsorgsarbeid utført for en syk, funksjonshemmet eller eldre person før 1992")
        val omsorgsarbeidFoer1992: String,
        @DisplayText("Omsorgsarbeid utført etter 69 år")
        val omsorgsarbeidEtter69Aar: String,
        @DisplayText("Pleie- og omsorgsarbeid mindre enn 6 måneder")
        val omsorgsarbeidMindreEnn6mnd: String,
        @DisplayText("Hvis søknad om AFP privat er avslått av Fellesordningen")
        val privatAFPavslaat: String,
        @DisplayText("Hvis det søkes om omsorgsopptjeningen for omsorg for barn under 7 år før 1992 uten at det er søkt om AFP privat")
        val omsorgsarbeidForBarnUnder7aarFoer1992: String,
        @DisplayText("Hvis omsorgsopptjening før 1992 allerede er godskrevet ektefellen")
        val omsorgsopptjeningenGodskrevetEktefellen: String,
        @DisplayText("Hvis bruker er født før 1948")
        val brukerFoedtFoer1948: String,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val omsorgGodskrevetAar: List<String>,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : FagsystemBrevdata
}