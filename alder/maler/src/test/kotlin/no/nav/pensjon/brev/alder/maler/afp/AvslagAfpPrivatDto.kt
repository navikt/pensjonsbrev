package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afp.AvslagAfpPrivatDto
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import java.time.LocalDate

fun createAvslagAfpPrivatDto(): AvslagAfpPrivatDto =
    AvslagAfpPrivatDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = AvslagAfpPrivatDto.PesysData(
            kravMottattDato = LocalDate.of(2026, 1, 15),
            begrunnelse = AvslagAfpPrivatDto.Begrunnelse.BRUKER_AVSLAG_AP,
        ),
    )
