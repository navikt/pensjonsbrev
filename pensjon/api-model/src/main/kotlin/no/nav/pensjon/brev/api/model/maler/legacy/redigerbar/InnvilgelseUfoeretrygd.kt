package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDate

data class InnvilgelseUfoeretrygdDto(
    override val saksbehandlerValg: Saksbehandlervalg,
    override val pesysData: PesysData,
    ) : RedigerbarBrevdata<InnvilgelseUfoeretrygdDto.Saksbehandlervalg, InnvilgelseUfoeretrygdDto.PesysData> {

    data class Saksbehandlervalg(
        @DisplayText("Info om rett til barnetillegg")
        val barnetilleggInfo: Boolean,
        @DisplayText("Refusjon")
        val refusjon: Boolean,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val pe: PEgruppe10,
        val oifuVedVirkningstidspunkt: Kroner?,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
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