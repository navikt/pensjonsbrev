package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createOpplysningerBruktIBeregningenLegacyDto() =
    OpplysningerBruktIBeregningenLegacyDto(
        tabellUfoereOpplysninger = OpplysningerBruktIBeregningenLegacyDto.TabellufoereOpplysningerDto(
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
            inntektsgrenseErUnderTak = true,
            inntektEtterUfoereGjeldendeBeloep = Kroner(1),
            antallAarOver1G = 4,
            antallAarOverInntektIAvtaleland = 3,
            beregningUfore_andelYtelseAvOIFU = 5,
            beregningUfore_BeregningVirkningDatoFom = LocalDate.of(2020, 1, 1),
            beregningUfore_prosentsatsOIFUForTak = 2,
            kravGjelderFoerstegangsbehandlingBosattUtland = true,
            ufoeretrygd_reduksjonsgrunnlag_gradertOppjustertIFU = Kroner(10)
        ),
        opplysningerOmBarnetillegg = Fixtures.create(),
        inntektsgrenseAar = Kroner(100),
        inntektstak = Kroner(1001),
        beregnetUTPerMaanedGjeldendeVirkFom = LocalDate.of(2020, 1, 1),
        beregnetUTPerMaanedGjeldendeGrunnbeloep = Kroner(1),
        PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor = listOf(Fixtures.create()),
        PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS = listOf(Fixtures.create()),
        PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral = listOf(Fixtures.create()),
    )

fun createOpplysningerOmMinstetillegg() = OpplysningerBruktIBeregningenLegacyDto.OpplysningerOmMinstetilleggDto(
    minsteytelseGjeldendeSats = null,
    ungUfoerGjeldende_erUnder20Aar = false,
    ufoeretrygdGjeldende = Fixtures.create(),
    inntektFoerUfoereGjeldende = Fixtures.create(),
    inntektsgrenseErUnderTak = true,
)

fun createOpplysningerBruktIBeregningenLegacyDto_TrygdetidNor() =
    OpplysningerBruktIBeregningenLegacyDto.TrygdetidNor(
        fom = LocalDate.of(2020, 1, 1),
        tom = LocalDate.of(2020, 2, 2),
    )

fun createOpplysningerBruktIBeregningenLegacyDto_TrygdetidEOS() =
    OpplysningerBruktIBeregningenLegacyDto.TrygdetidEOS(
        fom = LocalDate.of(2020, 1, 1),
        tom = LocalDate.of(2020, 2, 2),
        land = "USA"
    )

fun createOpplysningerBruktIBeregningenLegacyDto_TrygdetidBilateral() =
    OpplysningerBruktIBeregningenLegacyDto.TrygdetidBilateral(
        fom = LocalDate.of(2020, 1, 1),
        tom = LocalDate.of(2020, 2, 2),
        land = "USA"
    )

fun createOpplysningerBruktIBeregningenLegacyDto_OpplysningerOmBarnetilleggDto() =
    OpplysningerBruktIBeregningenLegacyDto.OpplysningerOmBarnetilleggDto(
        barnetillegg = Fixtures.create(),
        anvendtTrygdetid = 123,
        harYrkesskade = true,
        harKravaarsakEndringInntekt = true,
        fraOgMedDatoErNesteAar = false,
    )