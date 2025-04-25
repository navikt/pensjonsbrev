package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.PoengTallsType
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class Pensjonspoeng(
    val pensjonsgivendeinntekt: Kroner,
    val grunnbelopVeiet: Kroner,
    val arstall: Int,
    val pensjonspoeng: Double,
    val poengtallstype: PoengTallsType?,
    val bruktIBeregningen: Boolean,
)

data class Trygdetid(
    val fom: LocalDate?,
    val tom: LocalDate?,
    val land: String?,
)
