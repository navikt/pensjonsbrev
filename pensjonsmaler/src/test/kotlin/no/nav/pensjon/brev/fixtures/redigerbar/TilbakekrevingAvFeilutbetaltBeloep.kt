package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.TilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.YearMonth


fun createTilbakekrevingAvFeilutbetaltBeloepDto() =
    TilbakekrevingAvFeilutbetaltBeloepDto(
        pesysData = TilbakekrevingAvFeilutbetaltBeloepDto.PesysData(
            feilutbetaltTotalBeloep = Kroner(25000),
            resultatAvVurderingenForTotalBeloep = TilbakekrevingResultat.FULL_TILBAKEKREV,
            sakstype = Sakstype.ALDER,
            sluttPeriodeForTilbakekreving = LocalDate.of(2024, 1, 1),
            startPeriodeForTilbakekreving = LocalDate.of(2024, 12, 31),
            sumTilInnkrevingTotalBeloep = Kroner(25000),
            dineRettigheterOgMulighetTilAaKlageDto = DineRettigheterOgMulighetTilAaKlageDto(
                sakstype = Sakstype.ALDER,
                brukerUnder18Aar = false
            ),
            oversiktOverFeilutbetalingPEDto = OversiktOverFeilutbetalingPEDto(
                bruttoTilbakekrevdTotalbeloep = Kroner(25000),
                nettoUtenRenterTilbakekrevdTotalbeloep = Kroner(20000),
                rentetilleggSomInnkrevesTotalbeloep = Kroner(0),
                resultatAvVurderingenForTotalbeloep = TilbakekrevingResultat.FULL_TILBAKEKREV,
                skattefradragSomInnkrevesTotalbeloep = Kroner(0),
                tilbakekrevingPerMaaned = listOf(
                    OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                        maanedOgAar = YearMonth.of(2024, 1),
                        bruttobeloepTilbakekrevd = Kroner(2000),
                        feilutbetaltBeloep = Kroner(2000),
                        nettobeloepUtenRenterTilbakekrevd = Kroner(1800),
                        resultatAvVurderingen = TilbakekrevingResultat.FULL_TILBAKEKREV,
                        skattefradragSomInnkreves = Kroner(0),
                        ytelsenMedFeilutbetaling = KonteringType.AP_GJT,
                    ), OversiktOverFeilutbetalingPEDto.Tilbakekreving(
                        maanedOgAar = YearMonth.of(2024, 2),
                        bruttobeloepTilbakekrevd = Kroner(3000),
                        feilutbetaltBeloep = Kroner(3000),
                        nettobeloepUtenRenterTilbakekrevd = Kroner(2000),
                        resultatAvVurderingen = TilbakekrevingResultat.FULL_TILBAKEKREV,
                        skattefradragSomInnkreves = Kroner(0),
                        ytelsenMedFeilutbetaling = KonteringType.AFP_KRONETILLEGG,
                    )
                )
            )
        ),
        saksbehandlerValg = EmptyBrevdata,
    )


