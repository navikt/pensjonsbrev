package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import java.time.LocalDate

fun createOpplysningerBruktIBeregningUTDto() =
    OpplysningerBruktIBeregningUTDto(
        //listOf(createOpplysningerBruktIBeregningUTDtoNorskTrygdetid(), createOpplysningerBruktIBeregningUTDtoNorskTrygdetid(), createOpplysningerBruktIBeregningUTDtoNorskTrygdetid())
        barnetilleggGjeldende = Fixtures.create(),
        beregnetUTPerManedGjeldende = Fixtures.create(),
        borIUtlandet = false,
        fraOgMedDatoErNesteAar = false,
        harBarnetilleggInnvilget = false,
        harEktefelletilleggInnvilget = false,
        harKravaarsakEndringInntekt = true,
        inntektEtterUfoereGjeldende_beloepIEU = Kroner(0),
        inntektFoerUfoereBegrunnelse = InntektFoerUfoereBegrunnelse.STDBEGR_12_8_2_3,
        inntektFoerUfoereGjeldende = Fixtures.create(),
        inntektsAvkortingGjeldende = Fixtures.create(),
        kravAarsakType = KravAarsakType.ENDRET_OPPTJENING,
        minsteytelseGjeldende_sats = 0.0,
        norskTrygdetidPeriode = (1..3).map { createOpplysningerBruktIBeregningUTDtoNorskTrygdetidPeriode() },
        opplysningerAvdoed = Fixtures.create(),
        opptjeningUfoeretrygd = Fixtures.create(),
        sivilstand = Sivilstand.PARTNER,
        trygdetidsdetaljerGjeldende = Fixtures.create(),
        ufoeretrygdGjeldende = Fixtures.create(),
        ufoeretrygdOrdinaer = Fixtures.create(),
        ungUfoerGjeldende_erUnder20Aar = false,
        utenlandskTrygdetidPeriode = (1..3).map { createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidPeriode() },
        yrkesskadeGjeldende = Fixtures.create(),
    )

