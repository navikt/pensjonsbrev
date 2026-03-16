package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OpphoerGjenlevendepensjonDto.*

fun createOpphoerGjenlevendepensjonDto() =
    OpphoerGjenlevendepensjonDto(
        saksbehandlerValg = SaksbehandlerValg(
            gifterSeg = false,
            inngaaPartnerskap = false,
            blirSamboerBarn = false,
            erSamboerBarn = false,
            blirSamboerTidligereGift = false,
            tilbakekreving = false
        ),
        pesysData = EmptyFagsystemdata
    )