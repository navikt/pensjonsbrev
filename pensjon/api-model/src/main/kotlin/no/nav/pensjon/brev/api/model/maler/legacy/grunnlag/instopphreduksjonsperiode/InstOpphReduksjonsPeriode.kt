package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.instopphreduksjonsperiode

import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class InstOpphReduksjonsPeriode(
    val fasteutgifter: Kroner,
    val forsorgeransvar: Boolean,
    val fomDato: LocalDate
)
