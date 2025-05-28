package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto() = VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto(
    saksbehandlerValg = VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.SaksbehandlerValg(
        ufoeretrygdErInnvilget = true,
        ufoeregradErOekt = false,
        pensjonsopptjeningenErEndret = false
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
    saksbehandlerValg = EmptyBrevdata,
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