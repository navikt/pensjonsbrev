package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
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
            inntektBruktIAvkortning = 0,
            nettoPerAr = 300000,
            nettoAkkumulert = 1000000,
            nettoRestbelop = 2000000
        ),
        barnetilleggFellesbarn = null,
        barnetilleggSaerkullsbarn = null,
        gjenlevendetillegg = null,
        forventetInntekt = 1,
        virkningFom = LocalDate.now(),
        totalNetto = 1,
        totalNettoAr = 12,
        datoForNormertPensjonsalder = LocalDate.now(),
        sokerMottarApIlaAret = false,
        btfbEndret = false,
        btsbEndret = false,
        brukerBorINorge = true,
        pe = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )