package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.alder.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAlderspensjonOvergang67AarAutoDto

fun createInfoAlderspensjonOvergang67AarAutoDto() =
    InfoAlderspensjonOvergang67AarAutoDto(
        ytelseForAldersovergang = YtelseForAldersovergangKode.UT_GRAD,
    )
