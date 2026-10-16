package no.nav.pensjon.brev.api.model.maler.felles

import java.time.LocalDate

data class Kravhode(
    val virkFom: LocalDate,
    val mottattDato: LocalDate,
    val arsak: Arsak,
)
{
    enum class Arsak {
        OMGJ_ETTER_KLAGE, OMGJ_ETTER_ANKE, ANNEN
    }
}