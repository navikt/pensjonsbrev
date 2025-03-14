package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTO
import java.time.LocalDate

fun createOmstillingsstoenadRevurderingDTO() =
    OmstillingsstoenadRevurderingDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        innholdForhaandsvarsel = createPlaceholderForRedigerbartInnhold(),
        beregning =
            OmstillingsstoenadBeregning(
                innhold = createPlaceholderForRedigerbartInnhold(),
                virkningsdato = LocalDate.of(2024, 1, 1),
                beregningsperioder =
                    listOf(
                        OmstillingsstoenadBeregningsperiode(
                            datoFOM = LocalDate.of(2024, 2, 1),
                            datoTOM = null,
                            inntekt = Kroner(100000),
                            oppgittInntekt = Kroner(600000),
                            fratrekkInnAar = Kroner(100000),
                            innvilgaMaaneder = 12,
                            grunnbeloep = Kroner(118000),
                            utbetaltBeloep = Kroner(5000),
                            ytelseFoerAvkorting = Kroner(22000),
                            restanse = Kroner(1000),
                            trygdetid = 40,
                            sanksjon = false,
                            institusjon = false,
                        ),
                        OmstillingsstoenadBeregningsperiode(
                            datoFOM = LocalDate.of(2024, 1, 1),
                            datoTOM = LocalDate.of(2024, 1, 31),
                            inntekt = Kroner(100000),
                            oppgittInntekt = Kroner(600000),
                            fratrekkInnAar = Kroner(100000),
                            innvilgaMaaneder = 12,
                            grunnbeloep = Kroner(118000),
                            utbetaltBeloep = Kroner(10000),
                            ytelseFoerAvkorting = Kroner(22000),
                            restanse = Kroner(1000),
                            trygdetid = 40,
                            sanksjon = false,
                            institusjon = false,
                        ),
                    ),
                sisteBeregningsperiode =
                    OmstillingsstoenadBeregningsperiode(
                        datoFOM = LocalDate.of(2024, 2, 1),
                        datoTOM = null,
                        inntekt = Kroner(100000),
                        oppgittInntekt = Kroner(600000),
                        fratrekkInnAar = Kroner(100000),
                        innvilgaMaaneder = 12,
                        grunnbeloep = Kroner(118000),
                        utbetaltBeloep = Kroner(5000),
                        ytelseFoerAvkorting = Kroner(22000),
                        restanse = Kroner(1000),
                        trygdetid = 40,
                        sanksjon = false,
                        institusjon = false,
                    ),
                sisteBeregningsperiodeNesteAar = null,
                trygdetid =
                    Trygdetid(
                        trygdetidsperioder =
                            listOf(
                                Trygdetidsperiode(
                                    datoFOM = LocalDate.of(2000, 1, 1),
                                    datoTOM = LocalDate.of(2023, 12, 31),
                                    land = "Norge",
                                    landkode = "NOR",
                                    opptjeningsperiode = Periode(23, 0, 0),
                                    type = TrygdetidType.FAKTISK,
                                ),
                                Trygdetidsperiode(
                                    datoFOM = LocalDate.of(2024, 1, 1),
                                    datoTOM = LocalDate.of(2050, 12, 31),
                                    land = "Norge",
                                    landkode = "NOR",
                                    opptjeningsperiode = Periode(26, 0, 0),
                                    type = TrygdetidType.FREMTIDIG,
                                ),
                            ),
                        beregnetTrygdetidAar = 40,
                        prorataBroek = null,
                        beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
                        beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
                        mindreEnnFireFemtedelerAvOpptjeningstiden = true,
                        navnAvdoed = "Elvis Presley",

                    ),
                oppphoersdato = LocalDate.of(2024, 12, 1),
                opphoerNesteAar = false,
                erYrkesskade = false,
            ),
        erEndret = true,
        erOmgjoering = false,
        datoVedtakOmgjoering = null,
        lavEllerIngenInntekt = false,
        feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL,
        tidligereFamiliepleier = false,
        erInnvilgelsesaar = true
    )

fun createOmstillingsstoenadRevurderingRedigerbartUtfallDTO() =
    OmstillingsstoenadRevurderingRedigerbartUtfallDTO(
        beregning =
        OmstillingsstoenadBeregningRevurderingRedigertbartUtfall(
            virkningsdato = LocalDate.of(2024, 1, 1),
            beregningsperioder =
            listOf(
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2024, 2, 1),
                    datoTOM = null,
                    inntekt = Kroner(100000),
                    oppgittInntekt = Kroner(600000),
                    fratrekkInnAar = Kroner(100000),
                    innvilgaMaaneder = 12,
                    grunnbeloep = Kroner(118000),
                    utbetaltBeloep = Kroner(5000),
                    ytelseFoerAvkorting = Kroner(22000),
                    restanse = Kroner(1000),
                    trygdetid = 40,
                    sanksjon = false,
                    institusjon = false,
                ),
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2024, 1, 1),
                    datoTOM = LocalDate.of(2024, 1, 31),
                    inntekt = Kroner(100000),
                    oppgittInntekt = Kroner(600000),
                    fratrekkInnAar = Kroner(100000),
                    innvilgaMaaneder = 12,
                    grunnbeloep = Kroner(118000),
                    utbetaltBeloep = Kroner(10000),
                    ytelseFoerAvkorting = Kroner(22000),
                    restanse = Kroner(1000),
                    trygdetid = 40,
                    sanksjon = false,
                    institusjon = false,
                ),
            ),
            sisteBeregningsperiode =
            OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.of(2024, 2, 1),
                datoTOM = null,
                inntekt = Kroner(100000),
                oppgittInntekt = Kroner(600000),
                fratrekkInnAar = Kroner(100000),
                innvilgaMaaneder = 12,
                grunnbeloep = Kroner(118000),
                utbetaltBeloep = Kroner(5000),
                ytelseFoerAvkorting = Kroner(22000),
                restanse = Kroner(1000),
                trygdetid = 40,
                sanksjon = false,
                institusjon = false,
            ),
            sisteBeregningsperiodeNesteAar = null,
            oppphoersdato = LocalDate.of(2024, 12, 1),
            opphoerNesteAar = false,
        ),
        erEndret = true,
        erEtterbetaling = true,
        etterbetaling =
        OmstillingsstoenadEtterbetaling(
            fraDato = LocalDate.of(2024, 1, 1),
            tilDato = LocalDate.of(2024, 1, 31),
        ),
        feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL,
        harFlereUtbetalingsperioder = true,
        harUtbetaling = true,
        inntekt = Kroner(0),
        inntektsAar = 2024,
        mottattInntektendringAutomatisk = null
    )
