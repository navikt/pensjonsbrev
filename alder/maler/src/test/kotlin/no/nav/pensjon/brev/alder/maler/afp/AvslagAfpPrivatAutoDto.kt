package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto
import java.time.LocalDate

fun createAvslagAfpPrivatAutoDto(): AvslagAfpPrivatAutoDto =
    AvslagAfpPrivatAutoDto(
        kravMottattDato = LocalDate.of(2026, 1, 15),
        begrunnelse = AvslagAfpPrivatDto.Begrunnelse.BRUKER_AVSLAG_AP,
    )
