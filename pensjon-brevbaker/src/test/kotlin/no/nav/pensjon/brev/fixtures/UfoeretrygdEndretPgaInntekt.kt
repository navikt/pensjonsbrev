package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDto
import java.time.LocalDate

fun createUfoeretrygdEndretPgaInntektDto() =
    UfoeretrygdEndretPgaInntektDto(
        beloepGammelUfoeretrygd = Kroner(44),
        beloepNyUfoeretrygd = Kroner(55),
        beloepGammelBarnetillegSaerkullsbarn = Kroner(44),
        beloepNyBarnetillegSaerkullsbarn = Kroner(55),
        beloepGammelBarnetillegFellesBarn = Kroner(44),
        beloepNyBarnetillegFellesBarn = Kroner(55),
        harInnvilgetBarnetilleggFellesBarn = true,
        harInnvilgetBarnetilleggSaerkullsBarn = true,
        brukersSivilstandUfoeretrygd = Sivilstand.GIFT,
        virkningFraOgMed = LocalDate.of(2020, 1, 1),
        antallFellesBarn = 5,
        forventetInntektAvkortet = Kroner(0),
        forventetInntektAvkoret = Kroner(0),
        aarFoerVirkningsAar = Year(2019),
        ufoeregrad = 5.5,
        utbetalingsgrad = 5.2,
        fyller67IVirkningsAar = true,
        barnetilleggSaerkullsbarnInntektBruktIAvkortning = Kroner(1234),
        barnetilleggFellesbarnInntektBruktIAvkortning = Kroner(1234),
        antallSaerkullsbarn = 5,
    )