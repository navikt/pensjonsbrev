package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import java.time.LocalDate

fun createAvslagForLiteTrygdetidDto() =
    AvslagForLiteTrygdetidAPDto(
        saksbehandlerValg = EmptyBrevdata,
        pesysData = AvslagForLiteTrygdetidAPDto.PesysData(
            avtaleland = "Avtaleland",
            bostedsland = "Bostedsland",
            erAvtaleland = false,
            erEOSland = false,
            regelverkType = AlderspensjonRegelverkType.AP2011,
            trygdeperioderNorge = listOf(
                AvslagForLiteTrygdetidAPDto.Trygdetid(
                    fom = LocalDate.of(2024, 1, 1),
                    tom = LocalDate.of(2025, 1, 31),
                    land = "Norge",
                )
            ),
            trygdeperioderEOSland = listOf(
                AvslagForLiteTrygdetidAPDto.Trygdetid(
                    fom = LocalDate.of(2025, 1, 1),
                    tom = LocalDate.of(2025, 3, 31),
                    land = "Storbrittania"
                )
            ),
            trygdeperioderAvtaleland = emptyList(),
            vedtaksBegrunnelse = VedtaksBegrunnelse.UNDER_1_AR_TT,
            dineRettigheterOgMulighetTilAaKlageDto = DineRettigheterOgMulighetTilAaKlageDto(
                sakstype = Sakstype.ALDER,
                brukerUnder18Aar = false
            ),
        )
    )
