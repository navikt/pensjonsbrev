package brev.maler.aldersovergang

import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.aldersovergang.AFPPrivateBeregning
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningInfo
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningType
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakEndringAFPEndretOpptjeningAutoDto
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
