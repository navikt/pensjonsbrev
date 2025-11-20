package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmFjerningAvOmsorgsopptjeningDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmInnvilgelseAvOmsorgspoengDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto

fun createVedtakOmInnvilgelseAvOmsorgspoengDto() =
    VedtakOmInnvilgelseAvOmsorgspoengDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakOmInnvilgelseAvOmsorgspoengDto.PesysData(
            omsorgspersonNavn = "Per Omsorgsperson Pensjon",
            omsorgsopptjeningsaar = "2025",
        )
    )
