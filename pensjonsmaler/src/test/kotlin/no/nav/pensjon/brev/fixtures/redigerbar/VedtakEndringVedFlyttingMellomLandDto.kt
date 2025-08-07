package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto
import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkattAP2025
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringVedFlyttingMellomLandDto() = VedtakEndringVedFlyttingMellomLandDto(
    saksbehandlerValg = VedtakEndringVedFlyttingMellomLandDto.SaksbehandlerValg(
        innvandret = false,
        reduksjonTilbakeITid = true,
        endringIPensjonen = true,
        etterbetaling = true,
        aarsakTilAtPensjonenOeker = VedtakEndringVedFlyttingMellomLandDto.AarsakTilAtPensjonenOeker.EKSPORTFORBUD_FLYKTNING,
    ),
    pesysData = VedtakEndringVedFlyttingMellomLandDto.PesysData(
        krav = VedtakEndringVedFlyttingMellomLandDto.PesysData.Krav(
            virkDatoFom = LocalDate.of(2024, Month.MAY, 10),
            aarsak = KravArsakType.UTVANDRET
        ),
        bruker = VedtakEndringVedFlyttingMellomLandDto.PesysData.Bruker(
            faktiskBostedsland = "Spania",
            borIEOES = true,
            borIAvtaleland = false
        ),
        alderspensjonVedVirk = VedtakEndringVedFlyttingMellomLandDto.PesysData.AlderspensjonVedVirk(
            erEksportberegnet = false,
            garantipensjonInnvilget = true,
            pensjonstilleggInnvilget = true,
            minstenivaaIndividuellInnvilget = false,
            minstenivaaPensjonistParInnvilget = false,
            uforeKombinertMedAlder = true,
            totalPensjon = Kroner(1000),
            gjenlevenderettAnvendt = false,
            fullUttaksgrad = true
        ),
        inngangOgEksportVurdering = VedtakEndringVedFlyttingMellomLandDto.PesysData.InngangOgEksportVurdering(
            eksportForbudKode = null,
            minst20AarTrygdetid = true,
            eksportTrygdeavtaleEOES = true,
            eksportTrygdeavtaleAvtaleland = false
        ),
        inngangOgEksportVurderingAvdoed = null,
        opphoersbegrunnelseVedVirk = VedtakEndringVedFlyttingMellomLandDto.PesysData.OpphoersbegrunnelseVedVirk(
            begrunnelseET = VedtakEndringVedFlyttingMellomLandDto.Opphoersbegrunnelse.BRUKER_FLYTTET_IKKE_AVT_LAND,
            begrunnelseBT = VedtakEndringVedFlyttingMellomLandDto.Opphoersbegrunnelse.ANNET
        ),
        ytelseskomponentInformasjon = VedtakEndringVedFlyttingMellomLandDto.PesysData.YtelseskomponentInformasjon(
            beloepEndring = BeloepEndring.ENDR_OKT
        ),
        beregnetpensjonPerMaanedVedVirk = VedtakEndringVedFlyttingMellomLandDto.PesysData.BeregnetPensjonPerMaanedVedVirk(
            grunnpensjon = Kroner(200)
        ),
        erEtterbetaling1Maaned = true,
        dineRettigheterOgMulighetTilAaKlage = createDineRettigheterOgMulighetTilAaKlageDto(),
        maanedligPensjonFoerSkatt = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025 = createMaanedligPensjonFoerSkattAP2025(),
        opplysningerBruktIBeregningen = createOpplysningerBruktIBeregningAlderDto(),
        opplysningerBruktIBeregningenAlderAP2025Dto = createOpplysningerBruktIBeregningAlderAP2025Dto(),
        opplysningerOmAvdoedBruktIBeregning = null,
        informasjonOmMedlemskapOgHelserettigheterDto = InformasjonOmMedlemskapOgHelserettigheterDto(erEOSLand = true)
    )
)