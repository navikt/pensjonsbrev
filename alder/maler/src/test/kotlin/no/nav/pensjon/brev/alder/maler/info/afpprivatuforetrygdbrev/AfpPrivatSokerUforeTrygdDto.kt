package no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto
fun createAfpPrivatSokerUforeTrygdDto() = AfpPrivatSokerUforeTrygdDto(
    saksbehandlerValg = lagSaksbehandlervalg("harSoktUforeTrygd" to false),
    pesysData = AfpPrivatSokerUforeTrygdDto.PesysData(
        createAfpPrivatSokerUforeTrygdVedleggDto()
    ),
)
fun createAfpPrivatSokerUforeTrygdVedleggDto() = AFpPrivatSokerUforeTrygdVedleggDto(true,true)