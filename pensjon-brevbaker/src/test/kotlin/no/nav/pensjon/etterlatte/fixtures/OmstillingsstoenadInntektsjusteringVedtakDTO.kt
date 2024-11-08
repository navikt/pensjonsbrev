package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVedtakDTO
import java.time.LocalDate

fun createOmstillingsstoenadInntektsjusteringVedtakDTO() = OmstillingsstoenadInntektsjusteringVedtakDTO(
    innhold = createPlaceholderForRedigerbartInnhold(),
    beregning =
    OmstillingsstoenadBeregning(
        innhold = createPlaceholderForRedigerbartInnhold(),
        virkningsdato = LocalDate.of(2024, 1, 1),
        beregningsperioder =
        listOf(
            OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.of(2024, 1, 1),
                datoTOM = LocalDate.of(2024, 1, 31),
                inntekt = Kroner(500000),
                oppgittInntekt = Kroner(600000),
                fratrekkInnAar = Kroner(100000),
                innvilgaMaaneder = 12,
                grunnbeloep = Kroner(118620),
                utbetaltBeloep = Kroner(10000),
                restanse = Kroner(300),
                ytelseFoerAvkorting = Kroner(20000),
                trygdetid = 40,
                sanksjon = false,
                institusjon = false,
            ),
            OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.of(2024, 2, 1),
                datoTOM = null,
                inntekt = Kroner(500000),
                oppgittInntekt = Kroner(600000),
                fratrekkInnAar = Kroner(100000),
                innvilgaMaaneder = 12,
                grunnbeloep = Kroner(118620),
                utbetaltBeloep = Kroner(9000),
                restanse = Kroner(300),
                ytelseFoerAvkorting = Kroner(22000),
                trygdetid = 40,
                sanksjon = false,
                institusjon = false,
            ),
        ),
        sisteBeregningsperiode =
        OmstillingsstoenadBeregningsperiode(
            datoFOM = LocalDate.of(2024, 2, 1),
            datoTOM = null,
            inntekt = Kroner(500000),
            oppgittInntekt = Kroner(600000),
            fratrekkInnAar = Kroner(100000),
            innvilgaMaaneder = 10,
            grunnbeloep = Kroner(118620),
            utbetaltBeloep = Kroner(9000),
            restanse = Kroner(300),
            ytelseFoerAvkorting = Kroner(22000),
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
                    datoFOM = LocalDate.of(2004, 1, 1),
                    datoTOM = LocalDate.of(2024, 1, 1),
                    land = "NOR",
                    opptjeningsperiode = Periode(20, 0, 0),
                    type = TrygdetidType.FAKTISK,
                ),
                Trygdetidsperiode(
                    datoFOM = LocalDate.of(2024, 1, 1),
                    datoTOM = LocalDate.of(2044, 1, 1),
                    land = "NOR",
                    opptjeningsperiode = Periode(20, 0, 0),
                    type = TrygdetidType.FREMTIDIG,
                ),
            ),
            beregnetTrygdetidAar = 40,
            prorataBroek = IntBroek(20, 150),
            beregningsMetodeFraGrunnlag = BeregningsMetode.NASJONAL,
            beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
            mindreEnnFireFemtedelerAvOpptjeningstiden = false,
            navnAvdoed = "Elvis Presley",
        ),
        oppphoersdato = LocalDate.of(2024, 12, 1),
        opphoerNesteAar = false,
    ),
    omsRettUtenTidsbegrensning = false,
    tidligereFamiliepleier = false,
    inntektsaar = 2025,
    innvilgetMindreEnnFireMndEtterDoedsfall = false,
    harUtbetaling = true,
    endringIUtbetaling = false,
    virkningstidspunkt = LocalDate.of(2024,1,1),
    bosattUtland = false,
)