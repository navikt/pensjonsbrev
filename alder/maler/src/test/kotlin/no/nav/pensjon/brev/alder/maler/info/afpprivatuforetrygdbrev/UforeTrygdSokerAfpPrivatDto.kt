package no.nav.pensjon.brev.alder.maler.info.afpprivatuforetrygdbrev

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.UforeTrygdSokerAfpPrivatDto
import no.nav.pensjon.brev.api.model.maler.EmptyFagsystemdata

fun createUforeTrygdSokerAfpPrivatDto() =
    UforeTrygdSokerAfpPrivatDto(
        saksbehandlerValg = lagSaksbehandlervalg("brukerHarSoktAfpPrivat" to false),
        pesysData = EmptyFagsystemdata,



    )