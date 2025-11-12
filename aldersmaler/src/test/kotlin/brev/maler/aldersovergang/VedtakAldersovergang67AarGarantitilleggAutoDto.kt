package brev.maler.aldersovergang

import brev.maler.vedlegg.createMaanedligPensjonFoerSkatt
import brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.model.alder.aldersovergang.AlderspensjonVedVirk
import no.nav.pensjon.brev.model.alder.aldersovergang.BeregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.model.alder.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createVedtakAldersovergang67AarGarantitilleggAutoDto(): VedtakAldersovergang67AarGarantitilleggAutoDto =
    VedtakAldersovergang67AarGarantitilleggAutoDto(
        virkFom = LocalDate.of(2024, 7, 1),
        beregnetPensjonPerManedVedVirk = BeregnetPensjonPerManedVedVirk(
            garantitillegg = Kroner(123)
        ),
        alderspensjonVedVirk = AlderspensjonVedVirk(
            totalPensjon = Kroner(5678),
            uttaksgrad = 100
        ),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto(Sakstype.ALDER),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        maanedligPensjonFoerSkattAP2025Dto = null,
    )