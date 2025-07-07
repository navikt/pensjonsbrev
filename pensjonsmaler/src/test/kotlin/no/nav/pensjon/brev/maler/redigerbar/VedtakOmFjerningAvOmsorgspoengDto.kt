package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgspoengDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto

fun createVedtakOmFjerningAvOmsorgspoengDto() = VedtakOmFjerningAvOmsorgspoengDto(
    saksbehandlerValg = VedtakOmFjerningAvOmsorgspoengDto.SaksbehandlerValg(
        aktuelleAar = "2019 og 2020"
    ),
    pesysData = VedtakOmFjerningAvOmsorgspoengDto.PesysData(
        dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
    )
)