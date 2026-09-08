package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse

fun createOmsorgManuellDto() =
    OmsorgEgenManuellDto(
        ReturAdresse(
            adresseLinje1 = "Postboks 6600 Etterstad",
            postNr = "0607",
            postSted = "Oslo",
        )
    ),