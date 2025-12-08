package no.nav.pensjon.brev.alder.model.aldersovergang

import no.nav.pensjon.brev.alder.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.api.model.maler.AutobrevData

@Suppress("unused")
data class InfoAlderspensjonOvergang67AarAutoDto(
    val ytelseForAldersovergang: YtelseForAldersovergangKode,
) : AutobrevData
