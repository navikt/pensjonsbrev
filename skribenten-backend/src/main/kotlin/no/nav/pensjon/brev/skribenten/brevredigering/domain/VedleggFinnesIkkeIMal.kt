package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId

data class VedleggFinnesIkkeIMal(val brevId: BrevId, val vedleggId: VedleggId) : BrevredigeringError