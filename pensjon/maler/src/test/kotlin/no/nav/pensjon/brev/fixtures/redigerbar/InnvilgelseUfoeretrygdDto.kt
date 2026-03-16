package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createInnvilgelseUfoeretrygdDto() =
    InnvilgelseUfoeretrygdDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = InnvilgelseUfoeretrygdDto.PesysData(
            pe = createPEgruppe10(),
            oifuVedVirkningstidspunkt = Kroner(10000),
            maanedligUfoeretrygdFoerSkatt = null,
            createOrienteringOmRettigheterUfoereDto(),
            barnetilleggInnvilget = listOf(
                InnvilgelseUfoeretrygdDto.Barnetillegg(LocalDate.of(1990, Month.JANUARY, 1), InnvilgelseUfoeretrygdDto.BarnetilleggResultatCode.INNVILGET),
                InnvilgelseUfoeretrygdDto.Barnetillegg(LocalDate.of(1991, Month.FEBRUARY, 2), InnvilgelseUfoeretrygdDto.BarnetilleggResultatCode.INNVILGET),
                InnvilgelseUfoeretrygdDto.Barnetillegg(LocalDate.of(1992, Month.MARCH, 3), InnvilgelseUfoeretrygdDto.BarnetilleggResultatCode.INNVILGET),
            ),
            barnetilleggAvslatt = listOf(
                InnvilgelseUfoeretrygdDto.Barnetillegg(LocalDate.of(1990, Month.APRIL, 4), InnvilgelseUfoeretrygdDto.BarnetilleggResultatCode.BT_INNT_OVER_1G),
                InnvilgelseUfoeretrygdDto.Barnetillegg(LocalDate.of(1991, Month.MAY, 5), InnvilgelseUfoeretrygdDto.BarnetilleggResultatCode.BT_GITT_TIL_ANNEN)
            ),
        ),
    )
