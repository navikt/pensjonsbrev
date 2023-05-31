package no.nav.pensjon.etterlatte.fixtures

import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTO
import java.time.LocalDate

fun createBarnepensjonInnvilgelseDTO() =
    BarnepensjonInnvilgelseDTO(
        utbetalingsinfo = BarnepensjonInnvilgelseDTO.Utbetalingsinfo(
            antallBarn = 2,
            beloep = Kroner(1234),
            soeskenjustering = true,
            virkningsdato = LocalDate.now(),
            beregningsperioder = listOf(
                BarnepensjonInnvilgelseDTO.Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    grunnbeloep = Kroner(106003),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                ),
                BarnepensjonInnvilgelseDTO.Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    grunnbeloep = Kroner(106003),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                )
            )
        ),
        avdoed = BarnepensjonInnvilgelseDTO.AvdoedEYB(
            navn = "Avdoed Avdoedesen",
            doedsdato = LocalDate.now()
        ),
        avsender = BarnepensjonInnvilgelseDTO.Avsender(
            kontor = "NAV Familie- og pensjonsytelser Porsgrunn",
            adresse = "Kammerherreløkka 2",
            postnummer = "3915 PORSGRUNN",
            telefonnummer = Telefonnummer("55553333"),
            saksbehandler = "Sak Saksbehandler"
        ),
        mottaker = BarnepensjonInnvilgelseDTO.Mottaker(
            navn = "Mottaker Motakesen",
            adresse = "Testveien 404",
            postnummer = "1337",
            poststed = "Broslo",
            land = "Norge"
        ),
        attestant = BarnepensjonInnvilgelseDTO.Attestant(
            navn = "Attestant Attestantesen",
            kontor = "NAV Familie- og pensjonsytelser Ålesund"
        )
    )