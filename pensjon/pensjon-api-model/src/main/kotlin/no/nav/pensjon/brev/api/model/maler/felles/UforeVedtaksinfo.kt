package no.nav.pensjon.brev.api.model.maler.felles

import java.time.LocalDate

data class UforeVedtaksinfo(
    val virkningsdatoTidligereMnd: Boolean,
    val vedtakFattetDatoEllerIdag: LocalDate,
)