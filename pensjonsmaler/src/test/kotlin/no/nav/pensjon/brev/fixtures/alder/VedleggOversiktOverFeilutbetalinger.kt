package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createOversiktOverFeilutbetalingerPEDto() =
    OversiktOverFeilutbetalingPEDto(
        sakstype = Sakstype.ALDER,
        bruttoTilbakekrevdTotalbeloep = Kroner(25000),
        nettoUtenRenterTilbakekrevdTotalbeloep = Kroner(20000),
        rentetilleggSomInnkrevesTotalbeloep = null,
        resultatAvVurderingenForTotalbeloep = TilbakekrevingResultat.FULL_TILBAKEKREV,
        skattefradragSomInnkrevesTotalbeloep = Kroner(5000),
        tilbakekrevingPerMaaned = listOf(
            OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                maanedOgAar = LocalDate.of(2024, 1, 1),
                bruttobeloepTilbakekrevd = Kroner(2000),
                feilutbetaltBeloep = Kroner(2000),
                nettobeloepUtenRenterTilbakekrevd = Kroner(1800),
                resultatAvVurderingen = TilbakekrevingResultat.FULL_TILBAKEKREV,
                skattefradragSomInnkreves = Kroner(200),
                ytelsenMedFeilutbetaling = KonteringType.AP_GJT,
            ), OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                maanedOgAar = LocalDate.of(2024, 2, 1),
                bruttobeloepTilbakekrevd = Kroner(3000),
                feilutbetaltBeloep = Kroner(3000),
                nettobeloepUtenRenterTilbakekrevd = Kroner(2000),
                resultatAvVurderingen = TilbakekrevingResultat.FULL_TILBAKEKREV,
                skattefradragSomInnkreves = Kroner(1000),
                ytelsenMedFeilutbetaling = KonteringType.AFP_KRONETILLEGG,
            )
        )
    )