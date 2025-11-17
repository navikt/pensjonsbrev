package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.Year
import no.nav.pensjon.brevbaker.api.model.maler.AutobrevData

data class OpptjeningVedForhoeyetHjelpesatsDto(val aarInnvilgetOmsorgspoeng: Year, val foedtEtter1953: Boolean):
    AutobrevData