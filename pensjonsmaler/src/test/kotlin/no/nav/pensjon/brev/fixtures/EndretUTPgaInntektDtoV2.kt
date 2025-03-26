package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2.Uforetrygd
import java.time.LocalDate

fun createEndretUTPgaInntektDtoV2() =
    EndretUTPgaInntektDtoV2(
        uforetrygd = Uforetrygd(
            netto = 1,
            endringsbelop = 1,
            uforegrad = 100,
            inntektstak = 1,
            inntektsgrense = 1,
            inntektBruktIAvkortning = 0
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
        btsbEndret = false,
        settingAvInntektForNesteAar = false,
    )