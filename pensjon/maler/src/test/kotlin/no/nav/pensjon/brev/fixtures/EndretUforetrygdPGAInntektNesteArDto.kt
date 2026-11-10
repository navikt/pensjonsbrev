package no.nav.pensjon.brev.fixtures

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto.BarnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto.BarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto.Uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArAutoDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArRedigerbarDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Fribelopsperiode
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

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
            fribelop1GDesemberIfjor = false,
            fribelopsperioder = listOf(
                Fribelopsperiode(
                    fom = LocalDate.of(2026, 1, 1),
                    tom = LocalDate.of(2026, 12, 31),
                    faktor = 0.4,
                    grunnbelop = Kroner(136000),
                    venteperiodeStartDato = LocalDate.of(2026, 7, 1),
                    gradsokning = true
                )
            ),
            fribelop1G = false,
            fribelop1GTil04G = null,
            fribelop04GTil1GTil04G = EndretUforetrygdPGAInntektNesteArDto.Fra1GTil04G(LocalDate.of(2026, 1, 1), 80),
            fribelop04GTil1G = false,
            datoOkningFribelop = LocalDate.of(2027, 1, 1),
            venteperiodeEtterGradsokning = true,
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
        virkningFom = vilkaarligDato,
        opphorDato = vilkaarligDato,
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