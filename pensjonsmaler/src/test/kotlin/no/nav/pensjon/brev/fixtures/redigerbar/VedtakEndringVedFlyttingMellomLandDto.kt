package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringVedFlyttingMellomLandDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkattAP2025
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringVedFlyttingMellomLandDto() = VedtakEndringVedFlyttingMellomLandDto(
    saksbehandlerValg = VedtakEndringVedFlyttingMellomLandDto.SaksbehandlerValg(
        innvandret = false,
        reduksjonTilbakeITid = true,
        endringIPensjonen = true,
        etterbetaling = true
    ),
    pesysData = VedtakEndringVedFlyttingMellomLandDto.PesysData(
        krav = VedtakEndringVedFlyttingMellomLandDto.PesysData.Krav(
            virkDatoFom = LocalDate.of(2024, Month.MAY, 10),
            aarsak = VedtakEndringVedFlyttingMellomLandDto.Aarsak.UTVANDRET
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
            grunnnpensjon = Kroner(200)
        ),
        erEtterbetaling1Maaned = true,
        dineRettigheterOgMulighetTilAaKlage = createDineRettigheterOgMulighetTilAaKlageDto(),
        maanedligPensjonFoerSkatt = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025 = createMaanedligPensjonFoerSkattAP2025()
    )
)