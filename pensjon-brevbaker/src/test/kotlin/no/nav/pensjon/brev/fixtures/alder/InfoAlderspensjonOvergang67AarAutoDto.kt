package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.YtelseForAldersovergang

fun createInfoAlderspensjonOvergang67AarAutoDto() =
    InfoAlderspensjonOvergang67AarAutoDto(
        ytelseForAldersovergang = YtelseForAldersovergang(
            harYtelseAvkortetGjenlevendepensjon = false,  // "GJP_AVKORT"
            harYtelseFamiliePleie = false,  // "FAM_PL"
            harYtelseFullGjenlevendepensjon = false,  // "GJP_FULL"
            harYtelseFullUforetrygd = true,  // "UT"
            harYtelseGradertUforetrygd = false,  // "UT_GRAD"
            harYtelseGradertUforetrygdKombinertAlderspensjon = false,  // "UT_AP_GRAD"
            harYtelseIngen = false,  // "INGEN_YT"
        )
    )