package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndring
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createVedtakAfpPrivatEndringOpptjeningAutoDto(): VedtakAfpPrivatEndringOpptjeningAutoDto =
    VedtakAfpPrivatEndringOpptjeningAutoDto(
        virkningFom = LocalDate.of(2024, 5, 1),
        brukerAlder = 65,
        beregning = AfpPrivatBeregningEndring(
            livsvarig = Kroner(8_000),
            kronetillegg = Kroner(1_500),
            kompensasjonstillegg = Kroner(500),
            sumAfpFoerSkatt = Kroner(10_000),
        ),
        borIForNorge = true,
    )
