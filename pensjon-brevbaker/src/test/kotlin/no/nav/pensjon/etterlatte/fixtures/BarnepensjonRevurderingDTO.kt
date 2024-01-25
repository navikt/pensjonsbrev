package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonRevurderingDTO() = BarnepensjonRevurderingDTO(
    innhold = listOf(
        Element(
            type = ElementType.HEADING_THREE,
            children = listOf(
                InnerElement(
                    text = "Begrunnelse for vedtaket"
                )
            )
        ),
        Element(
            type = ElementType.PARAGRAPH,
            children = listOf(
                InnerElement(
                    text = "Redigerbar tekst her"
                ),
            )
        ),
    ),
    erEndret = true,
    beregning = BarnepensjonBeregning(
        innhold = listOf(),
        virkningsdato = LocalDate.of(2023, Month.MAY, 1),
        antallBarn = 1,
        grunnbeloep = Kroner(118620),
        beregningsperioder = listOf(
            BarnepensjonBeregningsperiode(
                datoFOM = LocalDate.of(2023, Month.MAY, 1),
                datoTOM = LocalDate.of(2023, Month.DECEMBER, 31),
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(8000),
            ),
            BarnepensjonBeregningsperiode(
                datoFOM = LocalDate.of(2024, Month.JANUARY, 1),
                datoTOM = null,
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(10000),
            ),
        ),
        sisteBeregningsperiode = BarnepensjonBeregningsperiode(
            datoFOM = LocalDate.of(2024, Month.JANUARY, 1),
            datoTOM = null,
            grunnbeloep = Kroner(118620),
            antallBarn = 1,
            utbetaltBeloep = Kroner(10000),
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
    etterbetaling = BarnepensjonEtterbetaling(
        fraDato = LocalDate.of(2023, Month.MAY, 1),
        tilDato = LocalDate.of(2023, Month.DECEMBER, 31),
        etterbetalingsperioder = listOf(
            BarnepensjonBeregningsperiode(
                datoFOM = LocalDate.of(2023, Month.MAY, 1),
                datoTOM = LocalDate.of(2023, Month.DECEMBER, 31),
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(8000)
            )
        )
    ),
    brukerUnder18Aar = true,
    bosattUtland = false,
    kunNyttRegelverk = false,
    harFlereUtbetalingsperioder = true,
)
