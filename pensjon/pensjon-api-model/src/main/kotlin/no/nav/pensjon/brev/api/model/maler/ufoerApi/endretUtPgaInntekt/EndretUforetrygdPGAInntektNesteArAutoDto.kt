package no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt

import no.nav.pensjon.brev.api.model.maler.AutobrevData

data class EndretUforetrygdPGAInntektNesteArAutoDto(
    val vedtakData: EndretUforetrygdPGAInntektNesteArDto,
) : AutobrevData
