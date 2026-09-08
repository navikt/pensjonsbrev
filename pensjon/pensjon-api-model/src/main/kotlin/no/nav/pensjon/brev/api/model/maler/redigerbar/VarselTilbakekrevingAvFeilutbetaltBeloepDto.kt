package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata

@Suppress("unused")
data class VarselTilbakekrevingAvFeilutbetaltBeloepDto(
    val sakstype: Sakstype
) : FagsystemBrevdata
