package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class AvslagUttakFoerNormertPensjonsalderAutoDto(
    val minstePensjonssats: Kroner,
    val uttaksgrad: Int,
    val normertPensjonsalder: NormertPensjonsalder,
    val virkFom: LocalDate,
    val totalPensjon: Kroner,
    val afpBruktIBeregning: Boolean
) : BrevbakerBrevdata

data class NormertPensjonsalder(
    val aar: Int,
    val maaneder: Int,
)