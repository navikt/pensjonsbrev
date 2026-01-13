package no.nav.pensjon.brev.alder.maler.sivilstand

import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.SivilstandAvdoed
import no.nav.pensjon.brev.alder.model.sivilstand.VedtakOmregningAFPTilEnsligPensjonistAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createVedtakOmregningAFPTilEnsligPensjonistAutoDto() =
    VedtakOmregningAFPTilEnsligPensjonistAutoDto(
        kravVirkDatoFom = LocalDate.of(2025, 6, 1),
        avdoed =
            VedtakOmregningAFPTilEnsligPensjonistAutoDto.Avdoed(
                navn = "Ola Nordmann",
                sivilstand = SivilstandAvdoed.GIFT,
            ),
        erEndret = true,
        harBarnUnder18 = true,
        beregnetPensjonPerManedVedVirk =
            VedtakOmregningAFPTilEnsligPensjonistAutoDto
                .BeregnetPensjonPerManedVedVirk(
                    totalPensjon = Kroner(25000),
                    saertillegg = true,
                    minstenivaaIndividuelt = false,
                ),
        etterbetaling = true,
        antallBeregningsperioder = 3,
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(),
        maanedligPensjonFoerSkattAFPOffentligDto = createMaanedligPensjonFoerSkattAFPOffentligDto(),
    )
