package no.nav.pensjon.brev.model.alder.aldersovergang

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class VedtakEndringAFPEndretOpptjeningAutoDto(
    val virkFom: LocalDate,
    val opptjeningType: OpptjeningType,
    val belopEndring: BeloepEndring,
    val afpPrivatBeregningVedVirk: AFPPrivatBeregning,
    val afpPrivatBeregningGjeldende: AFPPrivatBeregning,
    val endretOpptjening: OpptjeningInfo,
    val maanedligPensjonFoerSkattAFP: MaanedligPensjonFoerSkattAFPDto?,
) : BrevbakerBrevdata

data class AFPPrivatBeregning(
    val totalPensjon: Kroner,
)

data class OpptjeningInfo(
    val sisteGyldigeOpptjeningsAar: Int?,
    val antallAarEndretOpptjening: Int,
    val endretOpptjeningsAar: Set<Int>,
)

enum class OpptjeningType {
    TILVEKST,
    KORRIGERING,
}
