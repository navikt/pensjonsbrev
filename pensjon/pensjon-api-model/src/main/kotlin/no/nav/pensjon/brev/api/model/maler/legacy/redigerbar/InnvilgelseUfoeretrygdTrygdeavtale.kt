package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.brev.BrevLandmodell.Landkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.UforeVedtaksinfo
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class InnvilgelseUfoeretrygdTrygdeavtaleDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<InnvilgelseUfoeretrygdTrygdeavtaleDto.PesysData> {

    data class PesysData(
        val pe: PEgruppe10,
        val vedtaksinfo: UforeVedtaksinfo,
        val kravFremsattDato: LocalDate?,
        val oifuVedVirkningstidspunkt: Kroner?,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
        val nyeInnvilgedeBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val nyeAvslagBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val avslagBarnetilleggNye: List<BarnetilleggMedSammeBegrunnelsePaSammeTidDto> = emptyList(),
        val trygdetidsgrunnlag: List<Trygdetidsgrunnlag> = emptyList(),
        val harVTA: Boolean,
        val hjemler: Set<String>,

        val eosNordisk: Boolean = false,
        val avtaletype: String? = null,
        val artikkel: String? = null,

        val kravType: KravType = KravType.ANNET,
    ) : FagsystemBrevdata

    data class Trygdetidsgrunnlag(
        val land: Landkode,
        val fom: LocalDate,
        val tom: LocalDate,
    )

    enum class KravType {
        ANNET,
        MELLOMBEHANDLING,
        SLUTTBEHANDLING
    }
}