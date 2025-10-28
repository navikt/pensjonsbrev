package brev.maler.aldersovergang

import brev.maler.createMaanedligPensjonFoerSkatt
import brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.aldersovergang.EndringAvAlderspensjonFordiDuFyller75AarAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


fun createEndringAvAlderspensjonFordiDuFyller75AarAutoDto() =
    EndringAvAlderspensjonFordiDuFyller75AarAutoDto(
        kravVirkDatoFom = LocalDate.of(2025, 6, 1),
        regelverkType = AlderspensjonRegelverkType.AP2011,
        totalPensjon = Kroner(25000),
        dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        opplysningerBruktIBeregningenAlder = createOpplysningerBruktIBeregningAlderDto()
    )