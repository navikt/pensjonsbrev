package no.nav.pensjon.brev.alder.maler.aldersovergang

import no.nav.pensjon.brev.alder.maler.vedlegg.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.aldersovergang.AlderspensjonVedVirk
import no.nav.pensjon.brev.alder.model.aldersovergang.BeregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.alder.model.aldersovergang.VedtakAldersovergang67AarGarantitilleggAutoDto
import no.nav.pensjon.brev.api.model.Sakstype
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