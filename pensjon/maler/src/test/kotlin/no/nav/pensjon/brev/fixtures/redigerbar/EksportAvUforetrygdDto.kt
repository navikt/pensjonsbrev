package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EksportAvUforetrygdDto
import no.nav.pensjon.brev.fixtures.createDineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import java.time.LocalDate

fun createEksportAvUforetrygdDto() =
    EksportAvUforetrygdDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = EksportAvUforetrygdDto.PesysData(
            pe = createPEgruppe10(),
            maanedligUfoeretrygdFoerSkatt = null,
            dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
            fullEksport = false,
            sumUtOgTillegg = BrevbakerType.Kroner(123),
            virkningstidspunkt = LocalDate.of(2025, 1, 1),
            muligTilbakekrevingVarsel = false,
            ensligOgMinstesats = false,
            minstesatsTilBruker = 123456.0,
            barnetilleggAvtaleland = false,
            flereBarn = true,
            innvilgetBT = false,
            btInnvilgetMenIngenUtbetaling = false,
            gjtEndret = false,
            gjtUendret = false,
            gjtAvkortet = 0,
            eosNordisk = false,
        ),
    )
