package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL

@Suppress("unused")
data class InformasjonOmGjenlevenderettigheterDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: PesysData,
) : BrevdataMedSaksbehandlerValg<InformasjonOmGjenlevenderettigheterDto.PesysData> {

    data class PesysData(
        val sakstype: Sakstype,
        val gjenlevendesAlder: Int,
    ) : FagsystemBrevdata

    enum class HvorBorBruker(override val displayText: String) : SaksbehandlerValgEnum {
        GJENLEVENDE_BOR_I_NORGE_ELLER_IKKE_AVTALELAND("Gjenlevende bor i Norge eller et ikke-avtaleland"),
        GJENLEVENDE_BOR_I_AVTALELAND("Gjenlevende bor i et avtaleland"),
    }

    enum class VilkarForGjenlevendeytelsen(override val displayText: String) : SaksbehandlerValgEnum {
        GJENLEVENDE_EPS("Vilkår for gjenlevende ektefelle, partner eller samboer"),
        GJENLEVENDE_SKILT("Vilkår for gjenlevende skilt person"),
    }
}