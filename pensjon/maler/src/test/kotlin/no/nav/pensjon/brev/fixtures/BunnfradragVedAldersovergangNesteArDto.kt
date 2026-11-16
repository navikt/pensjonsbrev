package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.BunnfradragVedAldersovergangNesteArAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.BunnfradragVedAldersovergangNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.BunnfradragVedAldersovergangNesteArDto.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.BunnfradragVedAldersovergangNesteArDto.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.BunnfradragVedAldersovergangNesteArRedigerbarDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createBunnfradragVedAldersovergangNesteArAutoDto() =
    BunnfradragVedAldersovergangNesteArAutoDto(
        data = createBunnfradragVedAldersovergangNesteArData(),
    )

fun createBunnfradragVedAldersovergangNesteArRedigerbarDto() =
    BunnfradragVedAldersovergangNesteArRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = BunnfradragVedAldersovergangNesteArRedigerbarDto.PesysData(
            data = createBunnfradragVedAldersovergangNesteArData(),
        ),
    )

fun createBunnfradragVedAldersovergangNesteArData() =
    BunnfradragVedAldersovergangNesteArDto(
        vedtakVirkFom = LocalDate.of(2027, 1, 1),
        gradertUforetrygd = true,
        nettoUforetrygd = Kroner(20000),
        nettoBarnetillegg = Kroner(3000),
        nettoGjenlevendetillegg = null,
        btfbEndret = true,
        btsbEndret = true,
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
            periodisert = true,
        ),
        btsb = BarnetilleggSaerkullsbarn(
            netto = Kroner(1000),
            inntektBruktIAvkortning = Kroner(150000),
            periodisert = true,
        ),
        pe = createPEgruppe10(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto().copy(
            harInnvilgetBarnetilleggFellesBarn = true,
            harInnvilgetBarnetilleggSaerkullsbarn = true,
        ),
    )
