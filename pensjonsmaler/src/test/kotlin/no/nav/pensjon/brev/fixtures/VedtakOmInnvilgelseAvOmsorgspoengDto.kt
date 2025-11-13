package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto

fun createVedtakOmInnvilgelseAvOmsorgspoengDto() =
    VedtakOmInnvilgelseAvOmsorgspoengDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = createVedtakOmInnvilgelseAvOmsorgspoengDto()
    )