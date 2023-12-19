package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OMSRevurderingEndringDTO
import java.time.LocalDate

fun createOMSRevurderingEndringDTO() =
    OMSRevurderingEndringDTO(
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
        etterbetalinginfo = EtterbetalingDTO(
            fraDato = LocalDate.now(),
            tilDato = LocalDate.now()
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
                    opptjeningsperiode = null
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
        ),
        erEndret = true
    )