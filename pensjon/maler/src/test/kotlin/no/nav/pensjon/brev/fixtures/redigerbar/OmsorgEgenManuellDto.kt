package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse

fun createOmsorgManuellDto() =
    OmsorgEgenManuellDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "aarEgenerklaringOmsorgspoeng" to 2024,
            "aarInnvilgetOmsorgspoeng" to 2023,
        ),
        pesysData = OmsorgEgenManuellDto.PesysData(
            ReturAdresse(
                adresseLinje1 = "Postboks 6600 Etterstad",
                postNr = "0607",
                postSted = "Oslo",
            )
        ),
    )