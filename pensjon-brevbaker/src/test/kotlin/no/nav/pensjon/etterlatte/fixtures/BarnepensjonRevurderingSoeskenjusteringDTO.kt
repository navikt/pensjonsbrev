package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingSoeskenjusteringDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonSoeskenjusteringGrunn
import java.time.LocalDate

fun createBarnepensjonRevurderingSoeskenjusteringDTO() =
    BarnepensjonRevurderingSoeskenjusteringDTO(
        grunnForJustering = BarnepensjonSoeskenjusteringGrunn.NYTT_SOESKEN,
        utbetalingsinfo = Utbetalingsinfo(
            antallBarn = 3,
            beloep = Kroner(1231),
            soeskenjustering = true,
            virkningsdato = LocalDate.of(2022, 8, 1),
            beregningsperioder = listOf(
                BarnepensjonBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    grunnbeloep = Kroner(106003),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                ),
                BarnepensjonBeregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    grunnbeloep = Kroner(118000),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                )
            )
        ),
    )