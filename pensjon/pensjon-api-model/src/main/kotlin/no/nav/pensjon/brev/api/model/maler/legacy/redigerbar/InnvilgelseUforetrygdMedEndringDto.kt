package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class InnvilgelseUforetrygdMedEndringDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<InnvilgelseUforetrygdMedEndringDto.PesysData> {

    data class PesysData(
        val pe: PEgruppe10,
        val oifuVedVirkningstidspunkt: Kroner?,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
        val nyeInnvilgedeBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val nyeAvslagBarnetillegg: List<BarnetilleggMedSammeBegrunnelsePaSammeTidDto> = emptyList(),
        val sisteTrygdetidsgrunnlag: Trygdetidsgrunnlag?,
        val hjemler: Set<String>
    ) : FagsystemBrevdata

    data class Trygdetidsgrunnlag(
        val fom: LocalDate,
        val tom: LocalDate,
    )
}