package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.etterlatte.maler.BeregningsMetode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRedigerbartVedlegg
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiode
import no.nav.pensjon.etterlatte.maler.Periode
import no.nav.pensjon.etterlatte.maler.Trygdetid
import no.nav.pensjon.etterlatte.maler.TrygdetidType
import no.nav.pensjon.etterlatte.maler.Trygdetidsperiode
import java.time.LocalDate

fun createTomOmstillingsstoenadBeregningRedigerbartVedlegg(): OmstillingsstoenadBeregningRedigerbartVedlegg {
    return OmstillingsstoenadBeregningRedigerbartVedlegg(
        innhold = emptyList()
    )
}

fun createOmstillingsstoenadBeregningRedigerbartVedlegg(): OmstillingsstoenadBeregningRedigerbartVedlegg {
        return OmstillingsstoenadBeregningRedigerbartVedlegg(
        innhold = emptyList(),
        omstillingsstoenadBeregning = OmstillingsstoenadBeregning(
            innhold = emptyList(),
            virkningsdato = LocalDate.of(2020,1,1),
            beregningsperioder = listOf(
                OmstillingsstoenadBeregningsperiode(
                    datoFOM = LocalDate.of(2025, 1, 1),
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
                    erFakeSanksjon = false,
                    institusjon = false,
                )
            ),
            sisteBeregningsperiode = OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.of(2025, 1, 1),
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
                erFakeSanksjon = false,
                institusjon = false,
            ),
            sisteBeregningsperiodeNesteAar = OmstillingsstoenadBeregningsperiode(
                datoFOM = LocalDate.of(2025, 1, 1),
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
                erFakeSanksjon = false,
                institusjon = false,
            ),
            trygdetid = Trygdetid(
                navnAvdoed = "Død Sist",
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
                prorataBroek = null,
                beregningsMetodeAnvendt = BeregningsMetode.NASJONAL,
                beregningsMetodeFraGrunnlag = BeregningsMetode.BEST,
                mindreEnnFireFemtedelerAvOpptjeningstiden = false
            )
            ,
            oppphoersdato = LocalDate.of(2025, 1, 1),
            opphoerNesteAar = true,
            erYrkesskade = false
        ),
        erInnvilgelsesAar = true
    )
}

