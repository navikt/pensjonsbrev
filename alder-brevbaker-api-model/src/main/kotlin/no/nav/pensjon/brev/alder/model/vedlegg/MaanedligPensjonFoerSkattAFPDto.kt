package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.aldersovergang.OpptjeningType
import no.nav.pensjon.brevbaker.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class MaanedligPensjonFoerSkattAFPDto(
    val afpPrivatBeregningGjeldende: AFPPrivatBeregning,
    val krav: Krav,
    val afpPrivatBeregningListe: AFPPrivatBeregningListe,
    val opptjeningType: OpptjeningType,
    val belopEndring: BeloepEndring,
) : VedleggData {
    data class AFPPrivatBeregning(
        val datoFom: LocalDate,
        val datoTil: LocalDate?,
        val afpLivsvarigNetto: Kroner?,
        val kronetilleggNetto: Kroner?,
        val komptilleggNetto: Kroner?,
        val totalPensjon: Kroner,
    )

    data class Krav(
        val virkDatoFom: LocalDate,
    )

    data class AFPPrivatBeregningListe(
        val antallBeregningsperioder: Int,
        val afpPrivatBeregningListe: List<AFPPrivatBeregning>,
    )
}
