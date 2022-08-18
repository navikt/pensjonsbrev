package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDto

@Suppress("unused")
data class OmsorgEgenAutoDto(
    val aarEgenerklaringOmsorgspoeng: Year,
    val aarInnvilgetOmsorgspoeng: Year,
    val egenerklaeringOmsorgsarbeidDto: EgenerklaeringOmsorgsarbeidDto
)
