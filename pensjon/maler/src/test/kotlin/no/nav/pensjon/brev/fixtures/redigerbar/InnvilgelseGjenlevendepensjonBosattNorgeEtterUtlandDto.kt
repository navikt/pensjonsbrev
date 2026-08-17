package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto
import java.time.LocalDate


fun createInnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto() =
    InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = InnvilgelseGjenlevendepensjonBosattNorgeEtterUtlandDto.PesysData(
            kravMottattDato = LocalDate.of(2026, 1, 1),
        )
    )