package no.nav.pensjon.brev.alder.maler.feilutbetaltbelop

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.feilutbetaltbelop.VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto
import java.time.LocalDate

fun createVedtakFeilutbetaltBeloepTilbakekrevesIkkeDto() =
    VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.PesysData(
            sakstype = Sakstype.ALDER,
            forhaandsvarselDato = LocalDate.of(2026, 8, 15),
            virkningFom = LocalDate.of(2025, 1, 1),
            virkningTom = LocalDate.of(2025, 12, 31),
            hjemmel = VedtakFeilutbetaltBeloepTilbakekrevesIkkeDto.Hjemmel.PARAGRAF_22_15,
        ),
    )
