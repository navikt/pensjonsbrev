package no.nav.pensjon.brev.api.model.vedlegg

import java.time.LocalDate

// Conditional for showing the attachment: The attachment is mandatory to letter UT_EO_FORHAANDSVARSEL_AUTO
@Suppress("unused")
data class PraktiskInformasjonEtteroppgjoerDto(
    val ufoeretrygdEtterOppgjoerPeriodeFom: LocalDate
)