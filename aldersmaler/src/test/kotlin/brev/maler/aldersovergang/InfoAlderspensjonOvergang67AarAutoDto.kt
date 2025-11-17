package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.model.alder.YtelseForAldersovergangKode
import no.nav.pensjon.brev.model.alder.aldersovergang.InfoAlderspensjonOvergang67AarAutoDto

fun createInfoAlderspensjonOvergang67AarAutoDto() =
    InfoAlderspensjonOvergang67AarAutoDto(
        ytelseForAldersovergang = YtelseForAldersovergangKode.UT_GRAD,
    )
