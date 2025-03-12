package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Sakstype

data class SamletMeldingOmPensjonsvedtakDto(
    val fornavn: String,
    val etternavn: String,
    val etternavnVedFoedsel: String?,
    val sakstype: Sakstype,
) : BrevbakerBrevdata