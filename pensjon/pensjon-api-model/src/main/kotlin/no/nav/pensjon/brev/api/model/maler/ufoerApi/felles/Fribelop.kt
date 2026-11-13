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

data class FribelopUtvikling(
    val kategori: FribelopUtviklingKategori,
    val gradsokning: Gradsokning?,
    val fribelopsokning: Fribelopsokning?,
)

data class Gradsokning(
    val dato: LocalDate,
    val uforegradFoerOkning: Int,
)

data class Fribelopsokning(
    val dato: LocalDate,
    val _2arEtterGradsokning: Boolean,
)

enum class FribelopUtviklingKategori {
    FRIBELOP_1G_FRA_IFJOR,
    FRIBELOP_1G_FRA_JAN,
    FRIBELOP_1G_TIL_04G,
    FRIBELOP_04G_TIL_1G,
    FRIBELOP_04G_TIL_1G_TIL_04G,
    FRIBELOP_04G
}