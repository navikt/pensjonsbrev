package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

data class OpptjeningVedForhoeyetHjelpesatsDto(val aarInnvilgetOmrsorgspoeng: Year, val foedtEtter1953: Boolean)