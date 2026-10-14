package no.nav.pensjon.brev.api.model.maler.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

//todo: har ikke sjekket mye her ift nullability og uthenting
data class Institusjonsopphold(
    val erAnvendt: Boolean,
    val type: JustertPeriodeCode,
    val fasteUtgifter: Kroner,
    val reduksjonsperioder: List<Reduksjonsperiode>
) {
    //todo dette er ca de funksjonene som fantes på pe, sjekk de
    val allePerioderHarForsorgeransvar: Boolean =
        reduksjonsperioder.all { it.forsorgeransvar }

    val ingenPerioderHarForsorgeransvar: Boolean =
        reduksjonsperioder.none { it.forsorgeransvar }

    val sistePeriodeHarForsorgeransvar: Boolean = reduksjonsperioder.maxBy {it.fomDato}.forsorgeransvar

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