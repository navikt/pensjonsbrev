package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto.Uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtvikling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsperiode
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Gradsokning
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate
import java.time.Month

fun createEndretUforetrygdPGAInntektNesteArDto() =
    EndretUforetrygdPGAInntektNesteArAutoDto(
        vedtakData = createEndretUforetrygdPGAInntektNesteArData(),
    )

fun createEndretUforetrygdPGAInntektNesteArRedigerbarDto() =
    EndretUforetrygdPGAInntektNesteArRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = EndretUforetrygdPGAInntektNesteArRedigerbarDto.PesysData(
            vedtakData = createEndretUforetrygdPGAInntektNesteArData(),
        ),
    )

fun createEndretUforetrygdPGAInntektNesteArData() =
    EndretUforetrygdPGAInntektNesteArDto(
        uforetrygd = Uforetrygd(
            netto = Kroner(1),
            endringsbelop = Kroner(1),
            inntektBruktIAvkortning = Kroner(0),
            inntektstak = Kroner(1),
            inntektEtterUforhet = Kroner(4),
            okningUforegradVedArskjoring = true,
            fribelop = Kroner(3),
            bunnfradrag = Kroner(4),
            erGradert = true,
            fribelopsperioder = listOf(
                Fribelopsperiode(
                    fom = LocalDate.of(2027, Month.JANUARY, 1),
                    tom = LocalDate.of(2027, Month.JUNE, 30),
                    faktor = 1.0,
                    grunnbelop = Kroner(136000),
                    venteperiodeStartDato = LocalDate.of(2025, Month.JANUARY, 1),
                    gradsokning = true
                ),
                Fribelopsperiode(
                    fom = LocalDate.of(2027, Month.JULY, 1),
                    tom = LocalDate.of(2027, Month.DECEMBER, 31),
                    faktor = 0.4,
                    grunnbelop = Kroner(136000),
                    venteperiodeStartDato = LocalDate.of(2027, Month.JULY, 1),
                    gradsokning = true
                )
            ),
            fribelopUtvikling = FribelopUtvikling(
                kategori = FribelopUtviklingKategori.FRIBELOP_1G_TIL_04G,
                gradsokning = Gradsokning(
                    dato = LocalDate.of(2027, Month.JULY, 1),
                    uforegradFoerOkning = 80
                ),
                fribelopsokning = Fribelopsokning(
                    dato = LocalDate.of(2027, Month.JANUARY, 1),
                    _2arEtterGradsokning = true
                )
            ),
        ),
        barnetilleggFellesbarn = BarnetilleggFellesbarn(
            netto = Kroner(1000),
            inntektBruker = Kroner(1000),
            inntektAnnenForelder = Kroner(1000),
            periodisert = false,
        ),
        barnetilleggSaerkullsbarn = BarnetilleggSaerkullsbarn(
            netto = Kroner(1000),
            periodisert = false,
            inntektBruktIAvkortning = Kroner(1000)
        ),
        gjenlevendetillegg = null,
        virkningFom = LocalDate.of(2027, 1, 1),
        opphorDato = null,
        totalNetto = Kroner(1),
        totalNettoAr = Kroner(12),
        datoForNormertPensjonsalder = vilkaarligDato,
        sokerMottarApIlaAret = false,
        btfbEndret = false,
        btsbEndret = false,
        brukerBorINorge = true,
        pe = createPEgruppe10(),
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto(),
        gjtEndret = false
    )