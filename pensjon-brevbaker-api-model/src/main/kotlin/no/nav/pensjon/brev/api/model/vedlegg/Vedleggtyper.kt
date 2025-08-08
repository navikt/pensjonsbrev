package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brevbaker.api.model.VedleggType

object Vedleggtyper {
    val P1 = VedleggType("P1", "P1 – Samlet melding om pensjonsvedtak")
    val InformasjonOmP1 = VedleggType("InformasjonOmP1", "Informasjon om skjemaet P1 og hvordan det brukes")
}