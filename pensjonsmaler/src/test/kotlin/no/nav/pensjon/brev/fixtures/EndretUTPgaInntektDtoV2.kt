package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUTPgaInntektDtoV2.*
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
            nettoRestbelop = 2000000,
            totalNettoInnevarendeAr = 200000,
            okningUforegradVedArsjoring = true,
        ),
        barnetilleggFellesbarn = BarnetilleggFellesbarn(
            netto = 1000,
            endringsbelop = 1000,
            totalInntektBruktIAvkortning = 1000,
            inntektBruker = 1000,
            inntektAnnenForelder = 1000,
            fribelop = 1000,
            inntektstak = 1000,
            antallBarn = 1,
            periodisert = false,
            totalNettoInnevarendeAr = 1000
        ),
        barnetilleggSaerkullsbarn = BarnetilleggSaerkullsbarn(
            netto = 1000,
            endringsbelop = 1000,
            fribelop = 1000,
            inntektstak = 1000,
            antallBarn = 1,
            periodisert = false,
            totalNettoInnevarendeAr = 1000,
            inntektBruktIAvkortning = 1000
        ),
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
        gjtEndret = false
    )