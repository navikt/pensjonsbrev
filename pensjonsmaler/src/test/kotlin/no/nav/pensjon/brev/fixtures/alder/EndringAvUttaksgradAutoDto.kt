package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvUttaksgradAutoDto() = EndringAvUttaksgradAutoDto(
alderspensjonVedVirk = EndringAvUttaksgradAutoDto.AlderspensjonVedVirk(
    privatAFPerBrukt = false,
    skjermingstilleggInnvilget = false,
    totalPensjon = Kroner(20000),
    ufoereKombinertMedAlder = false,
    uttaksgrad = 50,
),
    harFlereBeregningsperioder = false,
    kravVirkDatoFom = LocalDate.of(2025, 1, 1),
    regelverkType = AlderspensjonRegelverkType.AP2011,
    dineRettigheterOgMulighetTilAaKlageDto =
)