package no.nav.pensjon.brev.alder.maler.endring

import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.KravInitiertAv
import no.nav.pensjon.brev.alder.model.endring.VedtakEndringAvUttaksgradDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Percent
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvUttaksgradDto() =
    VedtakEndringAvUttaksgradDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "etterbetaling" to true,
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
            maanedligPensjonFoerSkattAP2025Dto = null,
            opplysningerBruktIBeregningenEndretUttaksgradDto = createOpplysningerBruktIBeregningenEndretUttaksgradDto()
        )
    )