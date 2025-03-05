package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.Uforetrygd
import java.time.LocalDate

fun createEndretUfoeretrygdPGAInntektDto2() =
    EndretUfoeretrygdPGAInntektDto2(
        uforetrygd = Uforetrygd(
            netto = 1,
            endringsbelop = 1,
            uforegrad = 100,
            inntektstak = 1,
            inntektsgrense = 1
        ),
        barnetilleggFellesbarn = null,
        barnetilleggSaerkullsbarn = null,
        gjenlevendetillegg = null,
        forventetInntekt = 1,
        virkningFom = LocalDate.now(),
        totalNetto = 1,
        datoForNormertPensjonsalder = LocalDate.now(),
        sokerMottarApIlaAret = false,
        btfbEndret = false,
        btsbEndret = false
    )