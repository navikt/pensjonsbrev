package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.foerstegangsvedtak.FoerstegangsvedtakUtfallDTO
import java.time.LocalDate

fun createOMSFoerstegangsvedtakUtfallDTO() =
    FoerstegangsvedtakUtfallDTO(
        virkningsdato = LocalDate.now(),
        avdoed = Avdoed(
            navn = "Avdod Avdodesen",
            doedsdato = LocalDate.now()
        ),
        utbetalingsbeloep = Kroner(12345)
)