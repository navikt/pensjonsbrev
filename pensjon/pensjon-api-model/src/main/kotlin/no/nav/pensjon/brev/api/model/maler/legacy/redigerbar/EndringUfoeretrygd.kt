package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class EndringUfoeretrygdDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<EndringUfoeretrygdDto.PesysData> {
    data class PesysData(
        val pe: PEgruppe10,
        val kravFremsattDato: LocalDate?,

        val oifuVedVirkningstidspunkt: Kroner?,

        val opphortEktefelletillegg: Boolean,
        val opphortGjenlevendetillegg: Boolean,
        val opphoersbegrunnelseEktefelletillegg: Opphoersbegrunnelse,

        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,

        val nyeInnvilgedeBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val nyeAvslagBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val nyeOpphorteBarnetillegg: List<BarnetilleggUTDto> = emptyList(),

        val avslagBarnetilleggNye: List<BarnetilleggMedSammeBegrunnelsePaSammeTidDto> = emptyList(),
        val opphorteBarnetilleggNye: List<BarnetilleggMedSammeBegrunnelsePaSammeTidDto> = emptyList(),

        val hjemler: Set<String>
    ) : FagsystemBrevdata

    data class Opphoersbegrunnelse(
        val bruker_flyttet_ikke_avt_land: Boolean,
        val eps_flyttet_ikke_avt_land: Boolean,
        val eps_opph_ikke_avt_land: Boolean,
        val barn_flyttet_ikke_avt_land: Boolean,
        val barn_opph_ikke_avt_land: Boolean,
    )
}