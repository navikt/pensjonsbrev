package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import java.time.LocalDate

@Suppress("unused")
data class AvslagGjenlevendepensjonDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<AvslagGjenlevendepensjonDto.PesysData> {

    enum class FolketrygdlovenParagraf(override val displayText: String) : SaksbehandlerValgEnum {
        paragraf17_2_foersteEllerTredje_ledd("$ 17-2 første eller tredjeledd"),
        paragraf17_2_andre_ledd("§ 17-2 andre ledd"),
        paragraf17_3("§ 17-3"),
        paragraf17_5("§ 17-5"),
        paragraf17_10("§ 17-10"),
    }

    data class PesysData(
        val kravMottattDato: LocalDate,
    ) : FagsystemBrevdata
}
