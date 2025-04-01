package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse
import no.nav.pensjon.brevbaker.api.model.Year

fun createOmsorgManuellDto() =
    OmsorgEgenManuellDto(
        saksbehandlerValg = OmsorgEgenManuellDto.SaksbehandlerValg(
            "Peder Ås",
            Year(3),
            Year(1)
        ),
        pesysData = OmsorgEgenManuellDto.PesysData(
            ReturAdresse(
                adresseLinje1 = "Postboks 6600 Etterstad",
                postNr = "0607",
                postSted = "Oslo",
            )
        ),
    )