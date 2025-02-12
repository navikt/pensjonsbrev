package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

@Suppress("unused")
data class InfoAlderspensjonOvergang67AarAutoDto(
    val ytelseForAldersovergang: YtelseForAldersovergangKode,
) : BrevbakerBrevdata
