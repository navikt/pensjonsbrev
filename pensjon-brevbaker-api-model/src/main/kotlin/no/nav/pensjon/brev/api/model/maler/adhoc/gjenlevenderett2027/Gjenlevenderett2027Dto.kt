package no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

data class Gjenlevenderett2027Dto(
    val inntekt2019: Int,
    val inntekt2020: Int,
    val inntekt2021: Int,
    val inntekt2022: Int,
    val inntekt2023: Int,

    val inntekt2019G: Double,
    val inntekt2020G: Double,
    val inntekt2021G: Double,
    val inntekt2022G: Double,
    val inntekt2023G: Double,

    val gjennomsnittInntektG: Double,

    val inntekt2022Over3g: Boolean,
    val inntekt2023Over3g: Boolean,
): BrevbakerBrevdata
