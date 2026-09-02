package no.nav.pensjon.brev.alder.maler.endring

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.maler.sivilstand.createMaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.KravArsakType
import no.nav.pensjon.brev.alder.model.endring.VedtakEndringVedFlyttingMellomLandDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringVedFlyttingMellomLandDto() = VedtakEndringVedFlyttingMellomLandDto(
    saksbehandlerValg = lagSaksbehandlervalg(
        "innvandret" to false,
        "reduksjonTilbakeITid" to true,
        "endringIPensjonen" to true,
        "etterbetaling" to true,
        "aarsakTilAtPensjonenOeker" to VedtakEndringVedFlyttingMellomLandDto.AarsakTilAtPensjonenOeker.EKSPORTFORBUD_FLYKTNING.name,
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
        informasjonOmMedlemskap = InformasjonOmMedlemskap.EOES,
        erEtterbetaling1Maaned = true,
        maanedligPensjonFoerSkatt = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025 = createMaanedligPensjonFoerSkattAP2025Dto(),
        opplysningerBruktIBeregningen = createOpplysningerBruktIBeregningAlderDto(),
        opplysningerBruktIBeregningenAlderAP2025Dto = createOpplysningerBruktIBeregningAlderAP2025Dto(),
        opplysningerOmAvdoedBruktIBeregning = null,
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto()
    )
)