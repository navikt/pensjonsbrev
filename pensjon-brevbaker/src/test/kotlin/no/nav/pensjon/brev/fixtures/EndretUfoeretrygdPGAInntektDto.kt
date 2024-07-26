package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createEndretUfoeretrygdPGAInntektDto() =
    EndretUfoeretrygdPGAInntektDto(
        pe = Fixtures.create(),
        opplysningerBruktIBeregningenLegacyDto = Fixtures.create(),
    )