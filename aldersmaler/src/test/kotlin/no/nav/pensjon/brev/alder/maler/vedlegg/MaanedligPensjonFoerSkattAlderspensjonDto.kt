package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import java.time.LocalDate
import java.time.Month

fun createMaanedligPensjonFoerSkattAlderspensjonDto(regelverkType: AlderspensjonRegelverkType = AlderspensjonRegelverkType.AP2016) =
    MaanedligPensjonFoerSkattAlderspensjonDto(
        krav = MaanedligPensjonFoerSkattAlderspensjonDto.Krav(
            virkDatoFom = LocalDate.of(2022, Month.MARCH, 1)
        ),
        alderspensjonGjeldende = MaanedligPensjonFoerSkattAlderspensjonDto.AlderspensjonGjeldende(
            regelverkType = regelverkType
        ),
        alderspensjonPerManed = listOf(
            createAlderspensjonPerManed(),
            createAlderspensjonPerManed(),
            createAlderspensjonPerManed()
        ),
    )