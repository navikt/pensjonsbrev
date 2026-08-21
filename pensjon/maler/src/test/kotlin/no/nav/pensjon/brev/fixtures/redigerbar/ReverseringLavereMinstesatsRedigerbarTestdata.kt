package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.LopendeYtelse
import no.nav.pensjon.brev.api.model.maler.legacy.OpphortYtelse
import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.ReverseringLavereMinstesatsRedigerbarDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createReverseringLavereMinstesatsData() =
    ReverseringLavereMinstesatsDto(
        opphortYtelse = OpphortYtelse(
            opphorsdato = LocalDate.of(2026, 10, 1)
        ),
        lopendeYtelse = LopendeYtelse(
            nettoTotal = Kroner(32000),
            nettoUforetrygd = Kroner(26000),
            nettoBarnetillegg = Kroner(4000),
            nettoGjenlevendetillegg = Kroner(2000),
            reduksjonsprosent = 50.0,
            brukersMinstesats = 300000.0,
            avkortetPgaRedusertTrygdetid = true,
            harGradertUfoeretrygd = true
        ),
        etterbetaling = Kroner(10000),
        hjemmeltekst = "§§ 12-13 til 12-16, 12-18 og 22-12",
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
    )

fun createReverseringLavereMinstesatsRedigerbarDto() =
    ReverseringLavereMinstesatsRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = ReverseringLavereMinstesatsRedigerbarDto.PesysData(
            data = createReverseringLavereMinstesatsData(),
        )
    )

fun createReverseringLavereMinstesatsAutoDto() =
    ReverseringLavereMinstesatsAutoDto(
        data = createReverseringLavereMinstesatsData(),
    )
