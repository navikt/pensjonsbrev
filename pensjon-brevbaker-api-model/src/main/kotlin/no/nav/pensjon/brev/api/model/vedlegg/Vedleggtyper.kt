package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LanguageCode.BOKMAL
import no.nav.pensjon.brevbaker.api.model.LanguageCode.ENGLISH

enum class Vedleggtyper(val tittel: Map<LanguageCode, String>) {
    P1(
        tittel = mapOf(
            BOKMAL to "P1 – Samlet melding om pensjonsvedtak",
            ENGLISH to "P1 – Summary of Pension Decisions"
        )
    ),
    InformasjonOmP1(
        tittel = mapOf(
            BOKMAL to "Informasjon om skjemaet P1 og hvordan det brukes",
            ENGLISH to "Information about the P1 form and its use"
        )
    )
}