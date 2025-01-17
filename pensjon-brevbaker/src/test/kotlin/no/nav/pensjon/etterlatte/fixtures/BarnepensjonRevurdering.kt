package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonRevurderingDTO():BarnepensjonRevurderingDTO {
    val bruktTrygdetid = Trygdetid(
        trygdetidsperioder = listOf(
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2004, 1, 1),
                datoTOM = LocalDate.of(2024, 3, 1),
                land = "NOR",
                opptjeningsperiode = Periode(20, 2, 0),
                type = TrygdetidType.FAKTISK
            ),
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2024, 4, 1),
                datoTOM = LocalDate.of(2044, 1, 1),
                land = "NOR",
                opptjeningsperiode = Periode(19, 10, 0),
                type = TrygdetidType.FREMTIDIG
            )
        ),
        beregnetTrygdetidAar = 40,
        prorataBroek = IntBroek(20, 150),
        beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
        beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
        mindreEnnFireFemtedelerAvOpptjeningstiden = false,
        navnAvdoed = "Elvis Presley"
    )
    return BarnepensjonRevurderingDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        innholdForhaandsvarsel = createPlaceholderForRedigerbartInnhold(),
        erEndret = true,
        erOmgjoering = false,
        datoVedtakOmgjoering = null,
        beregning = BarnepensjonBeregning(
            innhold = listOf(),
            virkningsdato = LocalDate.of(2024, Month.APRIL, 1),
            antallBarn = 1,
            grunnbeloep = Kroner(118620),
            beregningsperioder = listOf(
                BarnepensjonBeregningsperiode(
                    datoFOM = LocalDate.of(2024, Month.APRIL, 1),
                    datoTOM = LocalDate.of(2024, Month.MAY, 31),
                    grunnbeloep = Kroner(118620),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(8000),
                    harForeldreloessats = false,
                ),
                BarnepensjonBeregningsperiode(
                    datoFOM = LocalDate.of(2024, Month.MAY, 1),
                    datoTOM = null,
                    grunnbeloep = Kroner(118620),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(10000),
                    harForeldreloessats = false,
                ),
            ),
            sisteBeregningsperiode = BarnepensjonBeregningsperiode(
                datoFOM = LocalDate.of(2024, Month.JANUARY, 1),
                datoTOM = null,
                grunnbeloep = Kroner(118620),
                antallBarn = 1,
                utbetaltBeloep = Kroner(10000),
                harForeldreloessats = false,
                ),
            trygdetid = listOf(
                Trygdetid(
                    trygdetidsperioder = listOf(
                        Trygdetidsperiode(
                            datoFOM = LocalDate.of(2014, 1, 1),
                            datoTOM = LocalDate.of(2024, 1, 1),
                            land = "NOR",
                            opptjeningsperiode = Periode(10, 0, 0),
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
                    prorataBroek = IntBroek(20, 150),
                    beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
                    beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
                    mindreEnnFireFemtedelerAvOpptjeningstiden = false,
                    navnAvdoed = "Hubba Bubba"
                ),
                bruktTrygdetid
            ),
            erForeldreloes = true,
            bruktTrygdetid = bruktTrygdetid,
            erYrkesskade = false,
        ),
        frivilligSkattetrekk = true,
        brukerUnder18Aar = true,
        bosattUtland = false,
        kunNyttRegelverk = false,
        harFlereUtbetalingsperioder = true,
        harUtbetaling = true,
        feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL,
        erMigrertYrkesskade = false,
        erEtterbetaling = false,
    )
}

fun createBarnepensjonRevurderingRedigerbartUtfallDTO() = BarnepensjonRevurderingRedigerbartUtfallDTO(
    harUtbetaling = true,
    feilutbetaling = FeilutbetalingType.FEILUTBETALING_MED_VARSEL,
    brukerUnder18Aar = true,
    bosattUtland = false,
    frivilligSkattetrekk = false,
    erEtterbetaling = false
)