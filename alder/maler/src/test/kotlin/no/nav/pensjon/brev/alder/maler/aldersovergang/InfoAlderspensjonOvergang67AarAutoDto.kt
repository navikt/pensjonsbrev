package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.alder.model.BorMedSivilstand
import no.nav.pensjon.brev.alder.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.alder.model.aldersovergang.InfoAlderspensjonOvergang67AarAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createInfoAlderspensjonOvergang67AarAutoDto() =
    InfoAlderspensjonOvergang67AarAutoDto(
        ytelseForAldersovergang = YtelseForAldersovergangKode.UT_GRAD,
        borMedSivilstand = BorMedSivilstand.GIFT_LEVER_ADSKILT,
        over2G = true,
        kronebelop2G = Kroner(260320),
    )
