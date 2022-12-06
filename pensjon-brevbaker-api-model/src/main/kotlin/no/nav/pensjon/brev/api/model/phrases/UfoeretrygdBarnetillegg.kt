package no.nav.pensjon.brev.api.model.phrases

import no.nav.pensjon.brev.api.model.Kroner

interface UfoeretrygdBarnetilleggIkkeUtbetalt {
    val utbetalt: Boolean
    val antallBarn: Int
    val inntektstak: Kroner
}