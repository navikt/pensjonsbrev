package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.KravAarsak
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import java.time.LocalDate

fun createOpplysningerBruktIBeregningUTDto() =
    OpplysningerBruktIBeregningUTDto(
        barnetilleggGjeldende = Fixtures.create(),
        beregnetUTPerManedGjeldende = Fixtures.create(),
        grunnbeloep = Kroner(90000),
        inntektEtterUfoereGjeldende_beloepIEU = Kroner(0),
        inntektFoerUfoereGjeldende = Fixtures.create(),
        inntektsAvkortingGjeldende = Fixtures.create(),
        minsteytelseGjeldende_sats = 0.0,
        sivilstand = Sivilstand.PARTNER,
        trygdetidsdetaljerGjeldende = Fixtures.create(),
        ufoeretrygdGjeldende = Fixtures.create(),
        ungUfoerGjeldende_erUnder20Aar = false,
        yrkesskadeGjeldende = Fixtures.create(),
        kravAarsak = KravAarsak.ENDRET_INNTEKT,
        fraOgMedDatoErNesteAar = false
    )

fun createOpplysningerBruktIBeregningUTDtoBarnetilleggGjeldende() =
    OpplysningerBruktIBeregningUTDto.BarnetilleggGjeldende(
        saerkullsbarn = Fixtures.create(),
        fellesbarn = Fixtures.create(),
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
        kompensasjonsgrad = 0.0,
        ufoeregrad = 0,
        ufoeretidspunkt = LocalDate.of(2020, 1, 1),
    )

fun createOpplysningerBruktIBeregningUTDtoYrkesskadeGjeldende() =
    OpplysningerBruktIBeregningUTDto.YrkesskadeGjeldende(
        beregningsgrunnlagBeloepAar = Kroner(0),
        inntektVedSkadetidspunkt = Kroner(0),
        skadetidspunkt = LocalDate.of(2020, 1, 1),
        yrkesskadegrad = 0,
    )