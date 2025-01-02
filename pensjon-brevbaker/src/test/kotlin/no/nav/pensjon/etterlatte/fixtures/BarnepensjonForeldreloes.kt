package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.ForskjelligAvdoedPeriode
import no.nav.pensjon.etterlatte.maler.ForskjelligTrygdetid
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesRedigerbarDTO
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

fun createBarnepensjonForeldreloesDTO(): BarnepensjonForeldreloesDTO {
    val tt2 = Trygdetid(
        navnAvdoed = "Død Sist",
        trygdetidsperioder = listOf(
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2004, 1, 1),
                datoTOM = LocalDate.of(2024, 1, 1),
                land = "NOR",
                opptjeningsperiode = Periode(20, 0, 0),
                type = TrygdetidType.FAKTISK
            ),
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2024, 1, 1),
                datoTOM = LocalDate.of(2044, 1, 1),
                land = "NOR",
                opptjeningsperiode = Periode(20, 0, 0),
                type = TrygdetidType.FREMTIDIG
            )
        ),
        beregnetTrygdetidAar = 40,
        prorataBroek = null,
        beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
        beregningsMetodeFraGrunnlag = BeregningsMetode.BEST,
        mindreEnnFireFemtedelerAvOpptjeningstiden = false
    )

    val tt1 = Trygdetid(
        navnAvdoed = "Først Død",
        trygdetidsperioder = listOf(
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2004, 1, 1),
                datoTOM = LocalDate.of(2014, 1, 1),
                land = "NOR",
                opptjeningsperiode = Periode(10, 0, 0),
                type = TrygdetidType.FAKTISK
            ),
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2014, 1, 1),
                datoTOM = LocalDate.of(2023, 10, 1),
                land = "SWE",
                opptjeningsperiode = Periode(10, 0, 0),
                type = TrygdetidType.FAKTISK
            ),
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2023, 11, 1),
                datoTOM = LocalDate.of(2044, 1, 1),
                land = "NOR",
                opptjeningsperiode = Periode(20, 0, 0),
                type = TrygdetidType.FREMTIDIG
            )
        ),
        beregnetTrygdetidAar = 40,
        prorataBroek = IntBroek(3, 4),
        beregningsMetodeAnvendt = BeregningsMetode.PRORATA,
        beregningsMetodeFraGrunnlag = BeregningsMetode.PRORATA,
        mindreEnnFireFemtedelerAvOpptjeningstiden = false

    )
    return BarnepensjonForeldreloesDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        beregning = BarnepensjonBeregning(
            innhold = createPlaceholderForRedigerbartInnhold(),
            virkningsdato = LocalDate.now(),
            antallBarn = 2,
            grunnbeloep = Kroner(123456),
            beregningsperioder = listOf(
                BarnepensjonBeregningsperiode(
                    datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
                    datoTOM = LocalDate.of(2023, Month.JULY, 31),
                    grunnbeloep = Kroner(123456),
                    antallBarn = 2,
                    utbetaltBeloep = Kroner(6234)
                )
            ),
            sisteBeregningsperiode = BarnepensjonBeregningsperiode(
                datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
                datoTOM = LocalDate.of(2023, Month.JULY, 31),
                grunnbeloep = Kroner(123456),
                antallBarn = 2,
                utbetaltBeloep = Kroner(6234)
            ),
            trygdetid = listOf(tt1, tt2),
            bruktTrygdetid = tt2,
            forskjelligTrygdetid = ForskjelligTrygdetid(
                foersteTrygdetid = tt1,
                foersteVirkningsdato = LocalDate.of(2023, 11, 1),
                senereVirkningsdato = LocalDate.of(2024, 1, 1),
                harForskjelligMetode = true,
                erForskjellig = true,
            ),
            erForeldreloes = true,
        ),
        frivilligSkattetrekk = true,
        bosattUtland = true,
        brukerUnder18Aar = true,
        kunNyttRegelverk = true,
        harUtbetaling = true,
        erGjenoppretting = false,
        vedtattIPesys = false,
        erMigrertYrkesskade = false,
        erEtterbetaling = false
    )
}

fun createBarnepensjonForeldreloesRedigerbarDTO(): BarnepensjonForeldreloesRedigerbarDTO {
    val siste = BarnepensjonBeregningsperiode(
        datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
        datoTOM = LocalDate.of(2023, Month.JULY, 31),
        grunnbeloep = Kroner(123456),
        antallBarn = 2,
        utbetaltBeloep = Kroner(6234)
    )
    val foersteDoed = YearMonth.now().minusMonths(6)
    val andreDoed = YearMonth.now().minusMonths(3).atDay(1)

    return BarnepensjonForeldreloesRedigerbarDTO(
        virkningsdato = foersteDoed.plusMonths(1).atDay(1),
        sisteBeregningsperiodeBeloep = siste.utbetaltBeloep,
        sisteBeregningsperiodeDatoFom = siste.datoFOM,
        erGjenoppretting = false,
        harUtbetaling = true,
        erEtterbetaling = true,
        flerePerioder = true,
        vedtattIPesys = false,
        forskjelligAvdoedPeriode = ForskjelligAvdoedPeriode(
            foersteAvdoed = Avdoed(navn = "Død Først", doedsdato = foersteDoed.atDay(7)),
            senereAvdoed = Avdoed(navn = "Senere Død", doedsdato = andreDoed),
            senereVirkningsdato = andreDoed.plusMonths(1)

        ),
        erSluttbehandling = false
    )
}