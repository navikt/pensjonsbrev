package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto

fun createVedtakOmFjerningAvOmsorgsopptjeningDto() = VedtakOmFjerningAvOmsorgsopptjeningDto(
    saksbehandlerValg = lagSaksbehandlervalg(
        "aktuelleAar" to "2019 og 2020",
    ),
    pesysData = VedtakOmFjerningAvOmsorgsopptjeningDto.PesysData(
        dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
    )
)