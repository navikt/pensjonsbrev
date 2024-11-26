package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata


@Suppress("unused")
data class InfoAlderspensjonOvergang67AarAutoDto(
val ytelseForAldersovergang: YtelseForAldersovergang,
) : BrevbakerBrevdata

data class YtelseForAldersovergang(
    val harYtelseAvkortetGjenlevendepensjon: Boolean, // fag=aldersovergang67ar=ytelseForAldersovergang="GJP_AVKORT"
    val harYtelseFamiliePleie: Boolean, // fag=aldersovergang67ar=ytelseForAldersovergang="FAM_PL"
    val harYtelseFullGjenlevendepensjon: Boolean, // fag=aldersovergang67ar=ytelseForAldersovergang="GJP_FULL"
    val harYtelseFullUforetrygd: Boolean, // fag=aldersovergang67ar=ytelseForAldersovergang="UT"
    val harYtelseGradertUforetrygd: Boolean, // fag=aldersovergang67ar=ytelseForAldersovergang="UT_GRAD"
    val harYtelseGradertUforetrygdKombinertAlderspensjon:Boolean, // fag=aldersovergang67ar=ytelseForAldersovergang="UT_AP_GRAD"
    val harYtelseIngen: Boolean, // fag=aldersovergang67ar=ytelseForAldersovergang="INGEN_YT"
)

