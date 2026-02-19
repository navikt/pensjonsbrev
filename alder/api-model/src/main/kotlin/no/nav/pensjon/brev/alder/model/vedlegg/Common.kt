package no.nav.pensjon.brev.alder.model.vedlegg

import no.nav.pensjon.brev.alder.model.PoengTallsType
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate

data class Pensjonspoeng(
    val pensjonsgivendeinntekt: Kroner,
    val grunnbelopVeiet: Kroner,
    val arstall: Int,
    val pensjonspoeng: Double,
    val poengtallstype: PoengTallsType?,
    val bruktIBeregningen: Boolean,
) : VedleggData

data class Trygdetid(
    val fom: LocalDate?,
    val tom: LocalDate?,
    val land: String?,
): VedleggData

