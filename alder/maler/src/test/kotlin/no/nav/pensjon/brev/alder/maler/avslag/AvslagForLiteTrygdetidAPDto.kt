package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.VedtaksBegrunnelse
import no.nav.pensjon.brev.alder.model.avslag.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.alder.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.model.vedlegg.Trygdetid
import java.time.LocalDate

fun createAvslagForLiteTrygdetidAPDto() =
    AvslagForLiteTrygdetidAPDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = AvslagForLiteTrygdetidAPDto.PesysData(
            avtaleland = "Avtaleland",
            bostedsland = "Bostedsland",
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
            regelverkType = AlderspensjonRegelverkType.AP2016,
            borINorge = true,
        )
    )
