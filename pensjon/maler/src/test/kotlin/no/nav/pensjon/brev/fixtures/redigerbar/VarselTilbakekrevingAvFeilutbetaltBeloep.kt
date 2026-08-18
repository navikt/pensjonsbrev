package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDto

fun createVarselTilbakekrevingAvFeilutbetaltBeloep() =
    VarselTilbakekrevingAvFeilutbetaltBeloepDto(
        saksbehandlerValg = lagSaksbehandlervalg("hvisAktueltAaIleggeRentetillegg" to true),
        pesysData = VarselTilbakekrevingAvFeilutbetaltBeloepDto.PesysData(
            Sakstype.ALDER
        )
    )