//         norskTrygdetidPeriode = listOf(createOpplysningerBruktIBeregningUTDtoNorskTrygdetidPeriode()),
//         utenlandskTrygdetidPeriode = listOf(createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidPeriode()),
fun createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende() =
    OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende(
        fellesbarn = Fixtures.create(),
        saerkullsbarn = Fixtures.create(),
        foedselsdatoPaaBarnTilleggetGjelder = listOf(
            LocalDate.of(2000, 1, 1),
            LocalDate.of(2000, 2, 2),
            LocalDate.of(2000, 3, 3),
            LocalDate.of(2000, 4, 4),
        )
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeFellesbarn() =
    OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Fellesbarn(
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
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeSaerkullsbarn() =
    OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Saerkullsbarn(
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

fun createOpplysningerBruktIBeregningUTDtoBeregnetUTPerManedGjeldende() =
    OpplysningerBruktIBeregningUTDto.BeregnetUTPerManedGjeldende(
        brukerErFlyktning = false,
        brukersSivilstand = Sivilstand.GIFT,
        grunnbeloep = Kroner(111477),
        virkDatoFom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoInntektFoerUfoereGjeldende() =
    OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende(
        erSannsynligEndret = false,
        inntektFoerUfoer = Kroner(0),
        oppjustertInntektFoerUfoer = Kroner(0),
    )

fun createOpplysningerBruktIBeregningUTDtoInntektsAvkortingGjeldende() =
    OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende(
        forventetInntektAar = Kroner(0),
        inntektsgrenseAar = Kroner(0),
        inntektstak = Kroner(0),
    )

fun createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldende() =
    OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende(
        anvendtTT = 40,
        beregningsmetode = Beregningsmetode.FOLKETRYGD,
        faktiskTTEOS = 0,
        faktiskTTNordiskKonv = 0,
        faktiskTTNorge = 0,
        fastsattTrygdetid = 0,
        framtidigTTEOS = 0,
        framtidigTTNorsk = 0,
        nevnerTTEOS = 0,
        nevnerTTNordiskKonv = 0,
        samletTTNordiskKonv = 0,
        tellerTTEOS = 0,
        tellerTTNordiskKonv = 0,
        utenforEOSogNorden = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldendeUtenforEOSogNorden() =
    OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldende.UtenforEOSogNorden(
        faktiskTTBilateral = 0,
        nevnerProRata = 0,
        tellerProRata = 0,
    )

fun createOpplysningerBruktIBeregningUTDtoUfoeretrygdGjeldende() =
    OpplysningerBruktIBeregningUTDto.UfoeretrygdGjeldende(
        beloepsgrense = Kroner(220000),
        beregningsgrunnlagBeloepAar = Kroner(0),
        erKonvertert = false,
        harUtbetalingsgradLessThanUfoeregrad = true,
        kompensasjonsgrad = 0.0,
        ufoeregrad = 0,
        ufoeretidspunkt = LocalDate.of(2020, 1, 1),
        fullUfoeretrygdPerAar = Kroner(0),
    )

fun createOpplysningerBruktIBeregningUTDtoYrkesskadeGjeldende() =
    OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende(
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

fun createOpplysningerBruktIBeregningUTDtoNorskTrygdetidPeriode() =
    OpplysningerBruktIBeregningUTDto.NorskTrygdetidPeriode(
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidPeriode() =
    OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidPeriode(
        trygdetidBilateralLand = "Storbritannia",
        trygdetidEOSLand = "Nederland",
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoUfoeretrygdOrdinaer() =
    OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer(
        harBeloepRedusert = true,
        harNyUTBeloep = true,
        harTotalNettoUT = true,
        nettoAkkumulerteBeloepUtbetalt = Kroner(200000),
        nettoTilUtbetalingRestenAvAaret = Kroner(100000),
        reduksjonIUfoeretrygd = Kroner(80000),
        harGammelUTBeloepUlikNyUTBeloep = false,
        nettoAkkumulertePlussNettoRestAar = Kroner(300000),
        nettoPerAarReduksjonUT = Kroner(0),
        overskytendeInntekt = Kroner(0),
        ufoeretrygdPlussInntekt = Kroner(500000),

        )

fun createOpplysningerBruktIBeregningUTDtoOpplysningerAvdoed() =
    OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed(
        erFlyktning = false,
        erUngUfoer = false,
        foedselsnummer = Foedselsnummer("01125512345"),
        harNyttGjenlevendetillegg = true,
        trygdetidsdetaljer = OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed.Trygdetidsdetaljer1(
            anvendtTT = 100000,
            beregningsmetode = Beregningsmetode.FOLKETRYGD,
            faktiskTTBilateral = 0,
            faktiskTTEOS = 0,
            faktiskTTNordiskKonv = 0,
            faktiskTTNorge = 0,
            faktiskTTNorgePlusFaktiskBilateral = 0,
            faktiskTTNorgePlusfaktiskTTEOS = 0,
            framtidigTTAvtaleland = 0,
            framtidigTTEOS = 0,
            framtidigTTNorsk = 0,
            nevnerTTBilateralProRata = 0,
            nevnerTTEOS = 0,
            nevnerTTNordiskKonv = 0,
            samletTTNordiskKonv = 0,
            tellerTTBilateralProRata = 0,
            tellerTTEOS = 0,
            tellerTTNordiskKonv = 0,
        ),
        ufoeretrygdGjeldende = OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed.UfoeretrygdGjeldende1(
            beregningsgrunnlagBeloepAar = Kroner(150000),
            ufoeregrad = 80,
            ufoeretidspunkt = LocalDate.of(2020, 1, 1),
        ),
        norskTrygdetidPeriode = OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed.NorskTrygdetidPeriode1(
            trygdetidFom = LocalDate.of(2020, 1, 1),
            trygdetidTom = LocalDate.of(2020, 1, 1),
        ),
        opptjeningUfoeretrygd = OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed.OpptjeningUfoeretrygd1(
            harFoerstegangstjenesteOpptjening = true,
            harOmsorgsopptjening = false,
            opptjeningsperiode = listOf(Fixtures.create(), Fixtures.create()),
        ),
        opptjeningsperiode = OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed.Opptjeningsperiode1(
            aar = Year(2021),
            harFoerstegangstjenesteOpptjening = true,
            harInntektAvtaleland = true,
            harOmsorgsopptjening = false,
            justertPensjonsgivendeInntekt = Kroner(400000),
            pensjonsgivendeInntekt = Kroner(300000),
        ),
        utenlandskTrygdePeriode = OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed.UtenlandskTrygdetidPeriode1(
            trygdetidBilateralLand = "Storbritannia",
            trygdetidEOSLand = "Nederland",
            trygdetidFom = LocalDate.of(2020, 1, 1),
            trygdetidTom = LocalDate.of(2020, 1, 1),
        ),
        yrkesskadeGjeldene = OpplysningerBruktIBeregningUTDto.OpplysningerAvdoed.YrkesskadeGjeldene1(
            yrkesskadegrad = 20,
            inntektVedSkadetidspunkt = Kroner(50000),
            beregningsgrunnlagBeloepAarYrkesskade = Kroner(275000),
        ),
    )








