package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTO
import java.time.LocalDate

fun createOmstillingsstoenadRevurderingDTO() =
    OmstillingsstoenadRevurderingDTO(
        omstillingsstoenadBeregning = OmstillingsstoenadBeregning(
            innhold = emptyList(),
            grunnbeloep = Kroner(118000),
            inntekt = Kroner(550000),
            virkningsdato = LocalDate.now(),
            beregningsperioder = listOf(
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    inntekt = Kroner(550000),
                    utbetaltBeloep = Kroner(0),
                    ytelseFoerAvkorting = Kroner(100),
                    trygdetid = 40
                ),
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    inntekt = Kroner(550000),
                    utbetaltBeloep = Kroner(0),
                    ytelseFoerAvkorting = Kroner(100),
                    trygdetid = 40
                )
            ),
            sisteBeregningsperiode = OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.now(),
                datoTOM = null,
                inntekt = Kroner(550000),
                utbetaltBeloep = Kroner(0),
                ytelseFoerAvkorting = Kroner(100),
                trygdetid = 40
            ),
            trygdetid = Trygdetid(
                trygdetidsperioder = listOf(
                    Trygdetidsperiode(
                        datoFOM = LocalDate.now(),
                        datoTOM = LocalDate.now(),
                        land = "Norge",
                        opptjeningsperiode = Periode(3, 0, 0),
                        type = TrygdetidType.FAKTISK
                    )
                ),
                beregnetTrygdetidAar = 12,
                beregnetTrygdetidMaaneder = 3,
                prorataBroek = IntBroek(250, 280),
                beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
                beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
                mindreEnnFireFemtedelerAvOpptjeningstiden = true,
            )
        ),
        etterbetalinginfo = OmstillingsstoenadEtterbetaling(
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
                    opptjeningsperiode = null,
                    type = TrygdetidType.FAKTISK
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