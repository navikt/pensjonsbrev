package no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata

data class Gjenlevenderett2027Dto(
    val inntekt2019: Int,
    val inntekt2020: Int,
    val inntekt2021: Int,
    val inntekt2022: Int,
    val inntekt2023: Int,
    val gjennomsnitt: Int,

    val inntekt2022Over3g: Boolean,
    val inntekt2023Over3g: Boolean,
): BrevbakerBrevdata
