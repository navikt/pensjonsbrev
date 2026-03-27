package no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev

import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto
import no.nav.pensjon.brev.alder.model.vedlegg.AfpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata

fun createAfpPrivatSokerUforeTrygdDto() = AfpPrivatSokerUforeTrygdDto(
    saksbehandlerValg = AfpPrivatSokerUforeTrygdDto.SaksBehandlerValg(harIkkeSoktUforeTrygd = false),
    pesysData = EmptyFagsystemdata,
    vedleggData = AfpPrivatSokerUforeTrygdVedleggDto(utTilattestering = false),
)