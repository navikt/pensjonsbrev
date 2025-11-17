package no.nav.pensjon.brev.model.alder.aldersovergang

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.model.alder.YtelseForAldersovergangKode

@Suppress("unused")
data class InfoAlderspensjonOvergang67AarAutoDto(
    val ytelseForAldersovergang: YtelseForAldersovergangKode,
) : AutobrevData
