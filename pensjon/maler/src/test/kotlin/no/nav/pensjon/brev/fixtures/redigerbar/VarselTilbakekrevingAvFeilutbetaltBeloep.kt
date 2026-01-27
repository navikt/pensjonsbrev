package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDto

fun createVarselTilbakekrevingAvFeilutbetaltBeloep() =
    VarselTilbakekrevingAvFeilutbetaltBeloepDto(
        saksbehandlerValg = VarselTilbakekrevingAvFeilutbetaltBeloepDto.SaksbehandlerValg(
            hvisAktueltAaIleggeRentetillegg = true,
        ),
        pesysData = VarselTilbakekrevingAvFeilutbetaltBeloepDto.PesysData(
            Sakstype.ALDER
        )
    )