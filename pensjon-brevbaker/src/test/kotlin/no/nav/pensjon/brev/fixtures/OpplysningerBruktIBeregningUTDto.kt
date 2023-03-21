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
        gjenlevendetilleggGjeldene = Fixtures.create(),
        grunnbeloep = Kroner(90000),
        harBarnetilleggInnvilget = false,
        harBrukerKonvertertUP = false,
        harEktefelletilleggInnvilget = false,
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
        sivilstand = Sivilstand.PARTNER,
        trygdetidGjeldende = Fixtures.create(),
        trygdetidsdetaljerGjeldende = Fixtures.create(),
        ufoeretrygdGjeldende = Fixtures.create(),
        ungUfoerGjeldende_erUnder20Aar = false,
        utenlandskTrygdetid = (1..3).map { createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetid() },
        yrkesskadeGjeldende = Fixtures.create(),
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende() =
    OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende(
        fellesbarn = Fixtures.create(),
        saerkullsbarn = Fixtures.create(),
        totaltAntallBarn = 4
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldendeFellesbarn() =
    OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende.Fellesbarn(
        avkortningsbeloepAar = Kroner(40000),
        beloepNetto = Kroner(10000),
        beloepBrutto = Kroner(20000),
        beloepAarNetto = Kroner(120000),
        beloepAarBrutto = Kroner(240000),
        beloepFratrukketAnnenForeldersInntekt = Kroner(35000),
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
        beloepNetto = Kroner(10000),
        beloepBrutto = Kroner(20000),
        beloepAarNetto = Kroner(120000),
        beloepAarBrutto = Kroner(240000),
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
        harInntektsgrenseLessThanInntektstak = false,
        inntektsgrenseAar = Kroner(0),
        inntektstak = Kroner(0),
        overskytendeInntekt = Kroner(0),
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
        kompensasjonsgrad = 0.0,
        ufoeregrad = 0,
        ufoeretidspunkt = LocalDate.of(2020, 1, 1),
        harFullUfoeregrad = false,
        harGammelUTBeloepUlikNyUTBeloep = false,
        harNyUTBeloep = false,
        ugradertBruttoPerAar = Kroner(0)
    )

fun createOpplysningerBruktIBeregningUTDtoYrkesskadeGjeldende() =
    OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende(
        beregningsgrunnlagBeloepAar = Kroner(0),
        inntektVedSkadetidspunkt = Kroner(0),
        skadetidspunkt = LocalDate.of(2020, 1, 1),
        yrkesskadegrad = 0,
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

fun createOpplysningerBruktIBeregningUTDtoUtenlandskTrygdetid() =
    OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetid(
        trygdetidLand = "Storbritannia",
        trygdetidFom = LocalDate.of(2020, 1, 1),
        trygdetidTom = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoGjenlevendetilleggGjeldene() =
    OpplysningerBruktIBeregningUTDto.GjenlevendetilleggGjeldene(
        harGjenlevendetillegg = true,
        harNyttGjenlevendetillegg = false,
    )


