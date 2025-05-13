package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvAlderspensjonSivilstandDto() =
    EndringAvAlderspensjonSivilstandDto(
        saksbehandlerValg = EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg
            (
            samboereMedFellesBarn = false,
            samboereTidligereGift = false,
            epsInntektOekningReduksjon = false,
            fraFlytet = false,
            giftBorIkkeSammen = false,
            institusjonsopphold = false,
            epsIkkeFylt62Aar = false,
            epsIkkeRettTilFullAlderspensjon = false,
            epsAvkallPaaEgenAlderspenspensjon = false,
            epsTarUtAlderspensjon = false,
            epsHarRettTilFullAlderspensjon = false,
            epsTarUtUfoeretrygd = false,
            epsHarInntektOver1G = false,
            epsTarUtAlderspensjonIStatligSektor = false,
            aarligKontrollEPS = true,
            endringPensjon = false,
            epsAvkallPaaEgenUfoeretrygd = false,
            etterbetaling = false,
            feilutbetaling = false,
            ingenBetydning = false,
            opplysninger1 = false,
            opplysninger2 = false,
            pensjonenOeker = false,
            pensjonenRedusert = false,
        ),
        pesysData = EndringAvAlderspensjonSivilstandDto.PesysData(
            alderspensjonVedVirk = EndringAvAlderspensjonSivilstandDto.AlderspensjonVedVirk(
                garantipensjonInnvilget = false,
                minstenivaaIndividuellInnvilget = false,
                minstenivaaPensjonsistParInnvilget = false,
                pensjonstilleggInnvilget = false,
                uttaksgrad = 100,
                saertilleggInnvilget = false
            ),
            brukersSivilstand = MetaforceSivilstand.GIFT,
            epsVedVirk = EndringAvAlderspensjonSivilstandDto.EpsVedVirk(
                borSammenMedBruker = true,
                harInntektOver2G = false,
                mottarOmstillingsstonad = false,
                mottarPensjon = true
            ),
            garantitillegg = null,
            grunnpensjon = Kroner(180000),
            kravAarsak = KravArsakType.VURDER_SERSKILT_SATS,
            kravVirkDatoFom = LocalDate.of(2025, 6, 1),
            regelverkType = AlderspensjonRegelverkType.AP2016,
            saerskiltSatsErBrukt = true,
            totalPensjon = Kroner(320000),
            ufoereKombinertMedAlder = false,
            vedtakEtterbetaling = false,
            dineRettigheterOgMulighetTilAaKlageDto = DineRettigheterOgMulighetTilAaKlageDto(
                sakstype = Sakstype.ALDER,
                brukerUnder18Aar = false
            ),
            maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
            maanedligPensjonFoerSkattAP2025Dto = MaanedligPensjonFoerSkattAP2025Dto(
                beregnetPensjonPerManedGjeldende = MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
                    inntektspensjon = Kroner(1000),
                    totalPensjon = Kroner(2000),
                    garantipensjonInnvilget = Kroner(500),
                    garantipensjon = Kroner(1000),
                    minstenivaIndividuell = Kroner(0),
                    virkDatoFom = LocalDate.now(),
                    virkDatoTom = null,
                ),
                beregnetPensjonperManed = listOf(),
                kravVirkFom = LocalDate.now()
            ),
        )
    )