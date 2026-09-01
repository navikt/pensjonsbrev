package no.nav.pensjon.brev.alder.model.afpprivat

import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class VedtakEndringAFPEndretOpptjeningAutoDto(
    val virkFom: LocalDate,
    val opptjeningType: OpptjeningType,
    val belopEndring: BeloepEndring,
    val afpPrivatBeregningVedVirk: AFPPrivatBeregning,
    val afpPrivatBeregningGjeldende: AFPPrivatBeregning,
    val endretOpptjening: OpptjeningInfo,
    val maanedligPensjonFoerSkattAFP: MaanedligPensjonFoerSkattAFPDto?,
) : AutobrevData

data class AFPPrivatBeregning(
    val totalPensjon: Kroner,
)

data class OpptjeningInfo(
    val sisteGyldigeOpptjeningsAar: Int?,
    val antallAarEndretOpptjening: Int,
    val endretOpptjeningsAar: List<Int>,
)

enum class OpptjeningType {
    TILVEKST,
    KORRIGERING,
}
