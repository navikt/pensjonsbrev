package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor

import java.time.LocalDate

data class UforetrygdEtteroppgjor(
    val barnetilleggfb: Boolean?,
    val barnetilleggsb: Boolean?,
    val periodefom: LocalDate?,
    val periodetom: LocalDate?,
    val uforetrygdetteroppgjordetaljbruker: UforetrygdEtteroppgjorDetaljBruker?,
    val uforetrygdetteroppgjordetaljeps: UforetrygdEtteroppgjorDetaljEPS?,


)
