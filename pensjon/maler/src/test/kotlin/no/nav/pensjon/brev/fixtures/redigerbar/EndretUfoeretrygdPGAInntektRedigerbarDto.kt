package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUfoeretrygdPGAInntektRedigerbarDto
import no.nav.pensjon.brev.fixtures.createEndretUTPgaInntektDtoV2

fun createEndretUfoeretrygdPGAInntektRedigerbarDto() = EndretUfoeretrygdPGAInntektRedigerbarDto(
    saksbehandlerValg = lagSaksbehandlervalg(),
    pesysData = EndretUfoeretrygdPGAInntektRedigerbarDto.PesysData(
        data = createEndretUTPgaInntektDtoV2(),
    ),
)
