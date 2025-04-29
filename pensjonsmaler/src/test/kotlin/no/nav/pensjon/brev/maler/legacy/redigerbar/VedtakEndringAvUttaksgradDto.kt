package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Percent
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvUttaksgradDto() =
    VedtakEndringAvUttaksgradDto(
        saksbehandlerValg = VedtakEndringAvUttaksgradDto.SaksbehandlerValg(
            visEtterbetaling = false
        ),
        pesysData = VedtakEndringAvUttaksgradDto.PesysData(
            orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
            maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
            krav = VedtakEndringAvUttaksgradDto.Krav(
                kravInitiertAv = KravInitiertAv.BRUKER,
                virkDatoFom = LocalDate.of(2024, Month.JANUARY, 1)
            ),
            alderspensjonVedVirk = VedtakEndringAvUttaksgradDto.AlderspensjonVedVirk(
                uttaksgrad = Percent(90),
                uforeKombinertMedAlder = true,
                totalPensjon = Kroner(1000),
                privatAFPErBrukt = true,
                regelverkType = AlderspensjonRegelverkType.AP2011,
                opphortEktefelletillegg = true,
                opphortBarnetillegg = true
            ),
            beregnetPensjonPerManed = VedtakEndringAvUttaksgradDto.BeregnetPensjonPerManed(
                antallBeregningsperioderPensjon = 5
            ),
            vedtak = VedtakEndringAvUttaksgradDto.Vedtak(
                etterbetaling = true
            ),
            maanedligPensjonFoerSkattAP2025Dto = null,
            opplysningerBruktIBeregningenEndretUttaksgradDto = createOpplysningerBruktIBeregningenEndretUttaksgradDto()
        )
    )