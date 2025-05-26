package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiode
import no.nav.pensjon.etterlatte.maler.ElementListe
import no.nav.pensjon.etterlatte.maler.IntBroek
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonInnvilgelseDTO(): BarnepensjonInnvilgelseDTO {
    val bruktTrygdetid = Trygdetid(
        trygdetidsperioder = listOf(
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2004, 1, 1),
                datoTOM = LocalDate.of(2024, 1, 1),
                land = "Norge",
                landkode = "NOR",
                opptjeningsperiode = Periode(20, 0, 0),
                type = TrygdetidType.FAKTISK
            ),
            Trygdetidsperiode(
                datoFOM = LocalDate.of(2024, 1, 1),
                datoTOM = LocalDate.of(2044, 1, 1),
                land = "Norge",
                landkode = "NOR",
                opptjeningsperiode = Periode(20, 0, 0),
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
    return BarnepensjonInnvilgelseDTO(
        innhold = createPlaceholderForRedigerbartInnhold(),
        beregning = BarnepensjonBeregning(
            innhold = ElementListe.tom(),
            virkningsdato = LocalDate.now(),
            antallBarn = 2,
            grunnbeloep = Kroner(123456),
            beregningsperioder = listOf(
                BarnepensjonBeregningsperiode(
                    datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
                    datoTOM = LocalDate.of(2023, Month.JULY, 31),
                    grunnbeloep = Kroner(123456),
                    antallBarn = 2,
                    utbetaltBeloep = Kroner(6234),
                    harForeldreloessats = true,
                )
            ),
            sisteBeregningsperiode = BarnepensjonBeregningsperiode(
                datoFOM = LocalDate.of(2020, Month.JANUARY, 1),
                datoTOM = LocalDate.of(2023, Month.JULY, 31),
                grunnbeloep = Kroner(123456),
                antallBarn = 2,
                utbetaltBeloep = Kroner(6234),
                harForeldreloessats = true,
            ),
            trygdetid = listOf(bruktTrygdetid),
            bruktTrygdetid = bruktTrygdetid,
            erYrkesskade = true,
        ),
        frivilligSkattetrekk = true,
        bosattUtland = true,
        brukerUnder18Aar = true,
        kunNyttRegelverk = false,
        erGjenoppretting = false,
        harUtbetaling = true,
        erMigrertYrkesskade = false,
        erEtterbetaling = false,
        datoVedtakOmgjoering = LocalDate.now()
    )
}

fun createBarnepensjonInnvilgelseRedigerbartUtfallDTO() = BarnepensjonInnvilgelseRedigerbartUtfallDTO(
    virkningsdato = LocalDate.of(2020, Month.JANUARY, 1),
    avdoed = Avdoed(
        navn = "Avdoed Avdoedesen",
        doedsdato = LocalDate.now().minusMonths(1),
    ),
    sisteBeregningsperiodeDatoFom = LocalDate.of(2020, Month.JANUARY, 1),
    sisteBeregningsperiodeBeloep = Kroner(1000),
    erEtterbetaling = true,
    harFlereUtbetalingsperioder = false,
    erGjenoppretting = false,
    harUtbetaling = true
)
