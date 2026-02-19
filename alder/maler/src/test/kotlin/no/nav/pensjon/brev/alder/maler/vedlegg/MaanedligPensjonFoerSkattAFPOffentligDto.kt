package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate

fun createMaanedligPensjonFoerSkattAFPOffentligDto() =
    MaanedligPensjonFoerSkattAFPOffentligDto(
        afpStatGjeldende =
            MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatGjeldende(
                erInntektsavkortet = true,
                er70ProsentRegelAvkortet = true,
            ),
        beregnetPensjonPerManedGjeldende = createAFPStatBeregning(),
        tilleggspensjonAFPStatGjeldende =
            MaanedligPensjonFoerSkattAFPOffentligDto.TilleggspensjonAFPStatGjeldende(
                erRedusert = true,
            ),
        beregnetPensjonPerManedVedVirk = createAFPStatBeregning(),
        beregnetPensjonPerManed =
            MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatBeregningListe(
                antallBeregningsperioder = 2,
                afpStatBeregningListe =
                    listOf(
                        createAFPStatBeregning().copy(
                            totalPensjon = Kroner(100),
                            totalPensjonForAvkort = Kroner(100),
                        ),
                        createAFPStatBeregning().copy(
                            virkDatoFom = LocalDate.now().minusMonths(4).withDayOfMonth(1),
                            virkDatoTom =
                                LocalDate
                                    .now()
                                    .minusMonths(3)
                                    .withDayOfMonth(1)
                                    .minusDays(1),
                        ),
                    ),
            ),
        kravVirkDatoFom = LocalDate.now().minusMonths(3).withDayOfMonth(1),
    )

private fun createAFPStatBeregning() =
    MaanedligPensjonFoerSkattAFPOffentligDto.AFPStatBeregning(
        fullTrygdetid = true,
        brukerErFlyktning = true,
        grunnbelop = Kroner(1),
        grunnpensjon = Kroner(2),
        grunnpensjonForAvkort = Kroner(3),
        tilleggspensjon = Kroner(4),
        tilleggspensjonForAvkort = Kroner(5),
        sartillegg = Kroner(6),
        sartilleggForAvkort = Kroner(7),
        afpTillegg = Kroner(8),
        afpTilleggForAvkort = Kroner(9),
        minstenivaIndividuell = Kroner(10),
        minstenivaIndividuellForAvkort = Kroner(11),
        totalPensjon = Kroner(12),
        totalPensjonForAvkort = Kroner(13),
        fasteUtgifterVedInstiitusjonsopphold = Kroner(14),
        fasteUtgifterVedInstiitusjonsoppholdForAvkort = Kroner(15),
        familietillegg = Kroner(16),
        familietilleggForAvkort = Kroner(17),
        virkDatoFom = LocalDate.now().minusMonths(3).withDayOfMonth(1),
        virkDatoTom =
            LocalDate
                .now()
                .minusMonths(2)
                .withDayOfMonth(1)
                .minusDays(1),
        fremtidigInntekt = Kroner(18),
    )
