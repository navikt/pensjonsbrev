package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Telefonnummer
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTO
import java.time.LocalDate

fun createBarnepensjonVedtakDTO() =
    BarnepensjonVedtakDTO(
        utbetalingsinfo = BarnepensjonVedtakDTO.Utbetalingsinfo(
            antallBarn = 2,
            beloep = Kroner(1234),
            soeskenjustering = true,
            virkningsdato = LocalDate.now(),
            beregningsperioder = listOf(
                BarnepensjonVedtakDTO.Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = LocalDate.now(),
                    grunnbeloep = Kroner(106003),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                ),
                BarnepensjonVedtakDTO.Beregningsperiode(
                    datoFOM = LocalDate.now(),
                    datoTOM = null,
                    grunnbeloep = Kroner(106003),
                    antallBarn = 1,
                    utbetaltBeloep = Kroner(495),
                )
            )
        ),
        avdoed = BarnepensjonVedtakDTO.AvdoedEYB(
            navn = "Avdoed Avdoedesen",
            doedsdato = LocalDate.now()
        ),
        avsender = BarnepensjonVedtakDTO.Avsender(
            kontor = "NAV Familie- og pensjonsytelser Porsgrunn",
            adresse = "Kammerherreløkka 2",
            postnummer = "3915 PORSGRUNN",
            telefonnummer = Telefonnummer("55553333"),
            saksbehandler = "Sak Saksbehandler"
        ),
        mottaker = BarnepensjonVedtakDTO.Mottaker(
            navn = "Mottaker Motakesen",
            adresse = "Testveien 404",
            postnummer = "1337",
            poststed = "Broslo",
            land = "Norge"
        ),
        attestant = BarnepensjonVedtakDTO.Attestant(
            navn = "Attestant Attestantesen",
            kontor = "NAV Familie- og pensjonsytelser Ålesund"
        )
    )