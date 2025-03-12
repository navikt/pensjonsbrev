package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto

fun createSamletMeldingOmPensjonsvedtakDto() =
    SamletMeldingOmPensjonsvedtakDto(
        fornavn = "Peder",
        etternavn = "Ås",
        etternavnVedFoedsel = "Holm",
        sakstype = Sakstype.ALDER
    )