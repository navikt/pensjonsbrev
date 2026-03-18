package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class InnvilgelseUfoeretrygdDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
    ) : RedigerbarBrevdata<EmptySaksbehandlerValg, InnvilgelseUfoeretrygdDto.PesysData> {
    data class PesysData(
        val pe: PEgruppe10,
        val oifuVedVirkningstidspunkt: Kroner?,
        val maanedligUfoeretrygdFoerSkatt: MaanedligUfoeretrygdFoerSkattDto?,
        val orienteringOmRettigheterUfoere: OrienteringOmRettigheterUfoereDto,
        val barnetilleggInnvilget: List<Barnetillegg> = emptyList(),
        val barnetilleggAvslatt: List<Barnetillegg> = emptyList()
    ) : FagsystemBrevdata

    data class Barnetillegg(val fodselsdato: LocalDate, val resultat: BarnetilleggResultatCode)
    enum class BarnetilleggResultatCode {
        INNVILGET,
        BT_GITT_TIL_ANNEN,
        ANNEN_FORLD_RETT_BT,
        BT_OVER_18,
        MINDRE_ETT_AR_BT_FLT,
        BT_INNT_OVER_1G,
        BRK_FORSO_IKKE_BARN,
        BRUKER_FLYTTET_IKKE_AVT_LAND,
        BARN_FLYTTET_IKKE_AVT_LAND,
        BARN_OPPH_IKKE_AVT_LAND
    }
}