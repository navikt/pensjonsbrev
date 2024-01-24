package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.ElementType
import no.nav.pensjon.etterlatte.maler.Etterbetaling
import no.nav.pensjon.etterlatte.maler.InnerElement
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBP
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
    utbetalingsinfo = Utbetalingsinfo(
        antallBarn = 1,
        beloep = Kroner(10000),
        soeskenjustering = true,
        virkningsdato = LocalDate.of(2023, Month.MAY, 1),
        beregningsperioder = listOf(
            Beregningsperiode(
                datoFOM = LocalDate.of(2023, Month.MAY, 1),
                datoTOM = LocalDate.of(2023, Month.DECEMBER, 31),
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(8000),
            ),
            Beregningsperiode(
                datoFOM = LocalDate.of(2024, Month.JANUARY, 1),
                datoTOM = null,
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(10000),
            ),
        ),
    ),
    beregningsinfo = BeregningsinfoBP(
        innhold = listOf(),
        grunnbeloep = Kroner(118620),
        beregningsperioder = listOf(
            Beregningsperiode(
                datoFOM = LocalDate.of(2023, Month.MAY, 1),
                datoTOM = LocalDate.of(2023, Month.DECEMBER, 31),
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(8000),
            ),
            Beregningsperiode(
                datoFOM = LocalDate.of(2024, Month.JANUARY, 1),
                datoTOM = null,
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(10000),
            ),
        ),
        antallBarn = 1,
        aarTrygdetid = 30,
        maanederTrygdetid = 360,
        prorataBroek = null,
        trygdetidsperioder = listOf(
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2003, Month.JANUARY, 1),
                datoTOM = LocalDate.of(2023, Month.MAY, 31),
                land = "NOR",
                opptjeningsperiode = Periode(20, 0, 0),
                type = TrygdetidType.FAKTISK
            ),
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2023, Month.JUNE, 1),
                datoTOM = LocalDate.of(2043, Month.MAY, 31),
                land = "NOR",
                opptjeningsperiode = Periode(20, 0, 0),
                type = TrygdetidType.FREMTIDIG
            )
        ),
        beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
        beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
        mindreEnnFireFemtedelerAvOpptjeningstiden = true
    ),
    etterbetaling = Etterbetaling(
        fraDato = LocalDate.of(2023, Month.MAY, 1),
        tilDato = LocalDate.of(2023, Month.DECEMBER, 31),
        etterbetalingsperioder = listOf(
            Beregningsperiode(
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
    sisteUtbetalingsperiodeDatoFom = LocalDate.of(2024, Month.JANUARY, 1),
)
