package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmGjenlevenderettigheterDto

fun createInformasjonOmGjenlevenderettigheterDto() = InformasjonOmGjenlevenderettigheterDto(
    saksbehandlerValg = lagSaksbehandlervalg(
        "infoOmstillingsstoenad" to false,
        "infoHvordanSoekeOmstillingsstoenad" to false,
        "infoVilkaarSkiltGjenlevende" to false,
        "gjenlevendeHarBarnUnder18MedAvdoed" to false,
        "gjenlevenderHarEllerKanHaAFPIOffentligSektor" to false,
        "gjenlevevendeHarAfpOgUttaksgradPaaApSattTilNull" to false,
        "vilkarForGjenlevendeytelsen" to InformasjonOmGjenlevenderettigheterDto.VilkarForGjenlevendeytelsen.GJENLEVENDE_EPS,
        "hvorBorBruker" to InformasjonOmGjenlevenderettigheterDto.HvorBorBruker.GJENLEVENDE_BOR_I_AVTALELAND,
    ),
    pesysData = InformasjonOmGjenlevenderettigheterDto.PesysData(
        sakstype = Sakstype.ALDER,
        gjenlevendesAlder = 65,
    )
)