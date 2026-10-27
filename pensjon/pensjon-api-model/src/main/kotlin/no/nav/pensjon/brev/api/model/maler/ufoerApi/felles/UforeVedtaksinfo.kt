package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

import java.time.LocalDate

data class UforeVedtaksinfo(
    val virkningsdatoTidligereMnd: Boolean,
    val vedtakFattetDatoEllerIdag: LocalDate,
)