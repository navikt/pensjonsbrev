package no.nav.pensjon.brev.alder.maler.aldersovergang

import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


fun createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() =
    EndringAvAlderspensjonFordiDuFyller75AarAutoDto(
        kravVirkDatoFom = LocalDate.of(2025, 6, 1),
        regelverkType = AlderspensjonRegelverkType.AP2011,
        totalPensjon = Kroner(25000),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        opplysningerBruktIBeregningenAlder = createOpplysningerBruktIBeregningAlderDto(),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
    )