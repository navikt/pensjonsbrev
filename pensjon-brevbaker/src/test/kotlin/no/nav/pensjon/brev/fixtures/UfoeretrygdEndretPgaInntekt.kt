package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDto
import java.time.LocalDate

fun createUfoeretrygdEndretPgaInntektDto() =
    UfoeretrygdEndretPgaInntektDto(
        barnetilleggFellesbarn = UfoeretrygdEndretPgaInntektDto.BarnetilleggFellesbarn(
            gammeltBeloep = Kroner(10000),
            harBarnetillegg = true,
            inntektErPeriodisert = true,
            fribeloepErPeriodisert = true,
            nyttBeloep = Kroner(4000),
            inntektBruktIAvkortning = Kroner(5000),
            gjelderFlereBarn = true,
        ),
        barnetilleggSaerkullsbarn = UfoeretrygdEndretPgaInntektDto.BarnetilleggSaerkullsbarn(
            harBarnetillegg = true,
            inntektErPeriodisert = true,
            fribeloepErPeriodisert = true,
            gammeltBeloep = Kroner(3000),
            nyttBeloep = Kroner(4000),
            inntektBruktIAvkortning = Kroner(5000),
            gjelderFlereBarn = true,
        ),
        sivilstand =Sivilstand.GIFT,
        ufoeretrygd = UfoeretrygdEndretPgaInntektDto.Ufoeretrygd(
            nyttBeloep = Kroner(1000),
            gammeltBeloep = Kroner(2000),
            beloepNetto = Kroner(3000),
            ufoeregrad = 100.0,
            utbetalingsgrad = 100.0,
            inntektsgrense = Kroner(4000),
            forventetInntekt = Kroner(3300),
            inntektstak = Kroner(5000),
            beloepErRedusert = false,
            avkortningsbeloepPerAar = Kroner(2000)
        ),
        virkningsdatoFraOgMed = LocalDate.of(2020,1,1),
        aarFoerVirkningsAar = Year(2019),
        fyller67IVirkningsAar = true,
        harEktefelletillegg = true,
        harGjenlevendetillegg = false,
        opplysningerBruktIBeregningUTDto = createOpplysningerBruktIBeregningUTDto()
    )