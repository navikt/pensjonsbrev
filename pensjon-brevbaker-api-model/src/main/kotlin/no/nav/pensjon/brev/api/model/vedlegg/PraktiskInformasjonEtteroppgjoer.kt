package no.nav.pensjon.brev.api.model.vedlegg

import java.time.LocalDate

// Conditional for showing the attachment: The attachment is mandatory to letter
@Suppress("unused")
data class PraktiskInformasjonEtteroppgjoerDto(
    val ufoeretrygdEtterOppgjoerPeriodeFom: LocalDate
)