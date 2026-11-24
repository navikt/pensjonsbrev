package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.InntektOgAldersovergangNesteArAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.InntektOgAldersovergangNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.InntektOgAldersovergangNesteArDto.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.InntektOgAldersovergangNesteArDto.BarnetilleggSerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.InntektOgAldersovergangNesteArRedigerbarDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createInntektOgAldersovergangNesteArAutoDto() =
    InntektOgAldersovergangNesteArAutoDto(
        data = createInntektOgAldersovergangNesteArData(),
    )

fun createInntektOgAldersovergangNesteArRedigerbarDto() =
    InntektOgAldersovergangNesteArRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = InntektOgAldersovergangNesteArRedigerbarDto.PesysData(
            data = createInntektOgAldersovergangNesteArData(),
        ),
    )

fun createInntektOgAldersovergangNesteArData() =
    InntektOgAldersovergangNesteArDto(
        begrensningFribelop = true,
        vedtakVirkFom = LocalDate.of(2027, 1, 1),
        gradertUforetrygd = true,
        nettoUforetrygd = Kroner(20000),
        nettoBarnetillegg = Kroner(3000),
        nettoGjenlevendetillegg = null,
        nettoTotal = Kroner(23000),
        bunnfradrag = Kroner(75000),
        fribelop = Kroner(50000),
        inntektEtterUforhet = Kroner(25000),
        datoFyller67 = LocalDate.of(2027, 6, 15),
        datoForNormertPensjonsalder = LocalDate.of(2027, 7, 1),
        hjemler = setOf("12-14", "22-12"),
        btfb = BarnetilleggFellesbarn(
            netto = Kroner(2000),
            inntektBruker = Kroner(150000),
            inntektAnnenForelder = Kroner(100000),
            erEndret = true,
        ),
        btsb = BarnetilleggSerkullsbarn(
            netto = Kroner(1000),
            inntektBruktIAvkortning = Kroner(150000),
            erEndret = true,
        ),
        pe = createPEgruppe10(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto().copy(
            harInnvilgetBarnetilleggFellesBarn = true,
            harInnvilgetBarnetilleggSaerkullsbarn = true,
        ),
    )
