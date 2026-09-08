package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmIFUReduksjonsprosentData

data class VedtakOmIFUReduksjonsprosentRedigerbarDto(
    val vedtakData: VedtakOmIFUReduksjonsprosentData,
) : FagsystemBrevdata
