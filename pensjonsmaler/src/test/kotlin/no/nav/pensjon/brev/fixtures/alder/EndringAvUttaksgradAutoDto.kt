package no.nav.pensjon.brev.fixtures.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderAP2025Dto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningAlderDto
import no.nav.pensjon.brev.maler.vedlegg.createOpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createEndringAvUttaksgradAutoDto() = EndringAvUttaksgradAutoDto(
    alderspensjonVedVirk = EndringAvUttaksgradAutoDto.AlderspensjonVedVirk(
        privatAFPerBrukt = false,
        skjermingstilleggInnvilget = false,
        totalPensjon = Kroner(20000),
        ufoereKombinertMedAlder = false,
        uttaksgrad = 0,
    ),
    harFlereBeregningsperioder = false,
    kravVirkDatoFom = LocalDate.of(2025, 1, 1),
    regelverkType = AlderspensjonRegelverkType.AP2011,
    dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto(),
    maanedligPensjonFoerSkattAP2025Dto = MaanedligPensjonFoerSkattAP2025Dto(
        beregnetPensjonPerManedGjeldende = MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
            inntektspensjon = Kroner(1000),
            totalPensjon = Kroner(2000),
            garantipensjon = Kroner(1000),
            minstenivaIndividuell = Kroner(1000),
            virkDatoFom = LocalDate.now(),
            virkDatoTom = null,
        ),
        beregnetPensjonperManed = listOf(),
        kravVirkFom = LocalDate.now()
    ),
    maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
    opplysningerBruktIBeregningenEndretUttaksgradDto = createOpplysningerBruktIBeregningenEndretUttaksgradDto(),
    orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
)