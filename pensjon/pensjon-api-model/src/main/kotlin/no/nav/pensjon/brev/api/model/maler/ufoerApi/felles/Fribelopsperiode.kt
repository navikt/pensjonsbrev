package no.nav.pensjon.brev.api.model.maler.ufoerApi.felles

import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class Fribelopsperiode(
        val fom: LocalDate,
        val tom: LocalDate,
        val gradsokning: Boolean,
        val faktor: Double,
        val grunnbelop: Kroner,
        val venteperiodeStartDato: LocalDate,
    )