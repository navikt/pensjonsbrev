package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.Year

data class OpptjeningVedForhoeyetHjelpesatsDto(val aarInnvilgetOmsorgspoeng: Year, val foedtEtter1953: Boolean) : BrevbakerBrevdata
