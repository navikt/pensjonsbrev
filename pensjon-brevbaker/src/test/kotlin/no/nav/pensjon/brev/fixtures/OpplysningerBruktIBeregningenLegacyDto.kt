package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createOpplysningerBruktIBeregningenLegacyDto() =
    OpplysningerBruktIBeregningenLegacyDto(
        tabellUfoereOpplysninger = OpplysningerBruktIBeregningenLegacyDto.TabellufoereOpplysningerLegacyDto(
            barnetilleggGjeldende = Fixtures.create(),
            beregnetUTPerManedGjeldende = Fixtures.create(),
            inntektFoerUfoereGjeldende = Fixtures.create(),
            inntektsAvkortingGjeldende = Fixtures.create(),
            trygdetidsdetaljerGjeldende = Fixtures.create(),
            ufoeretrygdGjeldende = Fixtures.create(),
            yrkesskadeGjeldende = Fixtures.create(),
            borMedSivilstand = BorMedSivilstand.PARTNER,
            brukersSivilstand = Sivilstand.GIFT,
            harMinsteytelse = true,
            erUngUfoer = true,
            inntektEtterUfoereGjeldendeBeloep = Kroner(1),
        ),
        opplysningerOmBarnetillegg = Fixtures.create(),
        inntektsgrenseAar = Kroner(100),
        inntektstak = Kroner(1001),
        beregnetUTPerMaanedGjeldendeVirkFom = LocalDate.of(2020, 1, 1),
        beregnetUTPerMaanedGjeldendeGrunnbeloep = Kroner(1),
        PE = Fixtures.create()
    )

fun createOpplysningerOmMinstetillegg() = OpplysningerBruktIBeregningenLegacyDto.OpplysningerOmMinstetilleggDto(
    minsteytelseGjeldendeSats = null,
    ungUfoerGjeldende_erUnder20Aar = false,
    ufoeretrygdGjeldende = Fixtures.create(),
    inntektFoerUfoereGjeldende = Fixtures.create(),
    inntektsgrenseErUnderTak = true,
)

fun createOpplysningerBruktIBeregningenLegacyDto_OpplysningerOmBarnetilleggDto() =
    OpplysningerBruktIBeregningenLegacyDto.OpplysningerOmBarnetilleggDto(
        barnetillegg = Fixtures.create(),
        anvendtTrygdetid = 123,
        harYrkesskade = true,
        harKravaarsakEndringInntekt = true,
        fraOgMedDatoErNesteAar = false,
    )