package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class MaanedligPensjonFoerSkattAP2025Dto(
    val beregnetPensjonPerManedGjeldende: AlderspensjonPerManed,
    val beregnetPensjonperManed: List<AlderspensjonPerManed>,
    val kravVirkFom: LocalDate,
) : VedleggDto {
    data class AlderspensjonPerManed(
        val inntektspensjon: Kroner,
        val totalPensjon: Kroner,
        val garantipensjon: Kroner?,
        val minstenivaIndividuell: Kroner?,
        val virkDatoFom: LocalDate,
        val virkDatoTom: LocalDate?,
    )
}
