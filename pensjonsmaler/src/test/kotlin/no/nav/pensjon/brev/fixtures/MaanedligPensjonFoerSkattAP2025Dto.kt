package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

private fun createAlderspensjonPerManedAP2025() = MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
    virkDatoFom = LocalDate.of(2020, 1, 1),
    virkDatoTom = LocalDate.of(2020, 1, 2),
    minstenivaIndividuell = Kroner(12),
    inntektspensjon = Kroner(12),
    garantipensjon = Kroner(12),
    totalPensjon = Kroner(12),
    garantipensjonInnvilget = Kroner(10),
)

fun createMaanedligPensjonFoerSkattAP2025Dto() =
    MaanedligPensjonFoerSkattAP2025Dto(
        beregnetPensjonPerManedGjeldende = createAlderspensjonPerManedAP2025(),
        beregnetPensjonperManed = listOf(createAlderspensjonPerManedAP2025(), createAlderspensjonPerManedAP2025()),
        kravVirkFom = LocalDate.of(2025, Month.JANUARY, 1)
    )