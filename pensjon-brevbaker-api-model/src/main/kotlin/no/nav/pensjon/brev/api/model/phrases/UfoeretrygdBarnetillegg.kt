package no.nav.pensjon.brev.api.model.phrases

import no.nav.pensjon.brev.api.model.Kroner

interface InnvilgetBarnetillegg {
    val utbetalt: Boolean
    val gjelderFlereBarn: Boolean
    val inntektstak: Kroner
}