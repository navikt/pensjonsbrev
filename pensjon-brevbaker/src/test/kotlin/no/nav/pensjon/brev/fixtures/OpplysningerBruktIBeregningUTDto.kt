package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

fun createOpplysningerBruktIBeregningUTDto() =
    OpplysningerBruktIBeregningUTDto(
        avdoed = Fixtures.create(),
        barnetillegg = Fixtures.create(),
        beregnetUTPerManed = Fixtures.create(),
        borIUtlandet = false,
        fraOgMedDatoErNesteAar = false,
        harEktefelletilleggInnvilget = false,
        harKravaarsakEndringInntekt = true,
        inntektEtterUfoereBeloepIEU = Kroner(0),
        inntektFoerUfoere = Fixtures.create(),
        inntektsAvkorting = Fixtures.create(),
        kravAarsakType = KravAarsakType.ENDRET_OPPTJENING,
        minsteytelseSats = 50.0,
        sivilstand = Sivilstand.PARTNER,
        trygdetid = Fixtures.create(),
        ufoeretrygd = Fixtures.create(),
        ungUfoerErUnder20Aar = false,
        yrkesskade = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetillegg() =
    OpplysningerBruktIBeregningUTDto.Barnetillegg(
        fellesbarn = Fixtures.create(),
        saerkullsbarn = Fixtures.create(),
        foedselsdatoPaaBarnTilleggetGjelder = listOf(
            LocalDate.of(2000, 1, 1),
            LocalDate.of(2000, 2, 2),
            LocalDate.of(2000, 3, 3),
            LocalDate.of(2000, 4, 4),
        )
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggFellesbarn() =
    OpplysningerBruktIBeregningUTDto.Barnetillegg.Fellesbarn(
        avkortningsbeloepAar = Kroner(40000),
        beloepAarBrutto = Kroner(240000),
        beloepAarNetto = Kroner(120000),
        beloepBrutto = Kroner(20000),
        beloepFratrukketAnnenForeldersInntekt = Kroner(35000),
        beloepNetto = Kroner(10000),
        erRedusertMotinntekt = true,
        fribeloep = Kroner(20000),
        fribeloepEllerInntektErPeriodisert = false,
        harFlereBarn = true,
        inntektAnnenForelder = Kroner(200000),
        inntektBruktIAvkortning = Kroner(50000),
        inntektOverFribeloep = Kroner(25000),
        inntektstak = Kroner(200000),
        justeringsbeloepAar = Kroner(120000),
        samletInntektBruktIAvkortning = Kroner(500000),
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggSaerkullsbarn() =
    OpplysningerBruktIBeregningUTDto.Barnetillegg.Saerkullsbarn(
        avkortningsbeloepAar = Kroner(5000),
        beloepAarBrutto = Kroner(240000),
        beloepAarNetto = Kroner(120000),
        beloepBrutto = Kroner(20000),
        beloepNetto = Kroner(10000),
        erRedusertMotinntekt = true,
        fribeloep = Kroner(35000),
        fribeloepEllerInntektErPeriodisert = false,
        harFlereBarn = true,
        inntektBruktIAvkortning = Kroner(200000),
        inntektOverFribeloep = Kroner(40000),
        inntektstak = Kroner(220000),
        justeringsbeloepAar = Kroner(15000),
    )

fun createOpplysningerBruktIBeregningUTDtoBeregnetUTPerManed() =
    OpplysningerBruktIBeregningUTDto.BeregnetUTPerManed(
        brukerErFlyktning = false,
        brukersSivilstand = Sivilstand.GIFT,
        grunnbeloep = Kroner(111477),
        virkDatoFom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoInntektFoerUfoere() =
    OpplysningerBruktIBeregningUTDto.InntektFoerUfoere(
        erSannsynligEndret = false,
        inntektFoerUfoer = Kroner(0),
        inntektFoerUfoereBegrunnelse = InntektFoerUfoereBegrunnelse.STDBEGR_12_8_2_3,
        oppjustertInntektFoerUfoer = Kroner(0),
        opptjeningUfoeretrygd = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoInntektsAvkorting() =
    OpplysningerBruktIBeregningUTDto.InntektsAvkorting(
        forventetInntektAar = Kroner(0),
        inntektsgrenseAar = Kroner(0),
        inntektstak = Kroner(0),
    )

fun createOpplysningerBruktIBeregningUTDtoTrygdetid() =
    OpplysningerBruktIBeregningUTDto.Trygdetid(
        bilateralTrygdePerioder = listOf(Fixtures.create(), Fixtures.create()),
        eosTrygdePerioder = listOf(Fixtures.create(), Fixtures.create()),
        norskTrygdetidPerioder = listOf(Fixtures.create(), Fixtures.create()),
        trygdetidsdetaljer = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoNorskTrygdetidPeriode() =
    OpplysningerBruktIBeregningUTDto.NorskTrygdetidPeriode(
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )


fun createOpplysningerBruktIBeregningUTDtoUtenlandsTrygdetidPeriode() =
    OpplysningerBruktIBeregningUTDto.UtenlandsTrygdetidPeriode(
        trygdetidBilateralLand = "Storbritannia",
        trygdetidEOSLand = "Nederland",
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoTrygdetidTrygdetidsdetaljer() =
    OpplysningerBruktIBeregningUTDto.Trygdetid.Trygdetidsdetaljer(
        anvendtTT = 40,
        beregningsmetode = Beregningsmetode.FOLKETRYGD,
        faktiskTTEOS = 0,
        faktiskTTNordiskKonv = 0,
        faktiskTTNorge = 0,
        fastsattNorskTrygdetid = 0,
        framtidigTTEOS = 0,
        framtidigTTNorsk = 0,
        harBoddArbeidUtland = false,
        nevnerTTEOS = 0,
        nevnerTTNordiskKonv = 0,
        samletTTNordiskKonv = 0,
        tellerTTEOS = 0,
        tellerTTNordiskKonv = 0,
        utenforEOSogNorden = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoTrygdetidTrygdetidsdetaljerUtenforEOSogNorden() =
    OpplysningerBruktIBeregningUTDto.Trygdetid.Trygdetidsdetaljer.UtenforEOSogNorden(
        faktiskTTBilateral = 0,
        nevnerProRata = 0,
        tellerProRata = 0,
    )


fun createOpplysningerBruktIBeregningUTDtoUfoeretrygd() =
    OpplysningerBruktIBeregningUTDto.Ufoeretrygd(
        beloepsgrense = Kroner(220000),
        beregningsgrunnlagBeloepAar = Kroner(0),
        erKonvertert = false,
        fullUfoeretrygdPerAar = Kroner(0),
        harInntektEtterUfoereBegrunnelse = false,
        harUtbetalingsgradLessThanUfoeregrad = true,
        kompensasjonsgrad = 0.0,
        ufoeregrad = 0,
        ufoeretidspunkt = LocalDate.of(2020, 1, 1),
        ufoeretrygdOrdinaer = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoUfoeretrygdOrdinaer() =
    OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer(
        harBeloepRedusert = true,
        harGammelUTBeloepUlikNyUTBeloep = false,
        harNyUTBeloep = true,
        harTotalNettoUT = true,
        nettoAkkumulerteBeloepUtbetalt = Kroner(200000),
        nettoAkkumulertePlussNettoRestAar = Kroner(300000),
        nettoPerAarReduksjonUT = Kroner(0),
        nettoTilUtbetalingRestenAvAaret = Kroner(100000),
        overskytendeInntekt = Kroner(0),
        reduksjonIUfoeretrygd = Kroner(80000),
        ufoeretrygdPlussInntekt = Kroner(500000),
    )


fun createOpplysningerBruktIBeregningUTDtoYrkesskade() =
    OpplysningerBruktIBeregningUTDto.Yrkesskade(
        beregningsgrunnlagBeloepAar = Kroner(0),
        inntektVedSkadetidspunkt = Kroner(0),
        skadetidspunkt = LocalDate.of(2020, 1, 1),
        yrkesskadegrad = 50,
    )

fun createOpplysningerBruktIBeregningUTDtoOpptjeningUfoeretrygd() =
    OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygd(
        harFoerstegangstjenesteOpptjening = false,
        harOmsorgsopptjening = true,
        opptjeningsperiode = listOf(Fixtures.create(), Fixtures.create()),
    )

fun createOpplysningerBruktIBeregningUTDtoOpptjeningsperiode() =
    OpplysningerBruktIBeregningUTDto.Opptjeningsperiode(
        aar = Year(2022),
        harFoerstegangstjenesteOpptjening = false,
        harInntektAvtaleland = true,
        harOmsorgsopptjening = true,
        justertPensjonsgivendeInntekt = Kroner(0),
        pensjonsgivendeInntekt = Kroner(0),
    )


fun createOpplysningerBruktIBeregningUTDtoOpplysningerBruktIBeregningUTAvdoed() =
    OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed(
        erFlyktning = false,
        erUngUfoer = false,
        foedselsnummer = Foedselsnummer("01125512345"),
        harNyttGjenlevendetillegg = true,
        trygdetidsdetaljer = OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed.TrygdetidsdetaljerAvdoed(
            anvendtTT = 100,
            beregningsmetode = Beregningsmetode.FOLKETRYGD,
            faktiskTTBilateral = 10,
            faktiskTTEOS = 20,
            faktiskTTNordiskKonv = 40,
            faktiskTTNorge = 30,
            faktiskTTNorgePlusFaktiskBilateral = 40,
            faktiskTTNorgePlusfaktiskTTEOS = 40,
            fastsattNorskTrygdetid = 40,
            framtidigTTAvtaleland = 20,
            framtidigTTEOS = 10,
            framtidigTTNorsk = 0,
            harBoddArbeidUtland = false,
            nevnerTTBilateralProRata = 0,
            nevnerTTEOS = 0,
            nevnerTTNordiskKonv = 0,
            samletTTNordiskKonv = 0,
            tellerTTBilateralProRata = 0,
            tellerTTEOS = 0,
            tellerTTNordiskKonv = 0,
        ),
        ufoeretrygd = OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed.Ufoeretrygd1(
            beregningsgrunnlagBeloepAar = Kroner(150000),
            ufoeregrad = 80,
            ufoeretidspunkt = LocalDate.of(2020, 1, 1),
        ),
        opptjeningUfoeretrygd = OpplysningerBruktIBeregningUTDto.OpptjeningUfoeretrygd(
            harFoerstegangstjenesteOpptjening = true,
            harOmsorgsopptjening = false,
            opptjeningsperiode = listOf(Fixtures.create(), Fixtures.create()),
        ),
        yrkesskade = OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed.Yrkesskade1(
            yrkesskadegrad = 20,
            inntektVedSkadetidspunkt = Kroner(50000),
            beregningsgrunnlagBeloepAarYrkesskade = Kroner(275000),
        ),
        norskTrygdetidPerioder = listOf(
            OpplysningerBruktIBeregningUTDto.NorskTrygdetidPeriode(
                trygdetidFom = LocalDate.of(2020, 1, 1),
                trygdetidTom = LocalDate.of(2020, 1, 1),
            )
        ),
        bilateralTrygdePerioder = listOf(
            OpplysningerBruktIBeregningUTDto.UtenlandsTrygdetidPeriode(
                trygdetidBilateralLand = "Storbritannia",
                trygdetidEOSLand = "",
                trygdetidFom = LocalDate.of(2020, 1, 1),
                trygdetidTom = LocalDate.of(2021, 1, 1 ),
            )
        ),
        eosTrygdePerioder = listOf(
            OpplysningerBruktIBeregningUTDto.UtenlandsTrygdetidPeriode(
                trygdetidBilateralLand = "",
                trygdetidEOSLand = "Nederland",
                trygdetidFom = LocalDate.of(2020, 1, 1),
                trygdetidTom = LocalDate.of(2021, 1, 1 ),
            )
        ),
        opptjeningsperioder = listOf(
            OpplysningerBruktIBeregningUTDto.Opptjeningsperiode(
                aar = Year(2020),
                harFoerstegangstjenesteOpptjening = false,
                harInntektAvtaleland = false,
                harOmsorgsopptjening = false,
                justertPensjonsgivendeInntekt = Kroner(0),
                pensjonsgivendeInntekt = Kroner(0),
            )
        )
    )


