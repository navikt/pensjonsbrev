package no.nav.pensjon.brev.alder.model.aldersovergang

import no.nav.pensjon.brev.alder.model.BorMedSivilstand
import no.nav.pensjon.brev.alder.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.Kroner

@Suppress("unused")
data class InfoAlderspensjonOvergang67AarAutoDto(
    val ytelseForAldersovergang: YtelseForAldersovergangKode,
    val borMedSivilstand: BorMedSivilstand?,
    val over2G: Boolean?,
    val kronebelop2G: Kroner?,
) : AutobrevData
