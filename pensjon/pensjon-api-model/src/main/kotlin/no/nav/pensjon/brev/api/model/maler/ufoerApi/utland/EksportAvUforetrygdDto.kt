package no.nav.pensjon.brev.api.model.maler.ufoerApi.utland

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class EksportAvUforetrygdDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EksportAvUforetrygdDto.PesysData> {
    data class PesysData(
        val pe: PEgruppe10,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,

        val fullEksport: Boolean,
        val sumUtOgTillegg: Kroner,
        val virkningstidspunkt: LocalDate,
        val muligTilbakekrevingVarsel: Boolean,
        val ensligOgMinstesats: Boolean,
        val minstesatsTilBruker: Double,
        val barnetilleggAvtaleland: Boolean,
        val flereBarn: Boolean,
        val innvilgetBT: Boolean,
        val btInnvilgetMenIngenUtbetaling: Boolean,
        val gjtEndret: Boolean,
        val gjtUendret: Boolean,
        val gjtAvkortet: Int,
        val eosNordisk: Boolean,
    ) : FagsystemBrevdata
}
