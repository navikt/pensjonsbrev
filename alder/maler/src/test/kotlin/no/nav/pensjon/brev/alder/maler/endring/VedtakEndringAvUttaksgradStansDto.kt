package no.nav.pensjon.brev.alder.maler.endring

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.alder.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.endring.VedtakEndringAvUttaksgradStansBrukerEllerVergeDto
import no.nav.pensjon.brev.alder.model.endring.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto() =
    VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "aarsak" to VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.Aarsak.ufoeretrygdErInnvilget.name
        ),
        pesysData = VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.PesysData(
            krav = VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.Krav(
                virkDatoFom = LocalDate.of(2024, Month.JANUARY, 1)
            ),
            alderspensjonVedVirk = VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.AlderspensjonVedVirk(
                skjermingstilleggInnvilget = false,
                regelverkType = AlderspensjonRegelverkType.AP2016
            ),
            dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
        )
    )

fun createVedtakEndringAvUttaksgradStansBrukerEllerVergeDto() = VedtakEndringAvUttaksgradStansBrukerEllerVergeDto(
    saksbehandlerValg = lagSaksbehandlervalg(),
    pesysData = VedtakEndringAvUttaksgradStansBrukerEllerVergeDto.PesysData(
        krav = VedtakEndringAvUttaksgradStansBrukerEllerVergeDto.Krav(
            virkDatoFom = LocalDate.of(2024, Month.JANUARY, 1)
        ),
        alderspensjonVedVirk = VedtakEndringAvUttaksgradStansBrukerEllerVergeDto.AlderspensjonVedVirk(
            skjermingstilleggInnvilget = false,
            regelverkType = AlderspensjonRegelverkType.AP2016
        ),
        dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
    )
)