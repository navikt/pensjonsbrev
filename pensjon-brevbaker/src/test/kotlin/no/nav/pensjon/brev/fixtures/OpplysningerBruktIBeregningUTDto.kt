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
        beregningUfoere = Fixtures.create(),
        borIUtlandet = false,
        fraOgMedDatoErNesteAar = false,
        gjenlevendetilleggInformasjon = Fixtures.create(),
        grunnbeloep = Kroner(90000),
        harBarnetilleggInnvilget = false,
        harBrukerKonvertertUP = false,
        harEktefelletilleggInnvilget = false,
        harGodkjentBrevkode = true,
        harKravaarsakEndringInntekt = true,
        inntektEtterUfoereGjeldende_beloepIEU = Kroner(0),
        inntektFoerUfoereBegrunnelse = InntektFoerUfoereBegrunnelse.STDBEGR_12_8_2_3,
        inntektFoerUfoereGjeldende = Fixtures.create(),
        inntektsAvkortingGjeldende = Fixtures.create(),
        kravAarsakType = KravAarsakType.ENDRET_OPPTJENING,
        minsteytelseGjeldende_sats = 0.0,
        norskTrygdetid = listOf(createOpplysningerBruktIBeregningUTDtoNorskTrygdetid()),
        opptjeningAvdoedUfoeretrygd = Fixtures.create(),
        opptjeningUfoeretrygd = Fixtures.create(),
        personGrunnlag = Fixtures.create(),
        sivilstand = Sivilstand.PARTNER,
        trygdetidGjeldende = Fixtures.create(),
        trygdetidsdetaljerGjeldende = Fixtures.create(),
        trygdetidsdetaljerGjeldeneAvdoed = Fixtures.create(),
        ufoeretrygdGjeldende = Fixtures.create(),
        ufoeretrygdOrdinaer = Fixtures.create(),
        ungUfoerGjeldende_erUnder20Aar = false,
        utenlandskTrygdetidBilateral = (1..3).map { createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidBilateral() },
        utenlandskTrygdetidEOS = (1..3).map { createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidEOS() },
        yrkesskadeGjeldende = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende() =
    OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende(
        fellesbarn = Fixtures.create(),
        foedselsdatoPaaBarnTilleggetGjelder = listOf(LocalDate.of(1990, 3, 24)),
        saerkullsbarn = Fixtures.create(),
        totaltAntallBarn = 4,
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
        grunnbeloep = Kroner(100000),
        virkDatoFom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoInntektFoerUfoereGjeldende() =
    OpplysningerBruktIBeregningUTDto.InntektFoerUfoereGjeldende(
        erSannsynligEndret = false,
        ifuInntekt = Kroner(0),
        oifuInntekt = Kroner(0),
    )

fun createOpplysningerBruktIBeregningUTDtoInntektsAvkortingGjeldende() =
    OpplysningerBruktIBeregningUTDto.InntektsAvkortingGjeldende(
        forventetInntektAar = Kroner(0),
        harForventetInntektLargerThanInntektstak = false,
        harInntektsgrenseLargerThanOrEqualToInntektstak = false,
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
        harDelvisUfoeregrad = false,
        harFullUfoeregrad = false,
        harUtbetalingsgradLessThanUfoeregrad = true,
        kompensasjonsgrad = 0.0,
        ufoeregrad = 0,
        ufoeretidspunkt = LocalDate.of(2020, 1, 1),
        ugradertBruttoPerAar = Kroner(0),
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
        opptjeningsperioder = listOf(Fixtures.create(), Fixtures.create())
    )

fun createOpplysningerBruktIBeregningUTDtoOpptjeningsperiode() =
    OpplysningerBruktIBeregningUTDto.Opptjeningsperiode(
        aar = Year(2022),
        erBrukt = false,
        harBeregningsmetodeFolketrygd = false,
        harFoerstegangstjenesteOpptjening = false,
        harInntektAvtaleland = true,
        harOmsorgsopptjening = true,
        inntektAvkortet = Kroner(0),
        justertPensjonsgivendeInntekt = Kroner(0),
        pensjonsgivendeInntekt = Kroner(0),
    )

fun createOpplysningerBruktIBeregningUTDtoTrygdetidGjeldende() =
    OpplysningerBruktIBeregningUTDto.TrygdetidGjeldende(
        fastsattTrygdetid = 0,
        har40AarFastsattTrygdetid = false,
        harFramtidigTrygdetidEOS = false,
        harFramtidigTrygdetidNorsk = false,
        harLikUfoeregradOgYrkesskadegrad = false,
        harTrygdetidsgrunnlag = false,
        harYrkesskadeOppfylt = false,
    )

fun createOpplysningerBruktIBeregningUTDtoNorskTrygdetid() =
    OpplysningerBruktIBeregningUTDto.NorskTrygdetid(
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidEOS() =
    OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidEOS(
        trygdetidEOSLand = "Nederland",
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetidBilateral() =
    OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidBilateral(
        trygdetidBilateralLand = "Storbritannia",
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoGjenlevendetilleggInformasjon() =
    OpplysningerBruktIBeregningUTDto.GjenlevendetilleggInformasjon(
        beregningsgrunnlagBeloepAar = Kroner(0),
        beregningsgrunnlagBeloepAarYrkesskade = Kroner(0),
        erUngUfoer = false,
        harGjenlevendetillegg = true,
        harNyttGjenlevendetillegg = false,
        inntektVedSkadetidspunkt = Kroner(0),
        ufoeretidspunkt = LocalDate.of(2020,1,1),
        yrkesskadegrad = 0,
    )

fun createOpplysningerBruktIBeregningUTDtoUfoeretrygdOrdinaer() =
    OpplysningerBruktIBeregningUTDto.UfoeretrygdOrdinaer(
        fradrag = Kroner(80000),
        harBeloepRedusert = true,
        harNyUTBeloep = true,
        harTotalNettoUT = true,
        nettoAkkumulerteBeloepUtbetalt = Kroner(200000),
        nettoTilUtbetalingRestenAvAaret = Kroner(100000),

        )

fun createOpplysningerBruktIBeregningUTDtoBeregningUfoere() =
    OpplysningerBruktIBeregningUTDto.BeregningUfoere(
        harGammelUTBeloepUlikNyUTBeloep = false,
        harInntektsgrenseLessThanInntektstak = true,
        nettoAkkumulertePlussNettoRestAar = Kroner(300000),
        nettoPerAarReduksjonUT = Kroner(0),
        overskytendeInntekt = Kroner(0),
        ufoeretrygdPlussInntekt = Kroner(500000),
    )

fun createOpplysningerBruktIBeregningUTDtoTrygdetidsdetaljerGjeldeneAvdoed() =
    OpplysningerBruktIBeregningUTDto.TrygdetidsdetaljerGjeldeneAvdoed(
        anvendtTT = 0,
        faktiskTTBilateral = 0,
        faktiskTTEOS = 0,
        faktiskTTNordiskKonv = 0,
        faktiskTTNorge = 0,
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
    )

fun createOpplysningerBruktIBeregningUTDtoPersonGrunnlag() =
    OpplysningerBruktIBeregningUTDto.PersonGrunnlag(
        avdoedesnavn = "Per Pensjon",
        avdoedeErFlyktning = false,
    )


