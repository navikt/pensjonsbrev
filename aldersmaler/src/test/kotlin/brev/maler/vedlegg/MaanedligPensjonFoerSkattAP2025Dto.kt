package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createMaanedligPensjonFoerSkattAP2025Dto() = MaanedligPensjonFoerSkattAP2025Dto(
    beregnetPensjonPerManedGjeldende =
        MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
            inntektspensjon = Kroner(1000),
            totalPensjon = Kroner(2000),
            garantipensjon = Kroner(1000),
            minstenivaIndividuell = Kroner(1000),
            virkDatoFom = LocalDate.now(),
            virkDatoTom = null,
        ),
    beregnetPensjonperManed = listOf(),
    kravVirkFom = LocalDate.now(),
)