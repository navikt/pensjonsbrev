package no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev

import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AFpPrivatSokerUforeTrygdVedleggDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto

fun createAfpPrivatSokerUforeTrygdDto() = AfpPrivatSokerUforeTrygdDto(
    createAfpPrivatSokerUforeTrygdVedleggDto()
)

fun createAfpPrivatSokerUforeTrygdVedleggDto() = AFpPrivatSokerUforeTrygdVedleggDto(true, true)