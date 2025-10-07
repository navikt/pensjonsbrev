package no.nav.pensjon.brev.maler.auto

import no.nav.pensjon.brev.api.model.maler.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.auto.AFPPrivateBeregning
import no.nav.pensjon.brev.api.model.maler.auto.OpptjeningInfo
import no.nav.pensjon.brev.api.model.maler.auto.OpptjeningType
import no.nav.pensjon.brev.api.model.maler.auto.VedtakEndringAFPEndretOpptjeningAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createVedtakEndringAFPEndretOpptjeningAutoDto(): VedtakEndringAFPEndretOpptjeningAutoDto =
    VedtakEndringAFPEndretOpptjeningAutoDto(
        virkFom = LocalDate.of(2024, 7, 1),
        opptjeningType = OpptjeningType.KORRIGERING,
        belopEndring = BeloepEndring.ENDR_OKT,
        afpPrivateBeregningVedVirk = AFPPrivateBeregning(totalPensjon = Kroner(5678)),
        afpPrivatBeregningGjeldende = AFPPrivateBeregning(totalPensjon = Kroner(5678)),
        endretOpptjening =
            OpptjeningInfo(
                sisteGyldigeOpptjeningsAar = 2022,
                antallAarEndretOpptjening = 2,
                endretOpptjeningsAar = setOf(2021, 2022),
            ),
    )
