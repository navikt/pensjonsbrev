package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createEndretUfoeretrygdPGAInntektDto() =
    EndretUfoeretrygdPGAInntektDto(
        PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus =Kroner(9155),
        PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus =Kroner(9191),
        PE_UT_NettoAkk_pluss_NettoRestAr = Kroner(5910),
        PE_UT_VirkningstidpunktArMinus1Ar = 2019,
        PE = Fixtures.create(),
        opplysningerBruktIBeregningenLegacyDto = Fixtures.create(),
    )