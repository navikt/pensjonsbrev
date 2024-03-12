package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO
import java.time.LocalDate
import java.time.Month

fun createOmstillingsstoenadInnvilgelseDTO() =
    OmstillingsstoenadInnvilgelseDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        avdoed = Avdoed(
            navn = "Avdoed Avdoedesen",
            doedsdato = LocalDate.of(2023, 12, 1)
        ),
        beregning = OmstillingsstoenadBeregning(
            innhold = createPlaceholderForRedigerbartInnhold(),
            virkningsdato = LocalDate.of(2024, 1, 1),
            beregningsperioder = listOf(
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2024, 1, 1),
                    datoTOM = LocalDate.of(2024, 1, 31),
                    inntekt = Kroner(500000),
                    aarsinntekt = Kroner(600000),
                    fratrekkInnAar = Kroner(100000),
                    relevantMaanederInnAar = 12,
                    grunnbeloep = Kroner(118620),
                    utbetaltBeloep = Kroner(10000),
                    ytelseFoerAvkorting = Kroner(20000),
                    trygdetid = 40,
                ),
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2024, 2, 1),
                    datoTOM = null,
                    inntekt = Kroner(500000),
                    aarsinntekt = Kroner(600000),
                    fratrekkInnAar = Kroner(100000),
                    relevantMaanederInnAar = 12,
                    grunnbeloep = Kroner(118620),
                    utbetaltBeloep = Kroner(9000),
                    ytelseFoerAvkorting = Kroner(22000),
                    trygdetid = 40
                )
            ),
            sisteBeregningsperiode = OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.of(2024, 2, 1),
                datoTOM = null,
                inntekt = Kroner(500000),
                aarsinntekt = Kroner(600000),
                fratrekkInnAar = Kroner(100000),
                relevantMaanederInnAar = 10,
                grunnbeloep = Kroner(118620),
                utbetaltBeloep = Kroner(9000),
                ytelseFoerAvkorting = Kroner(22000),
                trygdetid = 40
            ),
            trygdetid = Trygdetid(
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
                beregnetTrygdetidMaaneder = 480,
                prorataBroek = IntBroek(20, 150),
                beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
                beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
                mindreEnnFireFemtedelerAvOpptjeningstiden = false,
            )
        ),
        etterbetaling = OmstillingsstoenadEtterbetaling(
            fraDato = LocalDate.of(2024, 1, 1),
            tilDato = LocalDate.of(2024, 2, 28),
            etterbetalingsperioder = listOf(
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2024, Month.JANUARY, 1),
                    datoTOM = LocalDate.of(2024, Month.FEBRUARY, 28),
                    ytelseFoerAvkorting = Kroner(20000),
                    inntekt = Kroner(500000),
                    aarsinntekt = Kroner(600000),
                    fratrekkInnAar = Kroner(100000),
                    relevantMaanederInnAar = 12,
                    grunnbeloep = Kroner(118620),
                    utbetaltBeloep = Kroner(9000),
                    trygdetid = 40
                )
            )
        ),
        innvilgetMindreEnnFireMndEtterDoedsfall = true,
        lavEllerIngenInntekt = true,
    )

fun createOmstillingsstoenadInnvilgelseRedigerbartUtfallDTO() =
    OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO(
        virkningsdato = LocalDate.now(),
        avdoed = Avdoed(
            navn = "Avdod Avdodesen",
            doedsdato = LocalDate.now()
        ),
        utbetalingsbeloep = Kroner(12345),
        etterbetaling = true
    )