package no.nav.pensjon.brev.model.alder.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.aldersovergang.OpptjeningType
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class MaanedligPensjonFoerSkattAFPDto(
    val afpPrivatBeregningGjeldende: AFPPrivatBeregning,
    val krav: Krav,
    val afpPrivatBeregningListe: AFPPrivatBeregingListe,
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

    data class AFPPrivatBeregingListe(
        val antallBeregningsperioder: Int,
        val afpPrivatBeregingListe: List<AFPPrivatBeregning>,
    )
}
