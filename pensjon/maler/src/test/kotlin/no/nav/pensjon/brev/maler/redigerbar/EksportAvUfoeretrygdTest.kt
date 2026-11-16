package no.nav.pensjon.brev.maler.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.utland.EksportAvUforetrygdDto
import no.nav.pensjon.brev.maler.ufore.utland.Arsak
import no.nav.pensjon.brev.maler.ufore.utland.FlyktningArsak
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createEksportAvUfoeretrygdDto() = EksportAvUforetrygdDto(
    saksbehandlerValg = lagSaksbehandlervalg(
        "arsak" to Arsak.FLYTTEDATOFRABRUKER,
        "flyktningArsak" to FlyktningArsak.IKKEFLYKTNING,
        "tyveArNorgeFullEksport" to true,
    ),
    pesysData = EksportAvUforetrygdDto.PesysData(
        fullEksport = true,
        sumUtOgTillegg = Kroner(25_000),
        virkningstidspunkt = LocalDate.of(2026, 1, 1),
        muligTilbakekrevingVarsel = false,
        ensligOgMinstesats = true,
        minstesatsTilBruker = 2.48,
        barnetilleggAvtaleland = true,
        flereBarn = false,
        innvilgetBT = false,
        btInnvilgetMenIngenUtbetaling = false,
        gjtEndret = false,
        gjtUendret = false,
        gjtAvkortet = 0,
        eosNordisk = true,
    )
)