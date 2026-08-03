package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OkningUforegradDto.Saksbehandlervalg
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class OkningUforegradDto(
    override val saksbehandlerValg: Saksbehandlervalg,
    override val pesysData: PesysData,
    ) : RedigerbarBrevdata<Saksbehandlervalg, OkningUforegradDto.PesysData> {

    data class Saksbehandlervalg(
        @DisplayText("Periodisert inntekt barnetillegg")
        val periodisertInntekt: PeriodisertInntektBarnetillegg?,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val pe: PEgruppe10,
        val kravFremsattDato: LocalDate?,
        val oifuVedVirkningstidspunkt: Kroner?,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
        val nyeInnvilgedeBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val nyeAvslagBarnetillegg: List<BarnetilleggUTDto> = emptyList(),
        val sisteTrygdetidsgrunnlag: Trygdetidsgrunnlag?,
        val hjemler: Set<String>
    ) : FagsystemBrevdata

    data class Trygdetidsgrunnlag(
        val fom: LocalDate,
        val tom: LocalDate,
    )
}
