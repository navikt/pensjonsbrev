package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.YtelseForAldersovergang

fun createInfoAlderspensjonOvergang67AarAutoDto() =
    InfoAlderspensjonOvergang67AarAutoDto(
        ytelseForAldersovergang = YtelseForAldersovergang(
            harYtelseAvkortetGjenlevendepensjon = false,
            harYtelseFamiliePleie = false,
            harYtelseFullGjenlevendepensjon = false,
            harYtelseFullUforetrygd = true,
            harYtelseGradertUforetrygd = false,
            harYtelseGradertUforetrygdKombinertAlderspensjon = false,
            harYtelseIngen = false,
        )
    )