package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto

fun createVedtakOmFjerningAvOmsorgsopptjeningDto() = VedtakOmFjerningAvOmsorgsopptjeningDto(
    dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
)