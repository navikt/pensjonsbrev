package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class OkningUforegradDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<OkningUforegradDto.PesysData> {

    data class PesysData(
        val pe: PEgruppe10,
        val kravFremsattDato: LocalDate?,
        val oifuVedVirkningstidspunkt: Kroner?,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
        val nyeInnvilgedeBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val nyeAvslagBarnetillegg: List<BarnetilleggMedSammeBegrunnelsePaSammeTidDto> = emptyList(),
        val sisteTrygdetidsgrunnlag: Trygdetidsgrunnlag?,
        val hjemler: Set<String>,
        val fribelopsperioder: List<Fribelopsperiode>? = null,
        val vektetFribelop: Double = 0.0,
        val vektetFribelopKr: Kroner = Kroner(0),
        val harVTA: Boolean = false,
    ) : FagsystemBrevdata
    //TODO: fjern defaultingen og nullable perioder når pen er oppdatert med ny dto

    data class Trygdetidsgrunnlag(
        val fom: LocalDate,
        val tom: LocalDate,
    )

    data class Fribelopsperiode(
        val fom: LocalDate,
        val tom: LocalDate,
        val gradsokning: Boolean,
        val faktor: Double,
        val venteperiodeStartDato: LocalDate,
    )
}
