package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Beregningsperiode
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingYrkesskadeDTO
import java.time.LocalDate
import java.time.Month

fun createBarnepensjonRevurderingYrkesskadeDTO(): BarnepensjonRevurderingYrkesskadeDTO {
    val virkningsdato = LocalDate.of(2022, Month.JUNE, 1)
    return BarnepensjonRevurderingYrkesskadeDTO(
        utbetalingsinfo = Utbetalingsinfo(
            antallBarn = 0,
            beloep = Kroner(12_000),
            soeskenjustering = false,
            virkningsdato = virkningsdato,
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
                    grunnbeloep = Kroner(118000),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                )
            )
        ), yrkesskadeErDokumentert = false,
        virkningsdato = virkningsdato
    )
}
