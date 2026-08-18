package no.nav.pensjon.brev.ufore.api.model.maler.info

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class InfoEndretUTPgaInntektDto(
    val belopsgrense: Kroner,
) : AutobrevData
