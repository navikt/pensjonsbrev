package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import java.time.LocalDateTime

@Suppress("unused")
data class AvslagGjenlevendepensjonDto (
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<AvslagGjenlevendepensjonDto.SaksbehandlerValg, AvslagGjenlevendepensjonDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Velg folketrygdloven paragraf:")
        val folketrygdlovenParagraf: FolketrygdlovenParagraf,
    ) : SaksbehandlerValgBrevdata {
        enum class FolketrygdlovenParagraf {
            @DisplayText("$ 17-2 første eller tredjeledd")
            paragraf17_2_foersteEllerTredje_ledd,

            @DisplayText("§ 17-2 andre ledd")
            paragraf17_2_andre_ledd,

            @DisplayText("§ 17-3")
            paragraf17_3,

            @DisplayText("§ 17-5")
            paragraf17_5,

            @DisplayText("§ 17-10")
            paragraf17_10,
        }
    }
    data class PesysData(
        val mottattDato: LocalDateTime,  //TODO  ikke støtte for PEbrevgruppe2
    ) : FagsystemBrevdata
}