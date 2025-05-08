package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravInitiertAv
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import java.time.LocalDate
import java.time.Month

fun createVedtakEndringAvUttaksgradStansDto() = VedtakEndringAvUttaksgradStansDto(
    saksbehandlerValg = EmptyBrevdata,
    pesysData = VedtakEndringAvUttaksgradStansDto.PesysData(
        krav = VedtakEndringAvUttaksgradStansDto.Krav(
            kravInitiertAv = KravInitiertAv.NAV,
            virkDatoFom = LocalDate.of(2024, Month.JANUARY, 1)
        ),
        alderspensjonVedVirk = VedtakEndringAvUttaksgradStansDto.AlderspensjonVedVirk(
            skjermingstilleggInnvilget = false,
            regelverkType = AlderspensjonRegelverkType.AP2016
        ),
        dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
    )
)