package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto() =
    VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.PesysData(
            krav = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.Krav(
                virkDatoFom = LocalDate.of(2024, Month.MAY, 1),
                arsakErEndretOpptjening = true,
                erForstegangsbehandling = true
            ),
            alderspensjonVedVirk = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.AlderspensjonVedVirk(
                totalPensjon = Kroner(1000),
                uforeKombinertMedAlder = true,
                regelverkType = AlderspensjonRegelverkType.AP2025,
                fullUttaksgrad = true
            ),
            ytelseskomponentInformasjon = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.YtelseskomponentInformasjon(
                belopEndring = BeloepEndring.ENDR_OKT
            ),
            behandlingKontekst = VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto.BehandlingKontekst(
                konteksttypeErKorrigeringopptjening = false
            ),
            etterbetaling = true,
            orienteringOmRettigheterOgPlikter = createOrienteringOmRettigheterOgPlikterDto(),
            maanedligPensjonFoerSkatt = Fixtures.createVedlegg(),
            maanedligPensjonFoerSkattAP2025 = Fixtures.createVedlegg(),
            opplysningerBruktIBeregningenAlder = createOpplysningerBruktIBeregningAlderDto(),
            opplysningerBruktIBeregningenAlderAP2025 = createOpplysningerBruktIBeregningAlderAP2025Dto(),
            opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjening = null,
        )
    )