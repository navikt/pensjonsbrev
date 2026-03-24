package no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev

import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto

fun createAfpPrivatSokerUforeTrygdDto() = AfpPrivatSokerUforeTrygdDto(
    saksbehandlerValg = AfpPrivatSokerUforeTrygdDto.SaksBehandlerValg(harSoktUforeTrygd = true),
    pesysData = AfpPrivatSokerUforeTrygdDto.PesysData(uforeTrygdTil_ATT = false),
)