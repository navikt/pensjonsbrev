package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.legacy.EPSEndringIBarnetilleggAutoDto
import no.nav.pensjon.brev.api.model.maler.legacy.EPSEndringIBarnetilleggData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.EPSEndringIBarnetilleggRedigerbarDto
import no.nav.pensjon.brev.fixtures.createDineRettigheterOgPlikterUforeDto
import no.nav.pensjon.brev.fixtures.createMaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

fun createEPSEndringIBarnetilleggData() =
    EPSEndringIBarnetilleggData(
        virkningstidspunkt = LocalDate.of(2020, 1, 1),
        nettoUforetrygdUtenTillegg = Kroner(10000),
        nettoBarnetilleggFB = Kroner(2000),
        nettoBarnetilleggSB = Kroner(3000),
        totalbelop = Kroner(12000),
        barnetilleggSB = true,
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = createMaanedligUfoeretrygdFoerSkattDto(),
        dineRettigheterOgPlikterUfore = createDineRettigheterOgPlikterUforeDto(),
    )

fun createEPSEndringIBarnetilleggAutoDto() =
    EPSEndringIBarnetilleggAutoDto(
        vedtakData = createEPSEndringIBarnetilleggData(),
    )

fun createEPSEndringIBarnetilleggRedigerbarDto() =
    EPSEndringIBarnetilleggRedigerbarDto(
        saksbehandlerValg = lagSaksbehandlervalg(),
        pesysData = EPSEndringIBarnetilleggRedigerbarDto.PesysData(
            vedtakData = createEPSEndringIBarnetilleggData(),
        )
    )