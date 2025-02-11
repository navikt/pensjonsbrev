package no.nav.brev.brevbaker.maler.example

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDto
import no.nav.pensjon.brev.maler.example.ExampleTilleggDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEksempelbrevRedigerbartDto() =
    EksempelRedigerbartDto(
        saksbehandlerValg = EmptyBrevdata,
        pesysData =
            EksempelRedigerbartDto.PesysData(
                pensjonInnvilget = true,
                datoInnvilget = LocalDate.of(2020, 1, 1),
                navneliste = listOf("test testerson1", "test testerson2", "test testerson3"),
                tilleggEksempel =
                    listOf(
                        ExampleTilleggDto(
                            navn = "Test testerson 1",
                            tillegg1 = Kroner(300),
                            tillegg3 = Kroner(500),
                        ),
                        ExampleTilleggDto(
                            navn = "Test testerson 2",
                            tillegg1 = Kroner(100),
                            tillegg2 = Kroner(600),
                        ),
                        ExampleTilleggDto(
                            navn = "Test testerson 3",
                            tillegg2 = Kroner(300),
                        ),
                    ),
                datoAvslaatt = LocalDate.of(2020, 1, 1),
                pensjonBeloep = 100,
            ),
    )
