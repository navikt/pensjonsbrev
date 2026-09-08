package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktBunnfradragData

data class VedtakOmOktBunnfradragRedigerbarDto(
    val vedtakData: VedtakOmOktBunnfradragData,
) : FagsystemBrevdata
