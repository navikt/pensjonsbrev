package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto

fun createInformasjonOmGjenlevenderettigheterDto() = InformasjonOmGjenlevenderettigheterDto(
    saksbehandlerValg = InformasjonOmGjenlevenderettigheterDto.SaksbehandlerValg(
        infoOmstillingsstoenad = false,
        infoHvordanSoekeOmstillingsstoenad = false,
        infoVilkaarSkiltGjenlevende = false,
        gjenlevendeHarBarnUnder18MedAvdoed = false,
        gjenlevenderHarEllerKanHaAFPIOffentligSektor = false,
        gjenlevevendeHarAfpOgUttaksgradPaaApSattTilNull = false,
        vilkarForGjenlevendeytelsen = InformasjonOmGjenlevenderettigheterDto.VilkarForGjenlevendeytelsen.GJENLEVENDE_EPS,
        hvorBorBruker = InformasjonOmGjenlevenderettigheterDto.HvorBorBruker.GJENLEVENDE_BOR_I_AVTALELAND
    ),
    pesysData = InformasjonOmGjenlevenderettigheterDto.PesysData(
        sakstype = Sakstype.ALDER,
        gjenlevendesAlder = 65,
    )
)