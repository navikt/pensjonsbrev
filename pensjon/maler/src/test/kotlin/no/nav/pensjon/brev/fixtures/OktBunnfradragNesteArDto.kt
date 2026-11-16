package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.OktBunnfradragNesteArAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.OktBunnfradragNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.OktBunnfradragNesteArRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtvikling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsperiode
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createOktBunnfradragNesteArDto() =
    OktBunnfradragNesteArAutoDto(
        data = createOktBunnfradragNesteArData(),
    )

fun createOktBunnfradragNesteArRedigerbarDto() =
    OktBunnfradragNesteArRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = OktBunnfradragNesteArRedigerbarDto.PesysData(
            data = createOktBunnfradragNesteArData(),
        ),
    )

fun createOktBunnfradragNesteArData() =
    OktBunnfradragNesteArDto(
        vedtakVirkFom = LocalDate.of(2027, Month.JANUARY, 1),
        gradertUforetrygd = true,
        nettoUforetrygd = Kroner(20000),
        nettoBarnetillegg = Kroner(3000),
        nettoGjenlevendetillegg = null,
        endringUforetrygd = Kroner(500),
        endringBarnetillegg = Kroner(0),
        endringGjenlevendetillegg = Kroner(0),
        bunnfradrag = Kroner(150000),
        inntektEtterUforhet = Kroner(50000),
        fribelop = Kroner(100000),
        grunnbelop = Kroner(136549),
        datoForNormertPensjonsalder = vilkaarligDato,
        sokerMottarApIlaAret = false,
        opphorsdato = null,
        fribelopsperioder = listOf(
            Fribelopsperiode(
                fom = LocalDate.of(2027, Month.JANUARY, 1),
                tom = LocalDate.of(2027, Month.MARCH, 31),
                faktor = 0.4,
                grunnbelop = Kroner(136549),
                venteperiodeStartDato = LocalDate.of(2025, Month.APRIL, 1),
                gradsokning = false,
            ),
            Fribelopsperiode(
                fom = LocalDate.of(2027, Month.APRIL, 1),
                tom = LocalDate.of(2027, Month.DECEMBER, 31),
                faktor = 1.0,
                grunnbelop = Kroner(136549),
                venteperiodeStartDato = LocalDate.of(2025, Month.APRIL, 1),
                gradsokning = false,
            ),
        ),
        fribelopUtvikling = FribelopUtvikling(
            kategori = FribelopUtviklingKategori.FRIBELOP_04G_TIL_1G,
            gradsokning = null,
            fribelopsokning = Fribelopsokning(
                dato = LocalDate.of(2027, Month.APRIL, 1),
                _2arEtterGradsokning = false,
            ),
        ),
        periodisertBarnetillegg = false,
        hjemler = setOf("12-14", "22-12"),
        pe = createPEgruppe10(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
    )
