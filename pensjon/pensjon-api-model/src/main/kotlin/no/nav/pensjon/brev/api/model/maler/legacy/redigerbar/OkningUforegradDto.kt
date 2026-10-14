package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.felles.Barnetillegg
import no.nav.pensjon.brev.api.model.maler.felles.Beregning
import no.nav.pensjon.brev.api.model.maler.felles.Grunnlag
import no.nav.pensjon.brev.api.model.maler.felles.Institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.felles.Kravhode
import no.nav.pensjon.brev.api.model.maler.felles.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.felles.Tillegg
import no.nav.pensjon.brev.api.model.maler.felles.UforeVedtaksinfo
import no.nav.pensjon.brev.api.model.maler.felles.Vedtak
import no.nav.pensjon.brev.api.model.maler.felles.VilkarsvurderingFullstendig
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
        val vedtaksinfo: UforeVedtaksinfo,
        val kravFremsattDato: LocalDate?,
        val oifuVedVirkningstidspunkt: Kroner?,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
        val nyeInnvilgedeBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val nyeAvslagBarnetillegg: List<BarnetilleggMedSammeBegrunnelsePaSammeTidDto> = emptyList(),
        val sisteTrygdetidsgrunnlag: Trygdetidsgrunnlag?,
        val hjemler: Set<String>,
        val fribelopsperioder: List<Fribelopsperiode>,
        val fribelop: Kroner,
        val harVTA: Boolean,
        val forrigeUforegrad: Int,
        val forrigeUforetidspunkt: LocalDate,

        val krav: Kravhode? = null,
        val vedtak: Vedtak? = null,
        val beregning: Beregning? = null,
        val grunnlag: Grunnlag? = null,
        val institusjonsopphold: Institusjonsopphold? = null,
        val reduksjonsgrunnlag: Reduksjonsgrunnlag? = null,
        val tillegg: Tillegg? = null,
        val barnetillegg: Barnetillegg? = null,
        val vilkarsvurdering: VilkarsvurderingFullstendig? = null,
    ) : FagsystemBrevdata

    data class Trygdetidsgrunnlag(
        val fom: LocalDate,
        val tom: LocalDate,
    )

    data class Fribelopsperiode(
        val fom: LocalDate,
        val tom: LocalDate,
        val gradsokning: Boolean,
        val faktor: Double,
        val grunnbelop: Kroner,
        val venteperiodeStartDato: LocalDate,
    )
}
