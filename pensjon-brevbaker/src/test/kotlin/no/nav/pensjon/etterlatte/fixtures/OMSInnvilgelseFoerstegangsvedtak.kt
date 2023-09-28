package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.OMSInnvilgelseFoerstegangsvedtakDTO
import java.time.LocalDate

fun createOMSInnvilgelseFoerstegangsvedtakDTO() =
    OMSInnvilgelseFoerstegangsvedtakDTO(
        utbetalingsinfo = Utbetalingsinfo(
            antallBarn = 2,
            beloep = Kroner(1234),
            soeskenjustering = true,
            virkningsdato = LocalDate.now(),
            beregningsperioder = listOf(
                Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    grunnbeloep = Kroner(106003),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                ),
                Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    grunnbeloep = Kroner(106003),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                )
            )
        ),
        avkortingsinfo = Avkortingsinfo(
            grunnbeloep = Kroner(118000),
            inntekt = Kroner(550000),
            virkningsdato = LocalDate.now(),
            beregningsperioder = listOf(
                AvkortetBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    inntekt = Kroner(550000),
                    utbetaltBeloep = Kroner(0),
                    ytelseFoerAvkorting = Kroner(100),
                    trygdetid = 40
                ),
                AvkortetBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    inntekt = Kroner(550000),
                    utbetaltBeloep = Kroner(0),
                    ytelseFoerAvkorting = Kroner(100),
                    trygdetid = 40
                )
            )
        ),
        avdoed = Avdoed(
            navn = "Avdoed Avdoedesen",
            doedsdato = LocalDate.now()
        ),
        etterbetalinginfo = EtterbetalingDTO(
            fraDato = LocalDate.now(),
            tilDato = LocalDate.now(),
            beregningsperioder = listOf(
                Etterbetalingsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    grunnbeloep = Kroner(118000),
                    stoenadFoerReduksjon = Kroner(9000),
                    utbetaltBeloep = Kroner(3080)
                ),
                Etterbetalingsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    grunnbeloep = Kroner(118000),
                    stoenadFoerReduksjon = Kroner(11000),
                    utbetaltBeloep = Kroner(2000)
                )
            )
        ),
        beregningsinfo = Beregningsinfo(
            grunnbeloep = Kroner(118000),
            innhold = listOf(
                Element(
                    type = ElementType.HEADING_TWO,
                    children = listOf(
                        InnerElement(
                            text = "<INSERT UTFALL HERE>"
                        )
                    )
                ),
                Element(
                    type = ElementType.PARAGRAPH,
                    children = listOf(
                        InnerElement(
                            text = "Her kommer det valgfri tekst om utfallene til beregning av stønaden"
                        )
                    )
                )
            ),
            beregningsperioder = listOf(
                NyBeregningsperiode(
                    inntekt = Kroner(650000),
                    trygdetid = 123,
                    stoenadFoerReduksjon = Kroner(9000),
                    utbetaltBeloep = Kroner(1234)
                )
            ),
            trygdetidsperioder = listOf(
                Trygdetidsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    land = "Norge",
                    opptjeningsperiode = "3 år?"
                )
            )
        ),
        innhold = listOf(
            Element(
                type = ElementType.HEADING_TWO,
                children = listOf(
                    InnerElement(
                        text = "Tittel 2"
                    )
                )
            ),
            Element(
                type = ElementType.PARAGRAPH,
                children = listOf(
                    InnerElement(
                        text = "Her kommer det valgfri tekst om innvilget vedtak"
                    )
                )
            )
        )
    )