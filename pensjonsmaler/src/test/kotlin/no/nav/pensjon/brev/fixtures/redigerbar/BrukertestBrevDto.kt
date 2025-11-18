package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.BrukerTestBrevDto

fun createBrukerTestBrevDto() = BrukerTestBrevDto(
    pesysData = EmptyFagsystemdata,
    saksbehandlerValg = BrukerTestBrevDto.SaksbehandlerValg(
        utsiktenFraKontoret = BrukerTestBrevDto.UtsiktenFraKontoret.MOT_PARKERINGSPLASSEN,
        denBesteKaken = BrukerTestBrevDto.DenBesteKaken.GULROTKAKE,
        kaffemaskinensTilgjengelighet = true,
        kontorplantenTorlill = true
    )
)