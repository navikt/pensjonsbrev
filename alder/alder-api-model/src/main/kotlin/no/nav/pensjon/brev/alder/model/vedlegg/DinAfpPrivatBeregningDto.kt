package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import java.time.LocalDate

/**
 * Vedlegg «Din AfP Privat Beregning» for innvilgelse av AFP i privat sektor.
 */
data class DinAfpPrivatBeregningDto(
    val brukerUnder70Aar: Boolean,
    val bosattINorge: Boolean,
    val totalPensjon: BrevbakerType.Kroner,
    val livsvarigBrutto: BrevbakerType.Kroner?,
    val kronetilleggBrutto: BrevbakerType.Kroner?,
    val kompensasjonstilleggBrutto: BrevbakerType.Kroner?,
    val opptjening: BrevbakerType.Kroner,
    val forholdstallUttak: Double,
    val justeringsbeloep: BrevbakerType.Kroner?,
    val referansebeloep: BrevbakerType.Kroner?,
    val kompensasjonstilleggForholdstall: Double?,
    val harEtterbetaling: Boolean,
    val etterbetalingVirkningFom: LocalDate?,
    val etterbetalingVirkningTom: LocalDate?,
) : VedleggData
