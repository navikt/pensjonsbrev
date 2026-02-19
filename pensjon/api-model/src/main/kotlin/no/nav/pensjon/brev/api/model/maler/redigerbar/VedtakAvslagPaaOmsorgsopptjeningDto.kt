package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Year

@Suppress("unused")
data class VedtakAvslagPaaOmsorgsopptjeningDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakAvslagPaaOmsorgsopptjeningDto.SaksbehandlerValg, VedtakAvslagPaaOmsorgsopptjeningDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Omsorgsarbeid utført for en syk, funksjonshemmet eller eldre person før 1992")
        val omsorgsarbeidFoer1992: Boolean,
        @DisplayText("Omsorgsarbeid utført etter 69 år")
        val omsorgsarbeidEtter69Aar: Boolean,
        @DisplayText("Pleie- og omsorgsarbeid mindre enn 22 timer  og mindre enn 6 måneder")
        val omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder: Boolean,
        @DisplayText("Pleie- og omsorgsarbeid mindre enn 22 timer")
        val omsorgsarbeidMindreEnn22Timer: Boolean,
        @DisplayText("Pleie- og omsorgsarbeid mindre enn 6 måneder")
        val omsorgsarbeidMindreEnn6Maaneder: Boolean,
        @DisplayText("Hvis søknad om AFP privat er avslått av Fellesordningen")
        val privatAFPavslaat: Boolean,
        @DisplayText("Hvis det søkes om omsorgsopptjeningen for omsorg for barn under 7 år før 1992 uten at det er søkt om AFP privat")
        val omsorgsarbeidForBarnUnder7aarFoer1992: Boolean,
        @DisplayText("Hvis omsorgsopptjening før 1992 allerede er godskrevet ektefellen")
        val omsorgsopptjeningenGodskrevetEktefellen: Boolean,
        @DisplayText("Hvis bruker er født før 1948")
        val brukerFoedtFoer1948: Boolean,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val navEnhet: String,
        val omsorgGodskrevetAar: List<Year>,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ) : FagsystemBrevdata
}