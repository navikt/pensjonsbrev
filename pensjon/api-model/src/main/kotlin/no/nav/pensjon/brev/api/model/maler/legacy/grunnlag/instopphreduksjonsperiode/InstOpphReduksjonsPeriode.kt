package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.instopphreduksjonsperiode

import java.time.LocalDate

data class InstOpphReduksjonsPeriode(
    val forsorgeransvar: Boolean,
    val fomDato: LocalDate
)
