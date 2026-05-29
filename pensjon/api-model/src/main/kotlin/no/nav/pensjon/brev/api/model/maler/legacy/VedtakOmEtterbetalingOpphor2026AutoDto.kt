package no.nav.pensjon.brev.api.model.maler.legacy

import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class VedtakOmEtterbetalingOpphor2026AutoDto(
    val etterbetaling: Kroner,
    val hjemler: Set<String>,
    val dineRettigheterOgPlikterUfore: DineRettigheterOgPlikterUforeDto,
) : AutobrevData
