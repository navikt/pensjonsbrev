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
            aarligKontrollEPS = false,
            endringPensjon = false,
            epsAvkallPaaEgenAlderspenspensjon = false,
            epsAvkallPaaEgenUfoeretrygd = false,
            epsHarInntektOver1G = true,
            epsHarRettTilFullAlderspensjon = false,
            epsIkkeFylt62Aar = false,
            epsIkkeRettTilFullAlderspensjon = false,
            epsInntektOekningReduksjon = false,
            epsTarUtAlderspensjon = false,
            epsTarUtAlderspensjonIStatligSektor = false,
            epsTarUtUfoeretrygd = false,
            etterbetaling = false,
            feilutbetaling = false,
            fraFlyttet = false,
            giftBorIkkeSammen = false,
            ingenBetydning = false,
            institusjonsopphold = false,
            pensjonenOeker = false,
            pensjonenRedusert = false,
            samboereMedFellesBarn = false,
            samboereTidligereGift = false,
        ),
        pesysData = EndringAvAlderspensjonSivilstandDto.PesysData(
            alderspensjonVedVirk = EndringAvAlderspensjonSivilstandDto.AlderspensjonVedVirk(
                garantipensjonInnvilget = false,
                minstenivaaIndividuellInnvilget = true,
                minstenivaaPensjonsistParInnvilget = false,
                pensjonstilleggInnvilget = false,
                saertilleggInnvilget = false,
                ufoereKombinertMedAlder =false,
                uttaksgrad = 100,
            ),
            beregnetPensjonPerManedVedVirk = EndringAvAlderspensjonSivilstandDto.BeregnetPensjonPerManedVedVirk(
                garantitillegg = null,
                grunnbelop = Kroner(124028),
                grunnpensjon = Kroner(320000),
                totalPensjon = Kroner(340000),
            ),
            epsVedVirk = EndringAvAlderspensjonSivilstandDto.EpsVedVirk(
                borSammenMedBruker = true,
                harInntektOver2G = false,
                mottarOmstillingsstonad = false,
                mottarPensjon = false,
            ),
            kravAarsak = KravArsakType.SIVILSTANDSENDRING,
            kravVirkDatoFom = LocalDate.of(2025, 6, 1),
            regelverkType = AlderspensjonRegelverkType.AP2011,
            saerskiltSatsErBrukt = false,
            sivilstand = MetaforceSivilstand.GIFT,
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
                    garantipensjon = Kroner(1000),
                    minstenivaIndividuell = Kroner(1000),
                    virkDatoFom = LocalDate.now(),
                    virkDatoTom = null,
                ),
                beregnetPensjonperManed = listOf(),
                kravVirkFom = LocalDate.now()
            ),
        )
    )