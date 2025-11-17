package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.DisplayText

@Suppress("unused")
data class InformasjonOmGjenlevenderettigheterDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData
) : RedigerbarBrevdata<SaksbehandlerValg, InformasjonOmGjenlevenderettigheterDto.PesysData> {

    data class PesysData(
        val sakstype: Sakstype,
        val gjenlevendesAlder: Int,
    ) : FagsystemBrevdata

    data class SaksbehandlerValg(

        @DisplayText("Hvis gradert uføretrygd, info omstillingsstønad")
        val infoOmstillingsstoenad: Boolean,

        @DisplayText("Hvis gradert uføretrygd, info søke omstillingsstønad")
        val infoHvordanSoekeOmstillingsstoenad: Boolean,

        @DisplayText("Hvis gradert uføretrygd, info vilkår skilt gjenlevende")
        val infoVilkaarSkiltGjenlevende: Boolean,

        @DisplayText("Gjenlevende har barn under 18 år sammen med avdøde")
        val gjenlevendeHarBarnUnder18MedAvdoed: Boolean,

        @DisplayText("Gjenlevende har eller kan ha AFP i offentlig sektor")
        val gjenlevenderHarEllerKanHaAFPIOffentligSektor: Boolean,

        @DisplayText("Gjenlevende har AFP privat og uttaksgrad på AP satt til 0")
        val gjenlevevendeHarAfpOgUttaksgradPaaApSattTilNull: Boolean,

        @DisplayText("Vilkår for gjenlevendeytelsen")
        val vilkarForGjenlevendeytelsen: VilkarForGjenlevendeytelsen?,

        @DisplayText("Hvor bor bruker")
        val hvorBorBruker: HvorBorBruker?
    ) : SaksbehandlerValgBrevdata

    enum class HvorBorBruker {
        @DisplayText("Gjenlevende bor i Norge eller et ikke-avtaleland")
        GJENLEVENDE_BOR_I_NORGE_ELLER_IKKE_AVTALELAND,

        @DisplayText("Gjenlevende bor i et avtaleland")
        GJENLEVENDE_BOR_I_AVTALELAND,
    }

    enum class VilkarForGjenlevendeytelsen {
        @DisplayText("Vilkår for gjenlevende ektefelle, partner eller samboer")
        GJENLEVENDE_EPS,

        @DisplayText("Vilkår for gjenlevende skilt person")
        GJENLEVENDE_SKILT,
    }
}