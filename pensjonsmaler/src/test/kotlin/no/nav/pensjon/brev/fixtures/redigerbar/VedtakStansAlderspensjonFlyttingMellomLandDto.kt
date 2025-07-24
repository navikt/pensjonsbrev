package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.EksportForbudKode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDto
import java.time.LocalDate

fun createVedtakStansAlderspensjonFlyttingMellomLandDto() =
    VedtakStansAlderspensjonFlyttingMellomLandDto(
        saksbehandlerValg = VedtakStansAlderspensjonFlyttingMellomLandDto.SaksbehandlerValg
            (feilutbetaling = false),
        pesysData = VedtakStansAlderspensjonFlyttingMellomLandDto.PesysData(
            brukersBostedsland = "Sverige",
            eksportForbudKode = EksportForbudKode.FLYKT_ALDER,
            eksportForbudKodeAvdoed = EksportForbudKode.FLYKT_ALDER,
            garantipensjonInnvilget = false,
            harAvdod = false,
            kravVirkDatoFom = LocalDate.of(2025, 8, 1),
            minst20ArTrygdetid = false,
            minst20AarAvdoed = false,
            regelverkType = AP2016,
            dineRettigheterOgMulighetTilAaKlageDto = DineRettigheterOgMulighetTilAaKlageDto(
                sakstype = Sakstype.ALDER,
                brukerUnder18Aar = false
            ),
            informasjonOmMedlemskapOgHelserettigheterDto = InformasjonOmMedlemskapOgHelserettigheterDto(
                erEOSLand = false,
            )
        )
    )