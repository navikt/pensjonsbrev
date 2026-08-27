package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestVedtaksbrevDto

fun createBrukerTestVedtaksbrevDto() = BrukerTestVedtaksbrevDto(
    pesysData = EmptyFagsystemdata,
    saksbehandlerValg = lagSaksbehandlervalg(
        "utsiktenFraKontoret" to BrukerTestVedtaksbrevDto.UtsiktenFraKontoret.MOT_PARKERINGSPLASSEN,
        "denBesteKaken" to BrukerTestVedtaksbrevDto.DenBesteKaken.GULROTKAKE,
        "kaffemaskinensTilgjengelighet" to true,
        "kontorplantenTorlill" to true
    )
)
