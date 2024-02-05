package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTO
import java.time.LocalDate

fun createOmstillingsstoenadRevurderingDTO() =
    OmstillingsstoenadRevurderingDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        beregning = OmstillingsstoenadBeregning(
            innhold = createPlaceholderForRedigerbartInnhold(),
            grunnbeloep = Kroner(118000),
            inntekt = Kroner(100000),
            virkningsdato = LocalDate.now(),
            beregningsperioder = listOf(
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2024, 2, 1),
                    datoTOM = null,
                    inntekt = Kroner(100000),
                    utbetaltBeloep = Kroner(5000),
                    ytelseFoerAvkorting = Kroner(22000),
                    trygdetid = 40
                ),
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2024, 1, 1),
                    datoTOM = LocalDate.of(2024, 1, 31),
                    inntekt = Kroner(100000),
                    utbetaltBeloep = Kroner(10000),
                    ytelseFoerAvkorting = Kroner(22000),
                    trygdetid = 40
                )
            ),
            sisteBeregningsperiode = OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.of(2024, 2, 1),
                datoTOM = null,
                inntekt = Kroner(100000),
                utbetaltBeloep = Kroner(5000),
                ytelseFoerAvkorting = Kroner(22000),
                trygdetid = 40
            ),
            trygdetid = Trygdetid(
                trygdetidsperioder = listOf(
                    Trygdetidsperiode(
                        datoFOM = LocalDate.of(2000, 1, 1),
                        datoTOM = LocalDate.of(2023, 12, 31),
                        land = "NOR",
                        opptjeningsperiode = Periode(23, 0, 0),
                        type = TrygdetidType.FAKTISK
                    ),
                    Trygdetidsperiode(
                        datoFOM = LocalDate.of(2024, 1, 1),
                        datoTOM = LocalDate.of(2050, 12, 31),
                        land = "NOR",
                        opptjeningsperiode = Periode(26, 0, 0),
                        type = TrygdetidType.FREMTIDIG
                    )
                ),
                beregnetTrygdetidAar = 40,
                beregnetTrygdetidMaaneder = 480,
                prorataBroek = null,
                beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
                beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
                mindreEnnFireFemtedelerAvOpptjeningstiden = true,
            )
        ),
        etterbetaling = OmstillingsstoenadEtterbetaling(
            fraDato = LocalDate.of(2024, 1, 1),
            tilDato = LocalDate.of(2024, 1, 31)
        ),
        erEndret = true,
        erOmgjoering = false,
        datoVedtakOmgjoering = null,
        harFlereUtbetalingsperioder = true,
        lavEllerIngenInntekt = false,
        harUtbetaling = true,
        feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL
    )