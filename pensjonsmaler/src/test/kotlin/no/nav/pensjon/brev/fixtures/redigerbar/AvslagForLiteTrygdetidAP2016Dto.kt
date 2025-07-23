package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016Dto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.Trygdetid
import java.time.LocalDate

fun createAvslagForLiteTrygdetidAP2016Dto() =
    AvslagForLiteTrygdetidAP2016Dto(
        saksbehandlerValg = EmptyBrevdata,
        pesysData = AvslagForLiteTrygdetidAP2016Dto.PesysData(
            avtaleland = "Avtaleland",
            bostedsland = "Bostedsland",
            erAvtaleland = false,
            erEOSland = true,
            trygdeperioderNorge = listOf(
                Trygdetid(
                    fom = LocalDate.of(2024, 1, 1),
                    tom = LocalDate.of(2025, 1, 31),
                    land = "Norge",
                )
            ),
            trygdeperioderEOSland = listOf(
                Trygdetid(
                    fom = LocalDate.of(2025, 1, 1),
                    tom = LocalDate.of(2025, 3, 31),
                    land = "Storbrittania"
                )
            ),
            trygdeperioderAvtaleland = emptyList(),
            vedtaksBegrunnelse = VedtaksBegrunnelse.UNDER_20_AR_BO_2016,
            dineRettigheterOgMulighetTilAaKlageDto = DineRettigheterOgMulighetTilAaKlageDto(
                sakstype = Sakstype.ALDER,
                brukerUnder18Aar = false
            ),
        )
    )
