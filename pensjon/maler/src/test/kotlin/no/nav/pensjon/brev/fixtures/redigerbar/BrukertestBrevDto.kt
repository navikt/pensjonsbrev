package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto

fun createBrukerTestBrevDto() = BrukerTestBrevDto(
    pesysData = EmptyFagsystemdata,
    saksbehandlerValg = lagSaksbehandlervalg(
        "utsiktenFraKontoret" to BrukerTestBrevDto.UtsiktenFraKontoret.MOT_PARKERINGSPLASSEN,
        "denBesteKaken" to BrukerTestBrevDto.DenBesteKaken.GULROTKAKE,
        "kaffemaskinensTilgjengelighet" to true,
        "kontorplantenTorlill" to true
    )
)