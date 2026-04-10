package no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev

import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto
fun createAfpPrivatSokerUforeTrygdDto() = AfpPrivatSokerUforeTrygdDto(
    saksbehandlerValg = AfpPrivatSokerUforeTrygdDto.SaksBehandlerValg(harSoktUforeTrygd = false),
    pesysData = AfpPrivatSokerUforeTrygdDto.PesysData(
        createAfpPrivatSokerUforeTrygdVedleggDto()
    ),
)
fun createAfpPrivatSokerUforeTrygdVedleggDto() = AFpPrivatSokerUforeTrygdVedleggDto(true,true)