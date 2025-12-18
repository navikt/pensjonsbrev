package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto

fun createVedtakOmInnvilgelseAvOmsorgspoengDto() =
    VedtakOmInnvilgelseAvOmsorgspoengDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakOmInnvilgelseAvOmsorgspoengDto.PesysData(
            omsorgspersonNavn = "Per Omsorgsperson Pensjon",
            omsorgsopptjeningsaar = "2025",
        )
    )
