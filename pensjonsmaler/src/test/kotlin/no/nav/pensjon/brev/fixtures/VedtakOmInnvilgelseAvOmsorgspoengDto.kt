package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto

fun createVedtakOmInnvilgelseAvOmsorgspoengDto() =
    VedtakOmInnvilgelseAvOmsorgspoengDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakOmInnvilgelseAvOmsorgspoengDto.PesysData(
            brukerNavn = "Per Pensjon",
            omsorgsopptjeningsaar = "2025",
            orienteringOmSaksbehandlingstidDto = Fixtures.create()
        )
    )