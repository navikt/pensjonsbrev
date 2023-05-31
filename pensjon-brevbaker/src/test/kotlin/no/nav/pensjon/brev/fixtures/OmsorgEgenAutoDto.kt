package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Year

fun createOmsorgEgenAutoDto() =
    OmsorgEgenAutoDto(
        aarEgenerklaringOmsorgspoeng = Year(2020),
        aarInnvilgetOmsorgspoeng = Year(2021),
        egenerklaeringOmsorgsarbeidDto = createEgenerklaeringOmsorgsarbeidDto()
    )


fun createEgenerklaeringOmsorgsarbeidDto() =
    EgenerklaeringOmsorgsarbeidDto(
        aarEgenerklaringOmsorgspoeng = Year(2020),
        returadresse = ReturAdresse(
            adresseLinje1 = "Postboks 6600 Etterstad",
            postNr = "0607",
            postSted = "Oslo",
        )
    )