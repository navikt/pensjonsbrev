package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.api.model.maler.alderApi.InfoAlderspensjonOvergang67AarAutoDto

fun createInfoAlderspensjonOvergang67AarAutoDto() =
    InfoAlderspensjonOvergang67AarAutoDto(
        ytelseForAldersovergang = YtelseForAldersovergangKode.FAM_PL,
    )
