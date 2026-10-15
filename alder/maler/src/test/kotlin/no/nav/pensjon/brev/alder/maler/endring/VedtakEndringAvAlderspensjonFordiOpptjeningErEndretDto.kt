package no.nav.pensjon.brev.alder.maler.endring

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.maler.sivilstand.createMaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.endring.VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto() =
    VedtakEndringAvAlderspensjonFordiOpptjeningErEndretDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
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
            maanedligPensjonFoerSkatt = createMaanedligPensjonFoerSkatt(),
            maanedligPensjonFoerSkattAP2025 = createMaanedligPensjonFoerSkattAP2025Dto(),
            opplysningerBruktIBeregningenAlder = createOpplysningerBruktIBeregningAlderDto(),
            opplysningerBruktIBeregningenAlderAP2025 = createOpplysningerBruktIBeregningAlderAP2025Dto(),
            opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjening = null,
        )
    )