package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode.BOKMAL
import no.nav.pensjon.brevbaker.api.model.LanguageCode.ENGLISH
import no.nav.pensjon.brevbaker.api.model.VedleggType

object Vedleggtyper {
    val P1 = VedleggType(
        mapOf(
            BOKMAL to "P1 – Samlet melding om pensjonsvedtak",
            ENGLISH to "P1 – Summary of Pension Decisions"
        )
    )
    val InformasjonOmP1 = VedleggType(
        mapOf(
            BOKMAL to "Informasjon om skjemaet P1 og hvordan det brukes",
            ENGLISH to "Information about the P1 form and its use"
        )
    )
} // TODO: tittel bør heller liggje som expression ein annan stad