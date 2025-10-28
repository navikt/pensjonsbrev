package brev.maler.stans

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.EksportForbudKode
import no.nav.pensjon.brev.model.alder.InformasjonOmMedlemskap
import no.nav.pensjon.brev.model.alder.stans.VedtakStansAlderspensjonFlyttingMellomLandDto
import no.nav.pensjon.brev.model.alder.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import java.time.LocalDate

fun createVedtakStansAlderspensjonFlyttingMellomLandDto() =
    VedtakStansAlderspensjonFlyttingMellomLandDto(
        saksbehandlerValg = VedtakStansAlderspensjonFlyttingMellomLandDto.SaksbehandlerValg
            (feilutbetaling = false),
        pesysData = VedtakStansAlderspensjonFlyttingMellomLandDto.PesysData(
            brukersBostedsland = "Sverige",
            eksportForbudKode = EksportForbudKode.FLYKT_ALDER,
            eksportForbudKodeAvdoed = null,
            garantipensjonInnvilget = false,
            harAvdoed = true,
            kravVirkDatoFom = LocalDate.of(2025, 8, 1),
            minst20ArTrygdetid = false,
            minst20AarTrygdetidKap20Avdoed = false,
            regelverkType = AlderspensjonRegelverkType.AP2016,
            informasjonOmMedlemskap = InformasjonOmMedlemskap.UTENFOR_EOES,
            dineRettigheterOgMulighetTilAaKlage = DineRettigheterOgMulighetTilAaKlageDto(
                sakstype = Sakstype.ALDER,
                brukerUnder18Aar = false
            )
        )
    )