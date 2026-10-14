package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto
import java.time.LocalDate

fun createVedtakFeilutbetaltBeloepTilbakekrevesIkkeDto() =
    VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.PesysData(
            sakstype = Sakstype.ALDER,
            erForeloepigUfoerepensjon = false,
            forhaandsvarselDato = LocalDate.of(2026, 8, 15),
            virkningFom = LocalDate.of(2025, 1, 1),
            virkningTom = LocalDate.of(2025, 12, 31),
            hjemmel = VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.Hjemmel.PARAGRAF_22_15,
        ),
    )
