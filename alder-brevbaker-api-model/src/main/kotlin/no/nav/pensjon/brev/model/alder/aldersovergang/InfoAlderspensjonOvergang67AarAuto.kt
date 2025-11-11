package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.model.alder.YtelseForAldersovergangKode

@Suppress("unused")
data class InfoAlderspensjonOvergang67AarAutoDto(
    val ytelseForAldersovergang: YtelseForAldersovergangKode
) : BrevbakerBrevdata
