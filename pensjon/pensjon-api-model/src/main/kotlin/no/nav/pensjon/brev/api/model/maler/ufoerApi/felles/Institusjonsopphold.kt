package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class Institusjonsopphold(
    val erAnvendt: Boolean,
    val type: JustertPeriodeCode?,
    val fasteUtgifter: Kroner?,
    val reduksjonsperioder: List<Reduksjonsperiode>,
    val allePerioderHarForsorgeransvar: Boolean,
    val ingenPerioderHarForsorgeransvar: Boolean
) {

    data class Reduksjonsperiode(
        val forsorgeransvar: Boolean,
        val fomDato: LocalDate
    )

    enum class JustertPeriodeCode {
        EPS_INST,
        OKNING,
        REDUKSJON,
        REDUKSJON_FO,
        REDUKSJON_FRIPERIODE,
        REDUKSJON_HS
    }
}