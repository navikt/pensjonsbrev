package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata
import no.nav.pensjon.brevbaker.api.model.Year

data class ReturAdresse(val adresseLinje1: String, val postNr: String, val postSted: String)

data class EgenerklaeringOmsorgsarbeidDto(
    val aarEgenerklaringOmsorgspoeng: Year,
    val returadresse: ReturAdresse,
) : VedleggBrevdata