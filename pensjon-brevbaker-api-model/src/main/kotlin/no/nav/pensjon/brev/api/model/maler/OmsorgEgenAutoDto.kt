package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brevbaker.api.model.Year

@Suppress("unused")
data class OmsorgEgenAutoDto(
    val aarEgenerklaringOmsorgspoeng: Year,
    val aarInnvilgetOmsorgspoeng: Year,
    val egenerklaeringOmsorgsarbeidDto: EgenerklaeringOmsorgsarbeidDto,
) : BrevbakerBrevdata
