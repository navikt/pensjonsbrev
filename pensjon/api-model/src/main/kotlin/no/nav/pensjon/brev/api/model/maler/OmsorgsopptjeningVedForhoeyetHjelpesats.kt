package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Year

data class OpptjeningVedForhoeyetHjelpesatsDto(val aarInnvilgetOmsorgspoeng: Year, val foedtEtter1953: Boolean):
    AutobrevData