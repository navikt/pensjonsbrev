package no.nav.pensjon.brev.api.model.maler.felles

import java.time.LocalDate

data class Vedtak(
    val virkFom: LocalDate,
    val virkningsdatoTidligereMnd: Boolean,
    val vedtakFattetDatoEllerIdag: LocalDate,
)